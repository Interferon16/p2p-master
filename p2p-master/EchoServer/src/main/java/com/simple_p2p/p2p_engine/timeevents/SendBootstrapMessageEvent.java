package com.simple_p2p.p2p_engine.timeevents;

import com.simple_p2p.entity.UserConnection;
import com.simple_p2p.p2p_engine.Message.Message;
import com.simple_p2p.p2p_engine.Message.MessageFactory;
import io.netty.channel.group.ChannelGroup;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.TimerTask;

public class SendBootstrapMessageEvent extends TimerTask{

    private ChannelGroup channelGroup;

    @Autowired
    private static SessionFactory sessionFactory;
    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public SendBootstrapMessageEvent(ChannelGroup channelGroup){
        this.channelGroup=channelGroup;
    }
    @Override
    public void run() {
        List<UserConnection> onlineUsers = getOnlineUsers();
        Message bootMessage = MessageFactory.createBootstrapInstance();
        bootMessage.setUserConnections(onlineUsers);
        System.out.println("Send bootstrap to "+channelGroup.size()+" users");
        for(UserConnection uc:bootMessage.getUserConnections()){
            System.out.println(uc.getInetAddress());
        }
        channelGroup.writeAndFlush(bootMessage);
    }

    private List<UserConnection> getOnlineUsers(){
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(UserConnection.class);
        List<UserConnection> list = criteria.add(Restrictions.eq("online", 1)).list();
        return list;
    }
}
