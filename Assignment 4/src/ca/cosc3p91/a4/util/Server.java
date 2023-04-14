package ca.cosc3p91.a4.util;

import ca.cosc3p91.a4.game.GameEngine;

import java.io.*;
import java.net.*;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements Runnable {

    public static final int SERVER_PORT = 42069;
    public static final int PACKET_SIZE = 4096;

    private final HashMap<Long, ConnectedClient> clients = new HashMap<>();
    private long clientAssignmentID = 0;
    private long lastMessageID = 0;
    private final DatagramSocket socket;
    private final Thread ioThread;

    private GameEngine mainEngine;

    private volatile boolean running = true;

    public Server() throws SocketException {
        socket = new DatagramSocket(SERVER_PORT);
        ioThread = new Thread(this);
        ioThread.start();
    }

    public void run(){
        while (running) {
            byte[] receiveData = new byte[PACKET_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                // BLOCKING!
                socket.receive(receivePacket);

                DataInputStream stream = new DataInputStream(new ByteArrayInputStream(receivePacket.getData()));

                byte packetID = stream.readByte();
                long clientID = stream.readLong();

                ConnectedClient client = clients.get(clientID);

                if (packetID == PacketTable.CONNECT){
                    clients.put(++clientAssignmentID, new ConnectedClient(socket, clientID, receivePacket.getAddress(), receivePacket.getPort()));
                } else if (packetID == PacketTable.DISCONNECT){
                    if (client == null)
                        throw new ServerException("Client disconnected with invalid client id! (" + clientID + ")");
                    client.halt();
                    clients.put(clientID, null);
                } else {
                    if (client == null)
                        throw new ServerException("Client message with invalid client id! (" + clientID + ")");
                    client.handleRequest(new ConnectedClient.ServerRequest(packetID, stream));
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
        private final ArrayList<ServerRequest> requests = new ArrayList<>();
        // could use read/write lock for some of this, as certain operations, mostly timeout check, won't modify data.
        private final ReentrantLock requestLock = new ReentrantLock();
        private final DatagramSocket socket;
        private final long clientID;
        private volatile boolean running = true;
        private final Thread processingThread;

        public ConnectedClient(DatagramSocket socket, long clientID, InetAddress address, int port){
            this.socket = socket;
            this.address = address;
            this.port = port;
            this.clientID = clientID;
            processingThread = new Thread(this);
            processingThread.start();
        }

        public void handleRequest(ServerRequest request){
            requestLock.lock();
            requests.add(request);
            requestLock.unlock();
        }

        public void run(){
            while (running){
                requestLock.lock();
                requests.removeIf(ServerRequest::isAck);
                for (ServerRequest request : requests){
                    if (request.getTimeSinceReceived().)
                }
                requestLock.unlock();
            }
        }

        public void halt() throws InterruptedException {
            running = false;
            processingThread.join();
        }

        private static class ServerRequest {
            private final byte id;
            private final Time receiveTime;
            private final DataInputStream receive;
            private boolean ack = false;

            public ServerRequest(byte id, DataInputStream receive){
                this.id = id;
                this.receive = receive;
                receiveTime = Time.getTime();
            }

            public byte getID(){
                return id;
            }

            public void acknowledged(){
                this.ack = true;
            }

            public boolean isAck(){
                return this.ack;
            }

            public DataInputStream getDataStream(){
                return receive;
            }

            public Time getTimeSinceReceived(){
                return Time.getTime().difference(receiveTime);
            }
        }

    }

}
