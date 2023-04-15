package ca.cosc3p91.a4.util.network;

import ca.cosc3p91.a4.util.Time;

import java.io.ByteArrayOutputStream;
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

    public static class Received extends Message {

        private final DataInputStream reader;
        private final byte[] data;

        public Received(byte packetID, long clientID, long messageID, DataInputStream reader, byte[] data) {
            super(packetID, clientID, messageID);
            this.reader = reader;
            this.data = data;
        }

        public DataInputStream getReader(){
            return reader;
        }

        public byte[] getData(){
            return data;
        }
    }

    public static class Sent extends Message {

        private final DataOutputStream writer;
        private final ByteArrayOutputStream data;
        private boolean ack = false;
        private final Time timeSent;

        /**
         * A message packet which will be sent to a client or the server, contains the standard message header and
         * writes the header to the stream, make sure you don't write into the stream before constructing this!
         *
         * @param packetID type of this message
         * @param clientID the client id, if this is going to the client it is unlikely to be used but should always be correct!
         * @param messageID client specific message id, used to reference/acknowledge messages
         * @param writer stream to write to
         * @param data byte array stream which contains the byte[] used in packet construction
         */
        public Sent(byte packetID, long clientID, long messageID) {
            super(packetID, clientID, messageID);
            this.data = new ByteArrayOutputStream();
            this.writer = new DataOutputStream(this.data);
            timeSent = Time.getTime();
            // write the header to the stream, make sure you don't write into the stream before constructing this!
            try {
                writer.writeByte(packetID);
                writer.writeLong(clientID);
                writer.writeLong(messageID);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        public void acknowledged(){
            this.ack = true;
        }

        public boolean isAcknowledged(){
            return ack;
        }

        public DataOutputStream getWriter(){
            return writer;
        }

        public Time getTimeSinceSent(){
            return Time.getTime().difference(timeSent);
        }

        public ByteArrayOutputStream getData(){
            return data;
        }
    }

}
