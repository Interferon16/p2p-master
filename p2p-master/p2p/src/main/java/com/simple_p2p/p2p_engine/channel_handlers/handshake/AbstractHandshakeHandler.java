package com.simple_p2p.p2p_engine.channel_handlers.handshake;

import com.simple_p2p.p2p_engine.Message.Message;
import com.simple_p2p.p2p_engine.Message.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractHandshakeHandler extends ChannelInboundHandlerAdapter {
    private ChannelGroup channelGroup;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private HandlerType handlerType;
    private boolean handshakeTrue = false;
    private AttributeKey<String> userHash = AttributeKey.valueOf("userHash");

    public enum HandlerType {
        SERVER,
        CLIENT
    }

    ;

    public AbstractHandshakeHandler(ChannelGroup channelGroup, HandlerType handlerType) {
        this.channelGroup = channelGroup;
        this.handlerType = handlerType;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        if (message.getType() == MessageType.HANDSHAKE) {
            boolean handshake = processingHandshake(message, ctx);
            if (!handshake) {
                ctx.channel().close().awaitUninterruptibly();
                logger.warn("Handshake with " + ctx.channel().remoteAddress().toString() + "not accepted, connection dropped");
            }
        } else {
            ctx.channel().close().awaitUninterruptibly();
            logger.warn("Handshake with " + ctx.channel().remoteAddress().toString() + " don't received, connection dropped");
        }
        //ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            if (!this.handshakeTrue) {
                logger.warn("Connection dropped by timeout");
                ctx.channel().close().awaitUninterruptibly();
            }
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }

    private boolean processingHandshake(Message message, ChannelHandlerContext ctx) {
        ctx.channel().attr(userHash).set(message.getFrom());
        channelGroup.add(ctx.channel());
        logger.info("Channel add" + ctx.channel().toString());
        ctx.channel().pipeline().remove(ReadTimeoutHandler.class);
        this.handshakeTrue = true;
        if (this.handlerType == HandlerType.SERVER) {
            message.setMessage("Hello back");
            ctx.writeAndFlush(message);
            logger.info("Handshake received, sending back");
        }
        return true;
    }
}
