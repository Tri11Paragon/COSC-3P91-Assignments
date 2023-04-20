package ca.cosc3p91.a4.util.network;

import ca.cosc3p91.a4.game.GameEngine;
import ca.cosc3p91.a4.game.Map;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements Runnable {

    public static final int SERVER_PORT = 42069;
    public static final int PACKET_SIZE = 4096;
    public static final long MAX_PACKET_ACK_TIME_SECONDS = 30;

    private final HashMap<Long, ConnectedClient> clients = new HashMap<>();
    private long clientAssignmentID = 0;
    private final DatagramSocket socket;
    private final Thread ioThread;
    private long lastSentMessageID = 0;

    private GameEngine mainEngine;

    private volatile boolean running = true;

    public Server() throws SocketException {
        socket = new DatagramSocket(SERVER_PORT);
        ioThread = new Thread(this);
        ioThread.start();

        mainEngine = new GameEngine();
    }

    public void run(){
        while (running) {
            try {
                byte[] receiveData = new byte[PACKET_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                // BLOCKING!
                socket.receive(receivePacket);

                // read in the message header that is associated with every message.
                DataInputStream stream = new DataInputStream(new ByteArrayInputStream(receivePacket.getData()));

                byte packetID = stream.readByte();
                long clientID = stream.readLong();
                long messageID = stream.readLong();

                System.out.println("Receiving message with ID " + messageID + " from client: " + clientID + " of type " + packetID);

                ConnectedClient client = clients.get(clientID);

                // the server must handle connection requests while the client's processing thread will handle all other messages
                if (packetID == PacketTable.CONNECT){
                    long cid = ++clientAssignmentID;
                    System.out.println("A client has connected, his clientID is " + cid);
                    clients.put(cid, new ConnectedClient(socket, mainEngine, cid, messageID, receivePacket.getAddress(), receivePacket.getPort()));
                    continue;
                }
                if (client == null)
                    throw new ServerException("Client sent message invalid client id! (" + clientID + ")");
                if (packetID == PacketTable.DISCONNECT) {
                    client.halt();
                    clients.remove(clientID);
                    continue;
                }
                client.handleRequest(new Message.Received(packetID, clientID, messageID, stream, receivePacket.getData()));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void halt() throws InterruptedException {
        running = false;
        ioThread.join();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Server();
    }

    private static class ConnectedClient implements Runnable {
        private final InetAddress address;
        private final int port;
        private final Queue<Message.Received> pendingRequests = new PriorityQueue<>();
        private final ReentrantLock requestLock = new ReentrantLock();
        private final AtomicBoolean allowUpdate;
        private final java.util.Map<Long, Message.Sent> sentMessages = Collections.synchronizedMap(new HashMap<>());
        private final DatagramSocket serverSocket;
        private final long clientID;
        private volatile boolean running = true;
        private final Thread processingThread;
        private final Thread gameEngineThread;
        private final GameEngine usingEngine;
        private final Map clientMap;

        public ConnectedClient(DatagramSocket serverSocket, GameEngine engine, long clientID, long messageID, InetAddress address, int port){
            this.serverSocket = serverSocket;
            this.address = address;
            this.port = port;
            this.clientID = clientID;
            this.clientMap = engine.generateInitialMap();
            this.usingEngine = engine;
            this.allowUpdate = new AtomicBoolean(true);
            processingThread = new Thread(this);
            processingThread.start();
            gameEngineThread = new Thread(() -> {
                while (running) {
                    if (this.allowUpdate.get()) {
                        engine.updateMap(clientMap);
                    }
                }
            });
            gameEngineThread.start();

            sendMessage(new Message.Sent(PacketTable.ACK, clientID, messageID));
        }

        public void handleRequest(Message.Received request){
            if (request.getClientID() != this.clientID)
                throw new RuntimeException("Server sent us a message, yet we are not the intended recipient!");
            requestLock.lock();
            pendingRequests.add(request);
            requestLock.unlock();
        }

        private void processRequest(Message.Received request){
            try {
                switch (request.getPacketID()) {
                    case PacketTable.ACK:
                        Message.Sent message = sentMessages.get(request.getMessageID());
                        if (message == null)
                            throw new RuntimeException("A message was acknowledged but does not exist!");
                        message.acknowledged();
                        sentMessages.remove(request.getMessageID());
                        break;
                    case PacketTable.MESSAGE:
                        System.out.println(request.getReader().readUTF());
                        break;
                    case PacketTable.BUILD:
                        if (usingEngine.build(clientMap, request.getReader().readUTF())){
                            System.out.println("Client " + clientID + " has built something!");
                        } else {
                            System.out.println("Client " + clientID + " failed to build!");
                        }
                        break;
                    case PacketTable.TRAIN:
                        usingEngine.train(clientMap, request.getReader().readUTF());
                        break;
                    case PacketTable.UPGRADE:
                        usingEngine.upgradeBuilding(clientMap, Integer.parseInt(request.getReader().readUTF()));
                        break;
                }
                sendMessage(new Message.Sent(PacketTable.ACK, clientID, request.getMessageID()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void run(){
            while (running){
                // handle request processing without blocking the I/O thread
                requestLock.lock();
                if (!pendingRequests.isEmpty()) {
                    Message.Received request = pendingRequests.remove();
                    allowUpdate.set(false);
                    processRequest(request);
                    allowUpdate.set(true);
                }
                requestLock.unlock();

                ArrayList<Long> removes = new ArrayList<>();
                for (HashMap.Entry<Long, Message.Sent> message : sentMessages.entrySet()){
                    Message.Sent sent = message.getValue();
                    if (!sent.isAcknowledged() && sent.getTimeSinceSent().get() > MAX_PACKET_ACK_TIME_SECONDS) {
                        System.out.println("The client did not acknowledge our message, did they receive it?");
                        sendMessage(sent);
                        removes.add(message.getKey());
                    }
                }
                for (Long l : removes)
                    sentMessages.remove(l);
            }
        }

        public void sendMessage(Message.Sent message){
            if (message.getPacketID() != PacketTable.ACK)
                this.sentMessages.put(message.getMessageID(), message);
            byte[] data = message.getData().toByteArray();
            if (data.length > PACKET_SIZE)
                throw new RuntimeException("Unable to send packet as it exceeds maximum packet size!");
            DatagramPacket request = new DatagramPacket(data, data.length, address, port);
            try {
                System.out.println("Sending message with ID " + message.getMessageID() + " to client: " + message.getClientID() + " of type " + message.getPacketID());
                serverSocket.send(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void halt() throws InterruptedException {
            running = false;
            processingThread.join();
        }

    }

}
