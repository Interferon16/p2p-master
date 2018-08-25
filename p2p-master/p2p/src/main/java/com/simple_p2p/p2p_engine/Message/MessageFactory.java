package com.simple_p2p.p2p_engine.Message;

public class MessageFactory {

    public static Message createTextMessageInstance() {
        Message message = new Message();
        message.setType(MessageType.TEXT);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static Message createDataPacketInstance() {
        Message message = new Message();
        message.setType(MessageType.DATA);
        message.setDataBuffCapacity(128 * 1024);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static Message createHandshakeInstance() {
        Message message = new Message();
        message.setType(MessageType.HANDSHAKE);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static Message createPingInstance() {
        Message message = new Message();
        message.setType(MessageType.PING);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static Message createBootstrapInstance() {
        Message message = new Message();
        message.setType(MessageType.BOOTSTRAP);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static Message createGetFileListInstance() {
        Message message = new Message();
        message.setType(MessageType.GET_FILE_LIST);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static Message createListOfFilesInstance() {
        Message message = new Message();
        message.setType(MessageType.LIST_OF_FILES);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static Message createFileChunkRequestInstance() {
        Message message = new Message();
        message.setType(MessageType.GET_FILE_PART);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }

    public static Message createAnswerToRequestInstance() {
        Message message = new Message();
        message.setType(MessageType.ANSWER);
        message.setTimeStamp(System.currentTimeMillis());
        return message;
    }
}
