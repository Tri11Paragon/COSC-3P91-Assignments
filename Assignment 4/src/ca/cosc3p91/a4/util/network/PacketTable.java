package ca.cosc3p91.a4.util.network;

public class PacketTable {

    // packetID -> byte defined in this file
    // clientID -> long
    // messageID -> long

    // messageHeader (packetID, clientID, messageID)

    // messageHeader, (clientID = 0 if connecting to server)
    public static final byte CONNECT = 0x1;
    // messageHeader
    public static final byte DISCONNECT = 0x2;
    // messageHeader
    public static final byte ACK = 0x3;
    // messageHeader, UTF8 String with length information (use DOS.writeUTF/DIS.readUTF)
    public static final byte MESSAGE = 0x4;
    // messageHeader, serial packets with map info
    public static final byte MAP_DATA = 0x5;

}
