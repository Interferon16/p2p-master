package com.simple_p2p.p2p_engine.channel_handlers.inbound_handlers;

import com.simple_p2p.entity.FileNode;
import com.simple_p2p.entity.UserConnection;
import com.simple_p2p.p2p_engine.Message.Message;
import com.simple_p2p.p2p_engine.Message.MessageFactory;
import com.simple_p2p.p2p_engine.Message.MessageType;
import com.simple_p2p.p2p_engine.Utils.HashWork;
import com.simple_p2p.p2p_engine.server.Settings;
import com.simple_p2p.p2p_engine.DBWorker.DBWriteBuffer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class InboundChannelHandler extends ChannelInboundHandlerAdapter {
    private ChannelGroup channelGroup;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Settings settings;



    public InboundChannelHandler(Settings settings) {
        this.channelGroup = settings.getConnectedChannelGroup();
        this.settings = settings;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            Message message = (Message) msg;
            logger.info("Message:" + message.getType().toString());
            switch (message.getType()) {
                case BOOTSTRAP:
                    updateDBFromBootstrap(message);
                    break;
                case FIND:
                    findFileInShareByHash(ctx,message);
                    break;
                case GET_FILE_LIST:
                    returnFileList(ctx);
                    break;
                case GET_FILE_PART:
                    sendFilePart(ctx,message);
                    break;
                default:
                    sendToAll(msg, ctx);
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        if (cause.getMessage().equals("Удаленный хост принудительно разорвал существующее подключение")) {
            logger.info(ctx.channel().remoteAddress().toString() + " disconnected");
        } else {
            cause.printStackTrace();
        }
        ctx.flush();
        ctx.close();
    }


    private void updateDBFromBootstrap(Message message) {
        DBWriteBuffer dbWriteBuffer = DBWriteBuffer.getInstance();
        for (UserConnection uc:message.getUserConnections()){
            settings.getOnline_users().add(uc.getInetAddress());
            UserConnection userConnection = new UserConnection();
            userConnection.setInetAddress(uc.getInetAddress());
            userConnection.setDate_time(uc.getDate_time());
            userConnection.setOnline(uc.getOnline());
            dbWriteBuffer.addEntityToDB(userConnection);
        }
        logger.info("get bootstrap");
        logger.info("From:" + message.getFrom());
    }

    private void findFileInShareByHash(ChannelHandlerContext ctx, Message message){
        String requestedFileHash=message.getDataHash();
        ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles = settings.getInMemoryListOfSharedFiles();
        boolean fileIsExist=inMemoryListOfSharedFiles.containsKey(requestedFileHash);
        if(fileIsExist){
            message.setType(MessageType.ANSWER);
            message.setAnswerToRequest(true);
            ctx.writeAndFlush(message);
        }
    }

    private void returnFileList(ChannelHandlerContext ctx){
        ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles = settings.getInMemoryListOfSharedFiles();
        Message message = MessageFactory.createListOfFilesInstance();
        message.setListOfSharedFiles(inMemoryListOfSharedFiles);
        ctx.writeAndFlush(message);
    }

    private void sendFilePart(ChannelHandlerContext ctx, Message message){
        File pathToFile = null;
        FileNode fileNode = null;
        String requestedFileHash=message.getDataHash();
        int fileChunkNumber = message.getChunkNumber();
        int startBytesToRead = (settings.getStandardFileChunkLength()*fileChunkNumber);
        int chunkLength = settings.getStandardFileChunkLength();
        ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles = settings.getInMemoryListOfSharedFiles();
        if(inMemoryListOfSharedFiles.containsKey(requestedFileHash)){
            fileNode = inMemoryListOfSharedFiles.get(requestedFileHash);
        }
        if (fileNode != null) {
            pathToFile= new File(fileNode.getFilePath());
        }
        if(pathToFile!=null&&pathToFile.exists()){
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(pathToFile,"rw");
                Message dataMessage = MessageFactory.createDataPacketInstance();
                byte[] bytes = new byte[chunkLength];
                String hash = HashWork.toSHA1(bytes);
                randomAccessFile.read(bytes,startBytesToRead,chunkLength);
                dataMessage.setDataBuff(bytes);
                dataMessage.setDataBuffCapacity(bytes.length);
                dataMessage.setDataHash(hash);
                ctx.writeAndFlush(dataMessage);
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                } catch (IOException ignore){

                }
            }
        }else{
            message.setType(MessageType.ANSWER);
            message.setAnswerToRequest(false);
            message.setDataHash(requestedFileHash);
            ctx.writeAndFlush(message);
        }
    }

    private void sendToAll(Object msg, ChannelHandlerContext ctx) {
        for (Channel c : channelGroup) {
            if (c != ctx.channel()) {
                c.writeAndFlush(msg);
            }
        }
        ctx.fireChannelRead(msg);
    }
}
