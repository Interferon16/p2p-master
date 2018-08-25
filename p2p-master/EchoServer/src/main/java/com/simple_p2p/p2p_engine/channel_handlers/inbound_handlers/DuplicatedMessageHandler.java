package com.simple_p2p.p2p_engine.channel_handlers.inbound_handlers;


import com.simple_p2p.p2p_engine.Message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CopyOnWriteArrayList;

public class DuplicatedMessageHandler extends ChannelInboundHandlerAdapter {

    private CopyOnWriteArrayList<Integer> messagesHashBuffer;


    public DuplicatedMessageHandler(CopyOnWriteArrayList<Integer> messagesHashBuffer){
        this.messagesHashBuffer=messagesHashBuffer;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        if(!messagesHashBuffer.contains(message.hashCode())){
            messagesHashBuffer.add(message.hashCode());
            super.channelRead(ctx, msg);
        }
    }
}
