package com.simple_p2p.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "FileNodes")
public class FileNode implements Serializable, EntityUniqueConstrain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int Id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_name_hash")
    private String fileNameHash;

    @Column(name = "file_hash")
    private String fileHash;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "last_modified")
    private Date lastModified;

    public FileNode() {

    }

    public FileNode(String filePath, String fileName, String fileNameHash, String fileHash, long fileSize, Date lastModified) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileNameHash = fileNameHash;
        this.fileHash = fileHash;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNameHash() {
        return fileNameHash;
    }

    public void setFileNameHash(String fileNameHash) {
        this.fileNameHash = fileNameHash;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileNode fileNode = (FileNode) o;
        return Id == fileNode.Id &&
                fileSize == fileNode.fileSize &&
                Objects.equals(filePath, fileNode.filePath) &&
                Objects.equals(fileName, fileNode.fileName) &&
                Objects.equals(fileNameHash, fileNode.fileNameHash) &&
                Objects.equals(fileHash, fileNode.fileHash) &&
                Objects.equals(lastModified, fileNode.lastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, filePath, fileName, fileNameHash, fileHash, fileSize, lastModified);
    }

    @Override
    public String toString() {
        return "FileNode{" +
                "Id=" + Id +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileNameHash='" + fileNameHash + '\'' +
                ", fileHash='" + fileHash + '\'' +
                ", fileSize=" + fileSize +
                ", lastModified=" + lastModified +
                '}';
    }

    @Override
    public String getUniqueConstrainName() {
        return "file_path";
    }

    @Override
    public Object getUniqueConstrainValue() {
        return filePath;
    }
}
