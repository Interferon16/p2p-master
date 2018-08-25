package com.simple_p2p.p2p_engine.client;

import com.simple_p2p.p2p_engine.Utils.NetworkEnvironment;
import com.simple_p2p.p2p_engine.channels_inits.ClientChannelInitializer;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;


public class Client {
    private EventLoopGroup connectionsLoop;
    private Bootstrap clientBootstrap;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private InetAddress localAddress;
    private Settings settings;
    private String signalServerAddress;


    public Client(EventLoopGroup connectionsLoop, Settings settings) {
        this.settings=settings;
        this.connectionsLoop = connectionsLoop;
        this.localAddress = NetworkEnvironment.getLocalAddress();
        this.signalServerAddress = settings.getSignalServerAddress();
    }

    public void run() throws Exception {
        clientBootstrap = new Bootstrap();
        clientBootstrap.group(connectionsLoop);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500);
        clientBootstrap.handler(new ClientChannelInitializer(settings));
        logger.info("Client start");
        connectOnStart();

    }


    private void connectOnStart(){
        logger.info("Connect to signal server");
        connect(signalServerAddress,8071);
    }

    public ChannelFuture connect(String address, int port){
        ChannelFuture channelFuture=clientBootstrap.connect(address, port);
        return channelFuture;
    }
}
