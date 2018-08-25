package com.simple_p2p;

import com.simple_p2p.p2p_engine.p2pcontrol.impl.P2PServerControlImpl;
import com.simple_p2p.p2p_engine.p2pcontrol.interfaces.P2PServerControl;
import com.simple_p2p.p2p_engine.server.Server;
import com.simple_p2p.p2p_engine.server.ServerFactory;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class SimpleP2P {

	public static void main(String[] args) {
		SpringApplication.run(SimpleP2P.class, args);
	}


/*	@Bean
	public SessionFactory sessionFactory() {
			Configuration configuration = new Configuration();
			configuration.configure();
			SessionFactory sessionFactory = configuration.buildSessionFactory();
			return sessionFactory;
	}*/

	@Bean
	@Autowired
	public Settings buildSettings(ApplicationContext appCtx){
		Settings settings=Settings.getInstance();
		settings.setSprAppCtx(appCtx);
		settings.setListener_port(16161);
		settings.setConnectedChannelGroup(new DefaultChannelGroup(new DefaultEventExecutor()));
		settings.setMessagesHashBuffer(new CopyOnWriteArrayList<>());
		Configuration configuration = new Configuration();
		configuration.configure();
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		settings.setSessionFactory(sessionFactory);
		settings.setSignalServerAddress("192.168.0.84");
		settings.setSignalServerPort(8071);
/*		DBWriteHandler dbWriteHandler = DBWriteHandler.getInstance();
		settings.setDbWriteHandler(dbWriteHandler);*/
/*        CopyOnWriteArrayList<FileNode> fileNodes = new CopyOnWriteArrayList<>();
        fileNodes.addAll(fileNodeRepository.findAll());*/
		return settings;
	}

	@Bean
	@Autowired
	public P2PServerControl p2PServerControl(Settings settings){
		Server server = ServerFactory.getServerInstance(settings);
		return new P2PServerControlImpl(server,settings);
	}
}
