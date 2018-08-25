package com.simple_p2p.server;


import com.simple_p2p.p2p_engine.server.Server;
import com.simple_p2p.p2p_engine.server.ServerFactory;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutor;

import java.util.concurrent.CopyOnWriteArrayList;

public class NetServerThread {

    public static void main(String[] args) {
/*        try {
            ServerSocket serverSocket = new ServerSocket(8071);
            System.out.println("initalized server");
            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println(socket.getInetAddress().getHostAddress() + " connected");
                ServerThread thread = new ServerThread(socket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Settings settings=buildSettings();
        Server server = ServerFactory.getServerInstance(settings);
    }

    public static Settings buildSettings(){
        Settings settings=Settings.getInstance();
        settings.setListener_port(8071);
        settings.setConnectedChannelGroup(new DefaultChannelGroup(new DefaultEventExecutor()));
        settings.setMessagesHashBuffer(new CopyOnWriteArrayList<>());
        return settings;
    }
}

