package ca.cosc3p91.a4.util.network;

import ca.cosc3p91.a4.userinterface.GameDisplay;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Client implements Runnable {
    private GameDisplay view = new GameDisplay();

    private DatagramSocket clientSocket;
    private boolean running = true;
    private Thread receiveThread;
    private final HashMap<Long, Message.Sent> sentMessages = new HashMap<>();
    private int lastMessageID = 0;
    private final InetAddress serverAddress;

    public Client(String address) throws IOException {
        serverAddress = InetAddress.getByName(address);
        clientSocket = new DatagramSocket();
        receiveThread = new Thread(this);
        receiveThread.start();

        sendMessage(new Message.Sent(PacketTable.CONNECT, 0, ++lastMessageID));

        String prompt;
        while (running) {
            if ((prompt = view.nextInput()) != null) {
                if (prompt.trim().isEmpty())
                    continue;
                if (prompt.charAt(0) == '6')
                    break;


                view.printGameMenu();
            }
        }
        clientSocket.close();
    }

    public void run(){
        while (running){
            try {
                byte[] receiveData = new byte[Server.PACKET_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                DataInputStream stream = new DataInputStream(new ByteArrayInputStream(receivePacket.getData()));

                byte packetID = stream.readByte();
                long clientID = stream.readLong();
                long messageID = stream.readLong();

                switch (packetID) {
                    case PacketTable.ACK:
                        Message.Sent message = sentMessages.get(messageID);
                        if (message == null)
                            throw new RuntimeException("Server message sync error!");
                        message.acknowledged();
                        sentMessages.remove(messageID);
                        System.out.println("Message acknowledged " + messageID);
                        break;
                    case PacketTable.DISCONNECT:
                        running = false;
                        break;
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(Message.Sent message){
        sentMessages.put(message.getMessageID(), message);
        byte[] data = message.getData().toByteArray();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, Server.SERVER_PORT);
        try {
            clientSocket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
