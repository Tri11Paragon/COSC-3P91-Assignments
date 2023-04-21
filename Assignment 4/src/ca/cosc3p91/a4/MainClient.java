package ca.cosc3p91.a4;

import ca.cosc3p91.a4.util.network.Client;
import java.io.*;

public class MainClient {

    public static void main(String[] args) throws IOException {
        new Client("localhost");
    }

}
