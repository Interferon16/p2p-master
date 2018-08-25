package com.simple_p2p.p2p_engine.channel_handlers.outbound_handlers;

import com.simple_p2p.p2p_engine.Message.Message;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class OutboundPacketHandler extends ChannelOutboundHandlerAdapter {
    private Settings settings;

    public OutboundPacketHandler(Settings settings){
        this.settings = settings;
    }
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(msg instanceof Message){
            ((Message) msg).setFrom(settings.getMyHash());
            ((Message) msg).setTimeStamp(System.currentTimeMillis());
        }
        super.write(ctx, msg, promise);
    }
}
