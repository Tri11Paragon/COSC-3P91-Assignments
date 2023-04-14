package ca.cosc3p91.a4.util;

import ca.cosc3p91.a4.game.GameEngine;

import java.io.*;
import java.net.*;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Server implements Runnable {

    public static final int SERVER_PORT = 42069;
    public static final int PACKET_SIZE = 4096;

    private final HashMap<Long, ConnectedClient> clients = new HashMap<>();
    private long clientAssignmentID = 0;
    private long lastMessageID = 0;
    private DatagramSocket socket;
    private Thread ioThread;

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
                socket.receive(receivePacket);

                DataInputStream stream = new DataInputStream(new ByteArrayInputStream(receivePacket.getData()));

                byte packetID = stream.readByte();
                long clientID = stream.readLong();

                if (packetID == PacketTable.CONNECT){
                    clients.put(++clientAssignmentID, new ConnectedClient(socket, clientID receivePacket.getAddress(), receivePacket.getPort()));
                } else if (packetID == PacketTable.DISCONNECT){
                    clients.put(clientID, null);
                } else {
                    ConnectedClient client = clients.get(clientID);
                    if (client == null)
                        throw new ServerException("Client Connected with invalid client id! (" + clientID + ")");
                    client.handleRequest(new ConnectedClient.ServerRequest(packetID, stream));
                }
            } catch (IOException e) {
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
        byte[] receiveData = new byte[1284];
        byte[] sendData = new byte[1284];
        stream_in = new ByteArrayInputStream(receiveData);
        stream_out = new ByteArrayOutputStream(1284);
        new Thread(mainEngine = new GameEngine(stream_in,stream_out)).start();
        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            stream_in.reset();
            Thread.sleep(500);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            sendData = stream_out.toByteArray();
            stream_out.reset();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }

    private static class ConnectedClient {
        private final InetAddress address;
        private final int port;
        private final ArrayList<ServerRequest> requests = new ArrayList<>();
        private final DatagramSocket socket;
        private final long clientID;

        public ConnectedClient(DatagramSocket socket, long clientID, InetAddress address, int port){
            this.socket = socket;
            this.address = address;
            this.port = port;
            this.clientID = clientID;
        }

        public void handleRequest(ServerRequest request){

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
