package com.simple_p2p.p2p_engine.server;


import com.simple_p2p.p2p_engine.channels_inits.ServerChannelInitializer;
import com.simple_p2p.p2p_engine.timeevents.SendBootstrapMessageEvent;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class Server implements Runnable {

    private int port;
    private Channel listenerChannel;
    private Settings settings;
    private Timer timeEvents;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Server() {

    }

    public Server(Settings settings) {
        this.settings = settings;
        this.port = settings.getListener_port();
        this.timeEvents = new Timer();
    }

    public void run() {
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startServer() throws Exception {
        EventLoopGroup listener = new NioEventLoopGroup();
        EventLoopGroup connectionsLoop = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(listener, connectionsLoop);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
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

            logger.info("initialized server");

            Class.forName("com.simple_p2p.p2p_engine.Message.Message");


            timeEvents.scheduleAtFixedRate(new SendBootstrapMessageEvent(settings.getConnectedChannelGroup()), 0, 5000);


            listenerChannel.closeFuture().sync();
        } finally {
            connectionsLoop.shutdownGracefully();
            listener.shutdownGracefully();


        }
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
