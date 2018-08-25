package com.simple_p2p.p2p_engine.Message;

import com.simple_p2p.entity.FileNode;
import com.simple_p2p.entity.UserConnection;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private MessageType type;
    private String from;
    private String to;
    private String message;
    private byte[] dataBuff;
    private String dataHash;
    private int chunkNumber;
    private boolean answerToRequest;
    private List<UserConnection> userConnections;
    private int dataBuffCapacity;
    private int hash;
    private long timeStamp;
    private ConcurrentHashMap<String, FileNode> ListOfSharedFiles;

    public Message() {

    }

    public void setDataBuffCapacity(int data_size) {
        this.dataBuff = new byte[data_size];
        this.dataBuffCapacity = data_size;
    }

    public int getDataBuffCapacity() {
        return dataBuffCapacity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public byte[] getDataBuff() {
        return dataBuff;
    }

    public void setDataBuff(byte[] dataBuff) {
        this.dataBuff = dataBuff;
    }

    public int getDataBuffLength() {
        return dataBuff.length;
    }

    public int getHash() {
        return hash;
    }

    public void createHash(int hash) {
        this.hash = this.hashCode();
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public List<UserConnection> getUserConnections() {
        return userConnections;
    }

    public void setUserConnections(List<UserConnection> userConnections) {
        this.userConnections = userConnections;
    }

    public ConcurrentHashMap<String, FileNode> getListOfSharedFiles() {
        return ListOfSharedFiles;
    }

    public void setListOfSharedFiles(ConcurrentHashMap<String, FileNode> listOfSharedFiles) {
        ListOfSharedFiles = listOfSharedFiles;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public int getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(int chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public boolean isAnswerToRequest() {
        return answerToRequest;
    }

    public void setAnswerToRequest(boolean answerToRequest) {
        this.answerToRequest = answerToRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return dataBuffCapacity == message1.dataBuffCapacity &&
                hash == message1.hash &&
                timeStamp == message1.timeStamp &&
                type == message1.type &&
                Objects.equals(from, message1.from) &&
                Objects.equals(to, message1.to) &&
                Objects.equals(message, message1.message) &&
                Arrays.equals(dataBuff, message1.dataBuff) &&
                Objects.equals(userConnections, message1.userConnections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, from, to, message, dataBuff, userConnections, dataBuffCapacity, hash, timeStamp);
    }
}
