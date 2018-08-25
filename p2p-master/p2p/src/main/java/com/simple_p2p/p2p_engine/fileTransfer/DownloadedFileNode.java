package com.simple_p2p.p2p_engine.fileTransfer;

import com.simple_p2p.p2p_engine.server.Settings;

import java.io.File;
import java.util.ArrayList;

public class DownloadedFileNode{

    private String fileName;
    private File FilePath;
    private String FileHash;
    private long fileSize;
    private boolean isFinished = false;
    private boolean isChecked = false;
    private boolean[] isDownloadedFileParts;
    private ArrayList<String> fileHostsIp = new ArrayList<>();
    private long lastAnnounce;


    public DownloadedFileNode(String fileName, File filePath, String fileHash, long fileSize) {
        this.fileName = fileName;
        FilePath = filePath;
        FileHash = fileHash;
        this.fileSize = fileSize;
        setFilePartNumberArray(fileSize);
    }

    private void setFilePartNumberArray(long size){
        int standardFileChunk = Settings.getInstance().getStandardFileChunkLength();
        int number = (int)size/standardFileChunk+1;
        isDownloadedFileParts = new boolean[number];
    }

    public String getFileName() {
        return fileName;
    }

    public File getFilePath() {
        return FilePath;
    }

    public String getFileHash() {
        return FileHash;
    }

    public long getFileSize() {
        return fileSize;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean[] getIsDownloadedFileParts() {
        return isDownloadedFileParts;
    }

    public ArrayList<String> getFileHostsIp() {
        return fileHostsIp;
    }

    public long getLastAnnounce() {
        return lastAnnounce;
    }

    public void setLastAnnounce(long lastAnnounce) {
        this.lastAnnounce = lastAnnounce;
    }
}




