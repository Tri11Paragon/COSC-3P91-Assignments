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
    private String[] lineBuffer = new String[0];
    private int expectedLines = 0;
    private int currentLines = 0;

    private long ourClientID = 0;

    public Client(String address) throws IOException {
        serverAddress = InetAddress.getByName(address);
        clientSocket = new DatagramSocket();
        receiveThread = new Thread(this);
        receiveThread.start();

        sendMessage(new Message.Sent(PacketIDs.CONNECT, ourClientID, ++lastMessageID));

        view.printGameMenu();

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
                        messageType = PacketIDs.BUILD;
                        break;
                    case '2':
                        messageType = PacketIDs.TRAIN;
                        break;
                    case '3':
                        messageType = PacketIDs.UPGRADE;
                        break;
                    case '4':
                        messageType = PacketIDs.EXPLORE;
                        break;
                    case '5':
                        messageType = PacketIDs.PRINT_MAP_DATA;
                        break;
                    case '7':
                        messageType = PacketIDs.ATTACK;
                        break;
                    case '8':
                        messageType = PacketIDs.GENERATE;
                        break;
                    case '9':
                        messageType = PacketIDs.TEST_ARMY;
                        break;
                    case '0':
                        messageType = PacketIDs.TEST_VILLAGE;
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
                    case PacketIDs.ACK:
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
                    case PacketIDs.MESSAGE:
                        System.out.println("\033[93m" + stream.readUTF() + "\033[0m");
                        break;
                    case PacketIDs.BEGIN_MAP_DATA:
                        expectedLines = stream.readInt();
                        currentLines = 0;
                        lineBuffer = new String[expectedLines];
                        break;
                    case PacketIDs.MAP_LINE_DATA:
                        int lineNumber = stream.readInt();
                        lineBuffer[lineNumber] = stream.readUTF();
                        currentLines++;
                        if (currentLines >= expectedLines) {
                            for (String line : lineBuffer){
                                System.out.println("\033[92m" + line + "\033[0m");
                            }
                        }
                        break;
                    case PacketIDs.DISCONNECT:
                        System.out.println("Disconnecting!");
                        running = false;
                        break;
                }
                if (packetID != PacketIDs.ACK && packetID != PacketIDs.DISCONNECT){
                    sendMessage(new Message.Sent(PacketIDs.ACK, ourClientID, messageID));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(Message.Sent message){
        if (message.getPacketID() != PacketIDs.ACK)
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
