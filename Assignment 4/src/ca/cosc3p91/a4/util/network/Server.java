package ca.cosc3p91.a4.util.network;

import ca.cosc3p91.a4.game.GameEngine;

import java.io.*;
import java.net.*;
import java.rmi.ServerException;
import java.util.*;
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

                ConnectedClient client = clients.get(clientID);

                // the server must handle connection requests while the client's processing thread will handle all other messages
                if (packetID == PacketTable.CONNECT){
                    clients.put(++clientAssignmentID, new ConnectedClient(socket, clientID, messageID, receivePacket.getAddress(), receivePacket.getPort()));
                } else if (packetID == PacketTable.DISCONNECT) {
                    if (client == null)
                        throw new ServerException("Client disconnected with invalid client id! (" + clientID + ")");
                    client.halt();
                    clients.put(clientID, null);
                } else {
                    if (client == null)
                        throw new ServerException("Client message with invalid client id! (" + clientID + ")");
                    client.handleRequest(new Message.Received(packetID, clientID, messageID, stream, receivePacket.getData()));
                }
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
        private final HashMap<Long, Message.Sent> sentMessages = new HashMap<>();
        private final DatagramSocket serverSocket;
        private final long clientID;
        private volatile boolean running = true;
        private final Thread processingThread;

        public ConnectedClient(DatagramSocket serverSocket, long clientID, long messageID, InetAddress address, int port){
            this.serverSocket = serverSocket;
            this.address = address;
            this.port = port;
            this.clientID = clientID;
            processingThread = new Thread(this);
            processingThread.start();

            ByteArrayOutputStream bstream = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(bstream);
            sendMessage(new Message.Sent(PacketTable.ACK, clientID, messageID, stream, bstream));
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
                switch (request.getPacketID()){
                    case PacketTable.ACK:
                        Message.Sent message = sentMessages.get(request.getMessageID());
                        if (message == null)
                            throw new RuntimeException("A message was acknowledged but does not exist!");
                        message.acknowledged();
                        break;
                    case PacketTable.MESSAGE:
                        System.out.println(request.getReader().readUTF());
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void run(){
            while (running){
                // handle request processing without blocking the I/O thread
                requestLock.lock();
                Message.Received request = pendingRequests.remove();
                processRequest(request);
                requestLock.unlock();

                for (Map.Entry<Long, Message.Sent> message : sentMessages.entrySet()){
                    if (message.getValue().getTimeSinceSent().get() > MAX_PACKET_ACK_TIME_SECONDS) {
                        System.out.println("The server did not process our message, did they receive it?");
                        // todo: resend message
                    }
                }
            }
        }

        public void sendMessage(Message.Sent message){
            this.sentMessages.put(message.getMessageID(), message);
            byte[] data = message.getData().toByteArray();
            if (data.length > PACKET_SIZE)
                throw new RuntimeException("Unable to send packet as it exceeds maximum packet size!");
            DatagramPacket request = new DatagramPacket(data, data.length, address, port);
            try {
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
