package com.simple_p2p.p2p_engine.server;


import com.simple_p2p.entity.FileNode;
import com.simple_p2p.p2p_engine.DBWorker.WriteFromBufferToDBTimeEvent;
import com.simple_p2p.p2p_engine.Utils.HashWork;
import com.simple_p2p.p2p_engine.Utils.NetworkEnvironment;
import com.simple_p2p.p2p_engine.channels_inits.ServerChannelInitializer;
import com.simple_p2p.p2p_engine.client.Client;
import com.simple_p2p.p2p_engine.timeevents.OpenAdditionConnections;
import com.simple_p2p.p2p_engine.timeevents.fortest.ShareFolderTemporally;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {

    private int port;
    private Channel listenerChannel;
    private Client client;
    private String myHash;
    private InetAddress localAddress;
    private String localMacAddress;
    private InetAddress externalAddress;
    private Settings settings;
    private EventLoopGroup listener;
    private EventLoopGroup connectionsLoop;
    private ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Server() {

    }

    public Server(Settings settings) {
        this.settings = settings;
        this.port = settings.getListener_port();
        this.listener = new NioEventLoopGroup();
        this.connectionsLoop = new NioEventLoopGroup();
        this.client = startClient(connectionsLoop, settings);
        this.inMemoryListOfSharedFiles = settings.getInMemoryListOfSharedFiles();
    }

    public void run() {
        getMyInfo();
        showMyInfo();
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getMyInfo() {
        this.myHash = HashWork.giveMyHash();
        this.localAddress = NetworkEnvironment.getLocalAddress();
        this.localMacAddress = NetworkEnvironment.getLocalMacAddressReadable();
        this.externalAddress = NetworkEnvironment.getExternalAddress();
        settings.setMyHash(myHash);
        settings.setLocalAddress(localAddress);
        settings.setLocalMacAddress(localMacAddress);
        settings.setExternalAddress(externalAddress);
    }

    private void showMyInfo() {
        logger.info("My hash: " + myHash);
        logger.info("My local address: " + localAddress.getHostAddress() + " | mac address: " + localMacAddress);
        logger.info("My external address: " + externalAddress.getHostAddress());
    }

    private void startServer() throws Exception {

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(listener, connectionsLoop);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ServerChannelInitializer(settings));
            boolean listenerBind = false;
            port--;
            int connectionPortCounts = 100;
            while (!listenerBind) {
                if (connectionPortCounts < 1) {
                    logger.error("All using port is bind or restricted, check network policy or multiple client is running");
                    System.exit(1);

                }
                connectionPortCounts--;
                port++;
                try {
                    listenerChannel = serverBootstrap.bind(port).sync().channel();
                    listenerBind = true;
                    logger.info("Listener is bind on port: " + port);
                } catch (Exception e) {
                    logger.warn("Port: " + port + " is already in use try another one");
                }
            }

            loadInMemoryListOfSharedFiles();
            startTimeEvents();
            logger.info("Server start");

            listenerChannel.closeFuture().sync();
        } finally {
            connectionsLoop.shutdownGracefully();
            listener.shutdownGracefully();
        }
    }

    private void startTimeEvents() {
        logger.info("Starting time events");
        Timer writeFromBufferToDBTimeEvent = new Timer("WriteFromBufferToDBTimeEvent", true);
        writeFromBufferToDBTimeEvent.scheduleAtFixedRate(new WriteFromBufferToDBTimeEvent(), 0, 5000);
        Timer openAdditionConnections = new Timer("OpenAdditionConnections", true);
        openAdditionConnections.scheduleAtFixedRate(new OpenAdditionConnections(), 5000, 30000);
        //testEvents();
    }

    private void testEvents() {
        logger.info("Start events for testing functionality");
        Timer shareFolderTemporally = new Timer("ShareFolderTemporally", true);
        shareFolderTemporally.scheduleAtFixedRate(new ShareFolderTemporally(), 10000, 20000);
    }

    private Client startClient(EventLoopGroup connectionsLoop, Settings settings) {
        client = new Client(connectionsLoop, settings);
        try {
            client.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    private void loadInMemoryListOfSharedFiles() {
        logger.info("Start load shared file in memory");
        ArrayList<FileNode> temporalListOfSharedFiles = getSharedFilesList();
        for (FileNode fileNode : temporalListOfSharedFiles) {
            inMemoryListOfSharedFiles.put(fileNode.getFileHash(), fileNode);
        }
        logger.info("End load shared file in memory");
    }

    private ArrayList<FileNode> getSharedFilesList() {
        Session session = Settings.getInstance().getSessionFactory().openSession();
        return (ArrayList<FileNode>) session.createCriteria(FileNode.class).list();
    }


    public Client getClient() {
        return client;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
