package ca.cosc3p91.a4.util;

public class PacketTable {

    // packetID -> byte defined in this file
    // clientID -> long
    // messageID -> long

    // packetID, clientID (0 if connecting to server)
    public static final byte CONNECT = 0x1;
    // packetID, clientID
    public static final byte DISCONNECT = 0x2;
    // packetID, clientID, messageID
    public static final byte ACK = 0x3;

}
