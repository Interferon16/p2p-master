package com.simple_p2p.p2p_engine.p2pcontrol.interfaces;

import com.simple_p2p.model.ChatMessage;
import io.netty.channel.Channel;

import java.io.File;
import java.util.ArrayList;

public interface P2PServerControl {
    void sendMessageToAllConnect(ChatMessage chatMessage);
    void shareDirOrFiles(ArrayList<File> sharedEntity);
    Channel connectTo(String inetAddress);
    boolean getSharedFileList(String ipAddress);
    void downloadFile(String fileName, String fileHash, int fileSize, String fromIpAddress, File pathToDownload);
}
