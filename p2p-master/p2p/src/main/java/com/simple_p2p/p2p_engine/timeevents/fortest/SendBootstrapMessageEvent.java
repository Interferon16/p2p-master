package com.simple_p2p.p2p_engine.timeevents.fortest;

import com.simple_p2p.entity.KnownUsers;
import io.netty.channel.group.ChannelGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class SendBootstrapMessageEvent extends TimerTask {

    private ChannelGroup channelGroup;

    public SendBootstrapMessageEvent(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    @Override
    public void run() {
/*        List<KnownUsers> knownUsers = createDummyList();
        Message bootMessage = MessageFactory.createBootstrapInstance();
        bootMessage.setKnownNode(knownUsers);
        channelGroup.writeAndFlush(bootMessage);*/
    }

    private List<KnownUsers> createDummyList() {
        List<KnownUsers> knownUsers = new ArrayList<>();
        knownUsers.add(new KnownUsers("qwertyuiopsdfghjklzxcvnm", "Alice", "123.126.15.25", "144.55.23.128"));
        knownUsers.add(new KnownUsers("rdhhfdhdfhrhdf455y34dfdf", "Bob", "123.127.40.55", "144.111.16.256"));
        knownUsers.add(new KnownUsers("5454sd54s5hs445g4sd4s5ge", "Cindy", "123.201.11.60", "144.144.56.30"));
        return knownUsers;
    }
}
