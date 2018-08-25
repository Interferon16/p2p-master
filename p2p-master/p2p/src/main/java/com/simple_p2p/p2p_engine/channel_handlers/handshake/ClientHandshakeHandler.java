package com.simple_p2p.p2p_engine.channel_handlers.handshake;

import com.simple_p2p.p2p_engine.Message.Message;
import com.simple_p2p.p2p_engine.Message.MessageFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandshakeHandler extends AbstractHandshakeHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ClientHandshakeHandler(ChannelGroup channelGroup) {

        super(channelGroup,HandlerType.CLIENT);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Message handshake = MessageFactory.createHandshakeInstance();
        handshake.setMessage("hello");
        ctx.channel().writeAndFlush(handshake);
        logger.info("Sending handshake packet");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        ctx.channel().pipeline().remove(ClientHandshakeHandler.class);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
