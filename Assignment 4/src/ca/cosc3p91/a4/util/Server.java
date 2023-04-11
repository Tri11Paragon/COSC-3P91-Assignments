package ca.cosc3p91.a4.util;

import ca.cosc3p91.a4.game.GameEngine;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Server {

    public static final int SERVER_PORT = 42069;

    public static ByteArrayInputStream stream_in;


    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
        byte[] receiveData = new byte[1284];
        receiveData[0] = (byte)'0';
        stream_in = new ByteArrayInputStream(receiveData);
        byte[] sendData = new byte[1024];
        new Thread(new GameEngine(stream_in)).start();
        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            stream_in.reset();
            /*
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket); */
        }
    }

}
