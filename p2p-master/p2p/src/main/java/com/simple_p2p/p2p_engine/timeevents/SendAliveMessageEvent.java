package com.simple_p2p.p2p_engine.timeevents;

import com.simple_p2p.p2p_engine.Message.MessageFactory;
import io.netty.channel.group.ChannelGroup;

import java.util.TimerTask;

public class SendAliveMessageEvent extends TimerTask{

    private ChannelGroup channelGroup;

    public SendAliveMessageEvent(ChannelGroup channelGroup){
        this.channelGroup=channelGroup;
    }
    @Override
    public void run() {
        channelGroup.writeAndFlush(MessageFactory.createPingInstance());
    }
}
