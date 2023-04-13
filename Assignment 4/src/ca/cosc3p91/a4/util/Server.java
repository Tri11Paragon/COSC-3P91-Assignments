package ca.cosc3p91.a4.util;

import ca.cosc3p91.a4.game.GameEngine;

import java.io.*;
import java.net.*;

public class Server {

    public static final int SERVER_PORT = 42069;

    public static ByteArrayInputStream stream_in;
    public static ByteArrayOutputStream stream_out;
    public static GameEngine mainEngine;

    public static void main(String[] args) throws IOException, InterruptedException {
        DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
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

}
