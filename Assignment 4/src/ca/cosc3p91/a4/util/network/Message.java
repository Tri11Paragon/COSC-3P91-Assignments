package ca.cosc3p91.a4.util.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Message {

    private final byte packetID;
    private final long clientID, messageID;

    public Message(byte packetID, long clientID, long messageID){
        this.packetID = packetID;
        this.clientID = clientID;
        this.messageID = messageID;
    }

    public byte getPacketID() {
        return packetID;
    }

    public long getClientID() {
        return clientID;
    }

    public long getMessageID() {
        return messageID;
    }

    public static class ReceivedMessage extends Message {

        private final DataInputStream reader;

        public ReceivedMessage(byte packetID, long clientID, long messageID, DataInputStream reader) {
            super(packetID, clientID, messageID);
            this.reader = reader;
        }

        public DataInputStream getReader(){
            return reader;
        }
    }

    public static class SentMessage extends Message {

        private final DataOutputStream writer;

        public SentMessage(byte packetID, long clientID, long messageID, DataOutputStream writer) {
            super(packetID, clientID, messageID);
            this.writer = writer;
        }

        public DataOutputStream getWriter(){
            return writer;
        }
    }

}
