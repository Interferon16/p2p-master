package com.simple_p2p.p2p_engine.p2pcontrol.impl;

import com.simple_p2p.model.ChatMessage;
import com.simple_p2p.p2p_engine.Message.Message;
import com.simple_p2p.p2p_engine.Message.MessageFactory;
import com.simple_p2p.p2p_engine.client.Client;
import com.simple_p2p.entity.FileNode;
import com.simple_p2p.p2p_engine.fiile_work.FileSharer;
import com.simple_p2p.p2p_engine.p2pcontrol.interfaces.P2PServerControl;
import com.simple_p2p.p2p_engine.server.Server;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;


public class P2PServerControlImpl implements P2PServerControl {
    private Server server;
    private Client client;
    private Settings settings;
    private ChannelGroup connectedChannelsGroup;
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public P2PServerControlImpl(Server server, Settings settings) {
        this.settings = settings;
        this.server = server;
        this.client = server.getClient();
        this.connectedChannelsGroup = settings.getConnectedChannelGroup();

     }

    public Channel connectTo(String ipAddress) {
        int port = settings.getListener_port();
        Channel channel = null;

        for (int i = 0; i < 5; i++) {
            ChannelFuture connectedFuture = client.connect(ipAddress, port + i);
            connectedFuture.awaitUninterruptibly();

            assert connectedFuture.isDone();

            if (connectedFuture.isCancelled()) {

            } else if (!connectedFuture.isSuccess()) {

                logger.info("Connection is failed to " + ipAddress + ":" + port + i);
                connectedFuture.channel().close().awaitUninterruptibly();
            } else {
                logger.info("Connection is established to " + ipAddress + ":" + port + i);
                channel=connectedFuture.channel();
                break;
            }
        }
        return channel;
    }

    public void disconnectFrom(String ipAddress) {
        for (Channel c : connectedChannelsGroup) {
            if (((InetSocketAddress) c.remoteAddress()).getAddress().getHostAddress().equals(ipAddress)) {
                c.close().awaitUninterruptibly();
            }
        }
    }

    public void sendMessageToAllConnect(ChatMessage chatMessage) {
        if (connectedChannelsGroup.size() > 0) {
            Message message = MessageFactory.createTextMessageInstance();
            message.setFrom(chatMessage.getSender());
            message.setMessage(chatMessage.getContent());
            connectedChannelsGroup.writeAndFlush(message);
        }
    }

    public void downloadFile(File pathTo, String fileHash, String fromUser) {
        //TODO
    }

    public void downloadFile(String fileName, String fileHash, int fileSize, String fromIpAddress, File pathToDownload) {
        //TODO
    }

    public void shareDirOrFiles(ArrayList<File> sharedEntity) {
        FileSharer fileSharer = FileSharer.getInstance();
        fileSharer
                .setTargets(sharedEntity)
                .setInMemoryListOfSharedFiles(settings.getInMemoryListOfSharedFiles());
        Future future = settings.getTreadsPoll().submit(fileSharer);
    }

    public ConcurrentHashMap<String, FileNode> getMySharedFilesList() {
        return settings.getInMemoryListOfSharedFiles();
    }

    public boolean getSharedFileList(String ipAddress) {
        boolean isAlreadyConnected = false;
        Channel isConnectedChannel = null;
        Channel connectedChannel = null;
        for (Channel channel : connectedChannelsGroup) {
            String alreadyConnectedIp = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
            if (alreadyConnectedIp.equals(ipAddress)) {
                isAlreadyConnected = true;
                isConnectedChannel = channel;
                break;
            }
        }
        if (!isAlreadyConnected) {
            connectedChannel = connectTo(ipAddress);
            if(connectedChannel!=null){
                connectedChannel.writeAndFlush(MessageFactory.createGetFileListInstance());
                return true;
            }else{
                return false;
            }
        }else{
            isConnectedChannel.writeAndFlush(MessageFactory.createGetFileListInstance());
            return true;
        }
    }
}