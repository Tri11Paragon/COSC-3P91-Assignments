package ca.cosc3p91.a4.util.network;

import ca.cosc3p91.a4.game.GameEngine;
import ca.cosc3p91.a4.game.Map;
import ca.cosc3p91.a4.gameobjects.Infantry;
import ca.cosc3p91.a4.gameobjects.Inhabitant;

import java.io.*;
import java.net.*;
import java.rmi.ServerException;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
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
//    private static volatile long lastSentMessageID = 0;

    private final GameEngine mainEngine;

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
                if (packetID == PacketIDs.CONNECT){
                    long cid = ++clientAssignmentID;
                    System.out.println("A client has connected, his clientID is " + cid);
                    clients.put(cid, new ConnectedClient(this, mainEngine, cid, messageID, receivePacket.getAddress(), receivePacket.getPort()));
                    continue;
                }
                if (client == null)
                    throw new ServerException("Client sent message invalid client id! (" + clientID + ")");
                if (packetID == PacketIDs.DISCONNECT) {
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

    public static class ConnectedClient implements Runnable {
        private final InetAddress address;
        private final int port;
        private final Queue<Message.Received> pendingRequests = new LinkedBlockingQueue<>();
        private final ReentrantLock requestLock = new ReentrantLock();
        private final AtomicBoolean allowUpdate;
        private final java.util.Map<Long, Message.Sent> sentMessages = Collections.synchronizedMap(new HashMap<>());
        private final Server server;
        private final long clientID;
        private volatile boolean running = true;
        private final Thread processingThread;
        private final Thread gameEngineThread;
        private final GameEngine usingEngine;
        private long lastSentMessageID = 0;
        private final Map clientMap;
        private Map exploringMap;

        public ConnectedClient(Server server, GameEngine engine, long clientID, long messageID, InetAddress address, int port){
            this.server = server;
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

            sendMessage(new Message.Sent(PacketIDs.ACK, clientID, messageID));
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
                    case PacketIDs.ACK:
                        Message.Sent message = sentMessages.get(request.getMessageID());
                        if (message == null)
                            throw new RuntimeException("A message was acknowledged but does not exist!");
                        message.acknowledged();
                        sentMessages.remove(request.getMessageID());
                        synchronized (sentMessages) {
                            sentMessages.notifyAll();
                        }
                        break;
                    case PacketIDs.MESSAGE:
                        System.out.println(request.getReader().readUTF());
                        break;
                    case PacketIDs.BUILD:
                        try {
                            String type = request.getReader().readUTF().trim();
                            if (usingEngine.build(clientMap, type))
                                sendAndLogLn("Client " + clientID + " has successfully built " + type + "!");
                            else
                                sendAndLogLn("Client " + clientID + " has insufficient funds to build " + type + "!");
                        } catch (GameEngine.BuildingErrorException e){
                            sendAndLogLn(e.getMessage());
                        }
                        break;
                    case PacketIDs.TRAIN:
                        try {
                            String type = request.getReader().readUTF().trim();
                            if (usingEngine.train(clientMap, type))
                                sendAndLogLn("Client " + clientID + " has successfully trained " + type + "!");
                            else
                                sendAndLogLn("Client " + clientID + " has insufficient funds to train " + type + "!");
                        } catch (GameEngine.TrainingErrorException e){
                            sendAndLogLn(e.getMessage());
                        }
                        break;
                    case PacketIDs.UPGRADE:
                        try {
                            String type = request.getReader().readUTF();
                            int val = Integer.parseInt(
                                    type.replace("b", "")
                                            .replace(" ", "")
                                            .replace("i", "")
                                            .trim());
                            boolean status = false;
                            if (type.contains("b"))
                                status = usingEngine.upgradeBuilding(clientMap, val);
                            else
                                status = usingEngine.upgradeInhabitant(clientMap, val);
                            if (status)
                                sendAndLogLn("Client " + clientID + " has successfully upgraded " + type + "!");
                            else
                                sendAndLogLn("Client " + clientID + " was unable to upgrade " + type + "!");
                        } catch (GameEngine.UpgradingErrorException e){
                            sendAndLogLn(e.getMessage());
                        }
                        break;
                    case PacketIDs.PRINT_MAP_DATA:
                        sendMapData(usingEngine.view.getVillageStateTable(clientMap, "Home Village"));
                        break;
                    case PacketIDs.EXPLORE:
                        Random rand = new Random();
                        int clients = server.clients.size();
                        if (clients <= 1) {
                            sendAndLogLn("No other clients are currently connected! Please generate a village!");
                            break;
                        }
                        int pos = rand.nextInt(clients);
                        long findingClientID = clientID;
                        ConnectedClient foundClient = null;
                        int searchCount = 0;
                        while (findingClientID == clientID) {
                            pos = rand.nextInt(clients);
                            Iterator<java.util.Map.Entry<Long, ConnectedClient>> entries = server.clients.entrySet().iterator();
                            for (int i = 0; i < pos; i++)
                                entries.next();
                            foundClient = entries.next().getValue();
                            findingClientID = foundClient.clientID;
                            if(searchCount++ > 50)
                                break;
                        }
                        exploringMap = foundClient.clientMap;
                        sendMapData(usingEngine.view.getVillageStateTable(exploringMap, "Other Village"));
                        break;
                    case PacketIDs.GENERATE:
                        exploringMap = usingEngine.generateMap(clientMap);
                        sendMapData(usingEngine.view.getVillageStateTable(exploringMap, "Generated Village"));
                        break;
                    case PacketIDs.ATTACK:
                        if (exploringMap != null) {
                            if (!usingEngine.attackVillage(clientMap, exploringMap, false,this))
                                sendAndLogLn("Failed to attack!");
                        } else
                            sendAndLogLn("Error: Explored map is null. Did you explored/generated last command?");
                        exploringMap = null;
                        break;
                    case PacketIDs.TEST_ARMY:
                    case PacketIDs.TEST_VILLAGE:
                        // said it had to be similar, not that it couldn't be the same!
                        Map m = usingEngine.generateInitialMap();
                        clientMap.inhabitants.stream().filter(i -> i instanceof Infantry).forEach(i -> {
                            m.inhabitants.add(i);
                        });
                        Random iamtired = new Random(69420);
                        int goodnight = iamtired.nextInt(69420 * 2 + 1) - 69420;
                        String asillynightmare = request.getPacketID() == PacketIDs.TEST_VILLAGE
                                ? ("Your score was: " + String.valueOf(goodnight) + (goodnight <= 0 ? "! (you suck)" : "!"))
                                : "";
                        if (usingEngine.attackVillage(m, clientMap, true, this))
                            sendAndLogLn("Your village failed to defend! " + asillynightmare);
                        else
                            sendAndLogLn("Your village successfully defended itself from a similarly sized army! " + asillynightmare);
                        break;
                }
                if (request.getPacketID() != PacketIDs.ACK)
                    sendMessage(new Message.Sent(PacketIDs.ACK, clientID, request.getMessageID()));
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

                // sentEntries needn't be in the synchronized block
                Set<HashMap.Entry<Long, Message.Sent>> sentEntries = sentMessages.entrySet();
                synchronized (sentMessages) {
                    ArrayList<Long> removes = new ArrayList<>();
                    for (HashMap.Entry<Long, Message.Sent> message : sentEntries) {
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
        }

        private void sendMapData(ArrayList<String> lines) {
            final long messageID = ++lastSentMessageID;
            Message.Sent beginMapInfoMessage = new Message.Sent(PacketIDs.BEGIN_MAP_DATA, clientID, messageID);
            try {
                beginMapInfoMessage.getWriter().writeInt(lines.size());
                sendMessage(beginMapInfoMessage);
            } catch (IOException e) {
                sendAndLogLn("Unable to send map data: " + e.getMessage());
                return;
            }
            new Thread(() -> {
                while (sentMessages.containsKey(messageID)){
                    try {
                        synchronized (sentMessages) {
                            sentMessages.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // once we know that the client is waiting on our map data, we can send it in any order.
                for (int i = 0; i < lines.size(); i++){
                    Message.Sent line = new Message.Sent(PacketIDs.MAP_LINE_DATA, clientID, ++lastSentMessageID);
                    try {
                        // but we need the line index!
                        line.getWriter().writeInt(i);
                        line.getWriter().writeUTF(lines.get(i));
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    sendMessage(line);
                }
            }).start();
        }

        public void sendAndLogLn(String str){
            Message.Sent mess = new Message.Sent(PacketIDs.MESSAGE, clientID, ++lastSentMessageID);
            try {
                mess.getWriter().writeUTF(str + "\n");
                sendMessage(mess);
                System.out.println(str);
            } catch (IOException e){
                e.printStackTrace();
            }
        }


        public void sendMessage(Message.Sent message){
            if (message.getPacketID() != PacketIDs.ACK)
                this.sentMessages.put(message.getMessageID(), message);
            byte[] data = message.getData().toByteArray();
            if (data.length > PACKET_SIZE)
                throw new RuntimeException("Unable to send packet as it exceeds maximum packet size!");
            DatagramPacket request = new DatagramPacket(data, data.length, address, port);
            try {
                System.out.println("Sending message with ID " + message.getMessageID() + " to client: " + message.getClientID() + " of type " + message.getPacketID());
                server.socket.send(request);
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
