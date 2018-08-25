package com.simple_p2p.p2p_engine.timeevents;

import com.simple_p2p.p2p_engine.p2pcontrol.interfaces.P2PServerControl;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.internal.ConcurrentSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.TimerTask;

public class OpenAdditionConnections extends TimerTask {
    private Settings settings;
    private ChannelGroup channelGroup;
    private ConcurrentSet<String> online_users;
    private int numberConnectionsToMaintain;
    private P2PServerControl p2pServerControl;
    private String myIp;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public OpenAdditionConnections() {
        this.settings = Settings.getInstance();
        this.channelGroup = settings.getConnectedChannelGroup();
        this.online_users = settings.getOnline_users();
        this.p2pServerControl = settings.getSprAppCtx().getBean(P2PServerControl.class);
        this.numberConnectionsToMaintain = settings.getNumberConnectionsToMaintain();
        this.myIp = settings.getLocalAddress().getHostAddress();
    }

    @Override
    public void run() {
        //logger.info("start additional connect");
        for (String online_users_ip : online_users) {
            if (channelGroup.size() < numberConnectionsToMaintain) {
                boolean userFromOnlineListIsNotConnected = true;
                boolean userFromOnlineListIsNotMe = true;
                for (Channel channel : channelGroup) {
                    String ip = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
                    if (online_users_ip.equals(ip)) {
                        userFromOnlineListIsNotConnected = false;
                    }
                    if (online_users_ip.equals(myIp)) {
                        userFromOnlineListIsNotMe = false;
                    }
                    if (userFromOnlineListIsNotConnected && userFromOnlineListIsNotMe) {
                        System.out.println("connect");
                        p2pServerControl.connectTo(online_users_ip);
                    }
                }
            }
        }
    }
}
