package com.simple_p2p.p2p_engine.fiile_work;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.simple_p2p.entity.FileNode;
import com.simple_p2p.p2p_engine.DBWorker.DBWriteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


public class FileSharer implements Callable<ArrayList<FileNode>> {


    Logger logger = LoggerFactory.getLogger(this.getClass());
    private HashFunction fileBodyHashFunction = Hashing.sha1();
    private HashFunction fileNameHashFunction = Hashing.md5();
    private ArrayList<FileNode> listOfHashedFiles = new ArrayList<>();
    private long finishHashedSize;
    private long totalSpaceOfIndexedFiles;
    private ArrayList<File> indexedFileList = new ArrayList<>();
    private long startHashingTime;
    private ArrayList<File> target;
    private int numberOfIndexedFiles = 0;
    private boolean working = false;
    private ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles;

    private FileSharer() {
    }

    private static class SettingsHolder {
        public static final FileSharer HOLDER_INSTANCE = new FileSharer();
    }

    public static FileSharer getInstance() {
        return FileSharer.SettingsHolder.HOLDER_INSTANCE;
    }

    @Override
    public ArrayList<FileNode> call() throws FileNotFoundException, AccessDeniedException {
        try {
            working = true;
            this.startHashingTime = System.currentTimeMillis();
            logger.info("Start indexing files");
            for (File file : target) {
                indexShare(file);
            }
            logger.info("End indexing, start hashing");
            hashIndexedFiles(indexedFileList);
            //logger.info("Compared sharing files with already shared");
            //compareAlreadySharedToNewlyShared(inMemoryListOfSharedFiles,listOfHashedFiles);
            logger.info("End hashing, start updating DB");
            updateSharedFilesToDB(listOfHashedFiles);
            return listOfHashedFiles;
        } finally {
            cleanState();
        }
    }

    private void cleanState() {
        this.listOfHashedFiles.clear();
        this.finishHashedSize = 0;
        this.totalSpaceOfIndexedFiles = 0;
        this.indexedFileList.clear();
        this.startHashingTime = 0;
        this.target = null;
        this.numberOfIndexedFiles = 0;
        this.working = false;
    }

    public FileSharer setTargets(ArrayList<File> target) {
        this.target = target;
        return this;
    }

    public FileSharer setInMemoryListOfSharedFiles(ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles) {
        this.inMemoryListOfSharedFiles = inMemoryListOfSharedFiles;
        return this;
    }

    private void indexShare(File file) throws FileNotFoundException, AccessDeniedException {
        boolean pathNotExist = !file.exists();
        boolean pathNotReadable = !file.canRead();
        if (pathNotExist) {
            throw new FileNotFoundException(file.getName());
        }
        if (pathNotReadable) {
            throw new AccessDeniedException(file.getName());
        }
        if (file.isFile()) {
            indexedFileList.add(file);
            numberOfIndexedFiles++;
            totalSpaceOfIndexedFiles += file.length();
        }
        if (file.isDirectory()) {
            indexDir(file);
        }
    }

    private void indexDir(File file) throws FileNotFoundException, AccessDeniedException {
        File[] subFiles = file.listFiles();
        if (subFiles != null && subFiles.length > 0) {
            for (File f : subFiles) {
                indexShare(f);
            }
        }
    }

    private void hashIndexedFiles(ArrayList<File> fileList) {
        for (File file : fileList) {
            String fileBodyHashCode = null;
            try {
                fileBodyHashCode = Files.asByteSource(file).hash(fileBodyHashFunction).toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String fileNameHashCode = fileNameHashFunction.newHasher().putString(file.getName(), Charset.forName("UTF8")).hash().toString();
            String fileName = file.getName();
            String filePath = file.getPath();
            Date lastModified = new Date(file.lastModified());
            long fileSize = file.length();
            listOfHashedFiles.add(new FileNode(filePath, fileName, fileNameHashCode, fileBodyHashCode, fileSize, lastModified));
            finishHashedSize += file.length();
        }
    }

    private void compareAlreadySharedToNewlyShared(ConcurrentHashMap<String, FileNode> inMemoryListOfSharedFiles, ArrayList<FileNode> listOfHashedFiles){
        for(FileNode fileNode:listOfHashedFiles) {
            String filePath = fileNode.getFilePath();
            for (Map.Entry<String, FileNode> entry : inMemoryListOfSharedFiles.entrySet()) {
                String filePathToCompare = entry.getValue().getFilePath();
                if (filePath.equals(filePathToCompare)) {
                    listOfHashedFiles.remove(fileNode);
                }
            }
        }
        listOfHashedFiles.trimToSize();
        for (FileNode fileNode:listOfHashedFiles){
            logger.info(fileNode.toString());
        }
    }

    private void updateSharedFilesToDB(ArrayList<FileNode> listOfFilesToShare) {
        DBWriteBuffer dbWriteBuffer = DBWriteBuffer.getInstance();
        logger.info("updating DB");
        for (FileNode fileNode : listOfFilesToShare) {
            logger.info(fileNode.toString());
            dbWriteBuffer.addEntityToDB(fileNode);
            inMemoryListOfSharedFiles.put(fileNode.getFileHash(),fileNode);
        }
        for (Map.Entry<String,FileNode> entry:inMemoryListOfSharedFiles.entrySet()){
            System.out.println(entry.getValue().toString());
        }
    }

    public long getFinishHashedSize() {
        return finishHashedSize;
    }

    public long getTotalSpaceOfIndexedFiles() {
        return totalSpaceOfIndexedFiles;
    }

    public long getStartHashingTime() {
        return startHashingTime;
    }

    public int getNumberOfIndexedFiles() {
        return numberOfIndexedFiles;
    }

    public int getNumberOfHashedFiles() {
        return listOfHashedFiles.size();
    }

    public boolean isWorking() {
        return working;
    }

}


