package com.simple_p2p.controller;

import com.simple_p2p.entity.UserConnection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class IPController {

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

    public void setOnlineUser(String inetAddress, UserConnection userConnection) {
        userConnection.setInetAddress(inetAddress);
        userConnection.setDate_time(LocalDateTime.now());
        userConnection.setOnline(1);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(userConnection);
        session.getTransaction().commit();
        session.close();
    }

    public void setOfflineUser(UserConnection userConnection) {
        userConnection.setOnline(0);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(userConnection);
        session.getTransaction().commit();
        session.close();
    }
}
