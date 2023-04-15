package ca.cosc3p91.a4.util.network;

import ca.cosc3p91.a4.userinterface.GameDisplay;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client implements Runnable {
    private GameDisplay view = new GameDisplay();

    private DatagramSocket clientSocket;
    private boolean running = true;
    private Thread receiveThread;

    public Client(String address) throws IOException {
        InetAddress serverAddress = InetAddress.getByName(address);
        clientSocket = new DatagramSocket();
        receiveThread = new Thread(this);
        receiveThread.start();

        String prompt;
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        while (running) {
            if ((prompt = view.nextInput()) != null) {
                if (!prompt.isEmpty() && prompt.charAt(0) == '6') break;
                sendData = prompt.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String serverOutput = new String(receivePacket.getData()).trim();
                System.out.println(">" + serverOutput);
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



            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
