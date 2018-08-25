package com.simple_p2p.p2p_engine.channel_handlers.handshake;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandshakeHandler extends AbstractHandshakeHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ServerHandshakeHandler(ChannelGroup channelGroup) {
        super(channelGroup, HandlerType.SERVER);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        ctx.channel().pipeline().remove(ServerHandshakeHandler.class);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
