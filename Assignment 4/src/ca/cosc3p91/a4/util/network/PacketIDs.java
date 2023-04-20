package ca.cosc3p91.a4.util.network;

public class PacketIDs {

    // packetID -> byte defined in this file
    // clientID -> long
    // messageID -> long

    // MESSAGE_HEADER (packetID, clientID, messageID)

    // MESSAGE_HEADER, (clientID = 0 if connecting to server)
    public static final byte CONNECT = 0x1;
    // MESSAGE_HEADER
    public static final byte DISCONNECT = 0x2;
    // MESSAGE_HEADER
    public static final byte ACK = 0x3;
    // MESSAGE_HEADER, UTF8 String with length information (use DOS.writeUTF/DIS.readUTF)
    public static final byte MESSAGE = 0x4;
    // MESSAGE_HEADER, build
    public static final byte BUILD = 0x5;
    // MESSAGE_HEADER, train
    public static final byte TRAIN = 0x6;
    // MESSAGE_HEADER, upgrade
    public static final byte UPGRADE = 0x7;
    // MESSAGE_HEADER
    public static final byte PRINT_MAP_DATA = 0x8; // client -> server only!
    // MESSAGE_HEADER, line count
    public static final byte BEGIN_MAP_DATA = 0x9; // server -> client
    // MESSAGE_HEADER, line number (int), UTF8 String (the line)
    public static final byte MAP_LINE_DATA = 0xA; // server -> client
    // MESSAGE_HEADER
    public static final byte EXPLORE = 0xB;
    // MESSAGE_HEADER
    public static final byte ATTACK = 0xC;
    // MESSAGE_HEADER
    public static final byte GENERATE = 0xD;
    // MESSAGE_HEADER
    public static final byte TEST_ARMY = 0xE;
    // MESSAGE_HEADER
    public static final byte TEST_VILLAGE = 0xF;
}
