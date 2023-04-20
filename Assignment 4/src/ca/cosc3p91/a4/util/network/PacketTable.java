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
    // messageHeader, build
    public static final byte BUILD = 0x5;
    // messageHeader, train
    public static final byte TRAIN = 0x6;
    // messageHeader, upgrade
    public static final byte UPGRADE = 0x7;
    // messageHeader
    public static final byte PRINT_MAP_DATA = 0x8; // client -> server only!
    // messageHeader, line count
    public static final byte BEGIN_MAP_DATA = 0x9; // server -> client
    // messageHeader, line number (int), UTF8 String (the line)
    public static final byte MAP_LINE_DATA = 0xA; // server -> client
}
