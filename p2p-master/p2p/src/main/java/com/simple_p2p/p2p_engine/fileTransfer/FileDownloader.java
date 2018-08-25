package com.simple_p2p.p2p_engine.fileTransfer;

import com.simple_p2p.entity.FileNode;
import com.simple_p2p.p2p_engine.server.Settings;
import io.netty.channel.group.ChannelGroup;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class FileDownloader {

    private Settings settings;
    private ChannelGroup connectedChannelGroup;
    private String activeDownloadHash;

    public static class FileDownloaderHolder {
        public static final FileDownloader HOLDER_INSTANCE = new FileDownloader();
    }

    public static FileDownloader getInstance() {
        return FileDownloader.FileDownloaderHolder.HOLDER_INSTANCE;
    }

    private FileDownloader() {
        this.settings=Settings.getInstance();
        this.connectedChannelGroup=settings.getConnectedChannelGroup();
    }

    private ConcurrentHashMap<String,DownloadedFileNode> listOfDownloads = new ConcurrentHashMap<>();

    public void addNewDownload(FileNode fileNode){
        String fileName = fileNode.getFileName();
        File filePath = new File(fileNode.getFilePath());
        String fileHash = fileNode.getFileHash();
        long fileSize = fileNode.getFileSize();
        DownloadedFileNode downloadedFileNode = new DownloadedFileNode(fileName,filePath,fileHash,fileSize);
        listOfDownloads.put(fileHash,downloadedFileNode);
    }

    public void addNewDownload(String fileName, File filePath, String fileHash, long fileSize){
        DownloadedFileNode downloadedFileNode = new DownloadedFileNode(fileName,filePath,fileHash,fileSize);
        listOfDownloads.put(fileHash,downloadedFileNode);
    }

    private void run(){

    }
}
