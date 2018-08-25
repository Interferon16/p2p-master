package com.simple_p2p.p2p_engine.channel_handlers.inbound_handlers;

import com.simple_p2p.controller.IPController;
import com.simple_p2p.entity.UserConnection;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


public class InboundChannelHandler extends ChannelInboundHandlerAdapter {
    private ChannelGroup channelGroup;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Settings settings;
    IPController mainController = new IPController();
    UserConnection userConnection = new UserConnection();
    String remoteAddress;


    public InboundChannelHandler(Settings settings) {
        this.channelGroup = settings.getConnectedChannelGroup();
        this.settings = settings;
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

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        remoteAddress = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
        mainController.setOnlineUser(remoteAddress, userConnection);
        logger.warn("-----------------------------"+remoteAddress+" online----------------------------");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        mainController.setOfflineUser(userConnection);
        logger.warn("-----------------------------"+remoteAddress+" offline----------------------------");
        super.channelInactive(ctx);
    }

}
