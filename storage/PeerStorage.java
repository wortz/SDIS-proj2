package storage;

import java.util.concurrent.ConcurrentHashMap;

public class PeerStorage{
    private ConcurrentHashMap<String,String> savedFiles;

    public PeerStorage(){
        savedFiles=new ConcurrentHashMap<String,String>();
    }

    public void addFile(String fileID, String body){
        savedFiles.put(fileID, body);
    }

    public void deleteFile(String fileID){
        savedFiles.remove(fileID);
    }

    public String getFile(String fileID){
        return savedFiles.get(fileID);
    }
}