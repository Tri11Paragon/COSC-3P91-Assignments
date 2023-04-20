package ca.cosc3p91.a4.util.network;

import ca.cosc3p91.a4.userinterface.GameDisplay;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Client implements Runnable {
    private GameDisplay view = new GameDisplay();

    private DatagramSocket clientSocket;
    private volatile boolean running = true;
    private Thread receiveThread;
    private final Map<Long, Message.Sent> sentMessages = Collections.synchronizedMap(new HashMap<>());
    private int lastMessageID = 0;
    private final InetAddress serverAddress;

    private long ourClientID = 0;

    public Client(String address) throws IOException {
        serverAddress = InetAddress.getByName(address);
        clientSocket = new DatagramSocket();
        receiveThread = new Thread(this);
        receiveThread.start();

        sendMessage(new Message.Sent(PacketTable.CONNECT, ourClientID, ++lastMessageID));

        while (running) {
            String prompt;
            if ((prompt = view.nextInput()) != null) {
                if (prompt.trim().isEmpty())
                    continue;
                if (prompt.charAt(0) == '6') {
                    running = false;
                    break;
                }
                view.printGameMenu();
                String[] args = prompt.split(" ");
                char c = prompt.charAt(0);
                if (c > '0' && c < '4') {
                    if (args.length < 2) {
                        System.err.println("Args must include type!");
                        continue;
                    }
                }
                byte messageType;
                switch (c) {
                    case '1':
                        messageType = PacketTable.BUILD;
                        break;
                    case '2':
                        messageType = PacketTable.TRAIN;
                        break;
                    case '3':
                        messageType = PacketTable.UPGRADE;
                        break;
                    case '5':
                        messageType = PacketTable.PRINT_MAP_DATA;
                        break;
                    default:
                        System.err.println("> Invalid command input!");
                        return;
                }
                Message.Sent buildMessage = new Message.Sent(messageType,ourClientID,++lastMessageID);
                buildMessage.getWriter().writeUTF(prompt.substring(1));
                sendMessage(buildMessage);
            }
            ArrayList<Long> removes = new ArrayList<>();
            for (HashMap.Entry<Long, Message.Sent> message : sentMessages.entrySet()){
                Message.Sent sent = message.getValue();
                if (!sent.isAcknowledged() && sent.getTimeSinceSent().get() > Server.MAX_PACKET_ACK_TIME_SECONDS) {
                    System.out.println("The server did not acknowledge our message, did they receive it?");
                    sendMessage(sent);
                    removes.add(message.getKey());
                }
            }
            for (Long l : removes)
                sentMessages.remove(l);
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

                System.out.println("Receiving message with ID " + messageID + " from server of type " + packetID + " our ClientID " + clientID + " / " + ourClientID);

                switch (packetID) {
                    case PacketTable.ACK:
                        if (ourClientID == 0)
                            ourClientID = clientID;
                        Message.Sent message = sentMessages.get(messageID);
                        if (message == null)
                            throw new RuntimeException("Server acknowledged a message we never sent! (" + messageID + ")");
                        message.acknowledged();
                        sentMessages.remove(messageID);
                        System.out.println("Message acknowledged with ID: " + messageID);
                        System.out.println("Messages left: " + sentMessages.size());
                        for (HashMap.Entry<Long, Message.Sent> ms : sentMessages.entrySet())
                            System.out.println("MessageID: " + ms.getKey());
                        break;
                    case PacketTable.MESSAGE:
                        System.out.println(stream.readUTF());
                        break;
                    case PacketTable.DISCONNECT:
                        running = false;
                        break;
                }
                if (packetID != PacketTable.ACK && packetID != PacketTable.DISCONNECT){
                    sendMessage(new Message.Sent(PacketTable.ACK, ourClientID, messageID));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(Message.Sent message){
        if (message.getPacketID() != PacketTable.ACK)
            sentMessages.put(message.getMessageID(), message);
        byte[] data = message.getData().toByteArray();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, Server.SERVER_PORT);
        try {
            System.out.println("Sending message with ID " + message.getMessageID() + " to server from: " + message.getClientID() + " of type " + message.getPacketID());
            clientSocket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
