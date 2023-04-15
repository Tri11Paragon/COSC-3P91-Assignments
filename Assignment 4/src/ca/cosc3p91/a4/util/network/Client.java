package ca.cosc3p91.a4.util.network;

import ca.cosc3p91.a4.userinterface.GameDisplay;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    GameDisplay view = new GameDisplay();

    public Client(int port) throws IOException {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        String prompt;
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        while (true) {
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
}
