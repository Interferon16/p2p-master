package com.simple_p2p.p2p_engine.server;

import com.simple_p2p.entity.FileNode;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.internal.ConcurrentSet;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Settings {

    public static class SettingsHolder {
        public static final Settings HOLDER_INSTANCE = new Settings();
    }

    public static Settings getInstance() {
        return SettingsHolder.HOLDER_INSTANCE;
    }

    private Settings() {}

    private String myHash;
    private InetAddress localAddress;
    private String localMacAddress;
    private InetAddress externalAddress;
    private int listener_port = 16161;
    private ChannelGroup connectedChannelGroup;
    private CopyOnWriteArrayList<Integer> messagesHashBuffer;
    private ApplicationContext sprAppCtx;
    private ExecutorService treadsPoll = Executors.newFixedThreadPool(10);
    private ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles = new ConcurrentHashMap<>();
    private SessionFactory sessionFactory;
    private ConcurrentSet<String> online_users = new ConcurrentSet<>();
    private int numberConnectionsToMaintain = 10;
    private int standardFileChunkLength = 128*1024;
    private String signalServerAddress = "127.0.0.1";
    private int signalServerPort = 8071;

    public int getListener_port() {
        return listener_port;
    }

    public void setListener_port(int listener_port) {
        this.listener_port = listener_port;
    }

    public ChannelGroup getConnectedChannelGroup() {
        return connectedChannelGroup;
    }

    public void setConnectedChannelGroup(ChannelGroup connectedChannelGroup) {
        this.connectedChannelGroup = connectedChannelGroup;
    }

    public CopyOnWriteArrayList<Integer> getMessagesHashBuffer() {
        return messagesHashBuffer;
    }

    public void setMessagesHashBuffer(CopyOnWriteArrayList<Integer> messagesHashBuffer) {
        this.messagesHashBuffer = messagesHashBuffer;
    }

    public String getMyHash() {
        return myHash;
    }

    public void setMyHash(String myHash) {
        this.myHash = myHash;
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
    }

    public String getLocalMacAddress() {
        return localMacAddress;
    }

    public void setLocalMacAddress(String localMacAddress) {
        this.localMacAddress = localMacAddress;
    }

    public InetAddress getExternalAddress() {
        return externalAddress;
    }

    public void setExternalAddress(InetAddress externalAddress) {
        this.externalAddress = externalAddress;
    }

    public ApplicationContext getSprAppCtx() {
        return sprAppCtx;
    }

    public void setSprAppCtx(ApplicationContext sprAppCtx) {
        this.sprAppCtx = sprAppCtx;
    }

    public ExecutorService getTreadsPoll() {
        return treadsPoll;
    }

    public ConcurrentHashMap<String, FileNode> getInMemoryListOfSharedFiles() {
        return inMemoryListOfSharedFiles;
    }

    public void setInMemoryListOfSharedFiles(ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles) {
        this.inMemoryListOfSharedFiles = inMemoryListOfSharedFiles;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ConcurrentSet<String> getOnline_users() {
        return online_users;
    }

    public int getNumberConnectionsToMaintain() {
        return numberConnectionsToMaintain;
    }

    public void setNumberConnectionsToMaintain(int numberConnectionsToMaintain) {
        this.numberConnectionsToMaintain = numberConnectionsToMaintain;
    }

    public int getStandardFileChunkLength() {
        return standardFileChunkLength;
    }

    public void setStandardFileChunkLength(int standardFileChunkLength) {
        this.standardFileChunkLength = standardFileChunkLength;
    }

    public String getSignalServerAddress() {
        return signalServerAddress;
    }

    public void setSignalServerAddress(String signalServerAddress) {
        this.signalServerAddress = signalServerAddress;
    }

    public int getSignalServerPort() {
        return signalServerPort;
    }

    public void setSignalServerPort(int signalServerPort) {
        this.signalServerPort = signalServerPort;
    }
}
