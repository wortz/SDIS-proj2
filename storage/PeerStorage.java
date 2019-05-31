package storage;

import java.util.concurrent.ConcurrentHashMap;

public class PeerStorage{
    private ConcurrentHashMap<String,byte[]> savedFiles;

    public PeerStorage(){
        savedFiles=new ConcurrentHashMap<String,byte[]>();
    }

    public void addFile(String fileID, byte[] body){
        savedFiles.put(fileID, body);
    }

    public void deleteFile(String fileID){
        savedFiles.remove(fileID);
    }

    public byte[] getFile(String fileID){
        return savedFiles.get(fileID);
    }
}