package peer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import utility.Message;
import java.nio.charset.StandardCharsets;

public class MessageHandler implements Runnable {
    private ObjectOutputStream writer;
    private Message message;

    MessageHandler(ObjectOutputStream outToClient, Message receivedMessage) {
        writer = outToClient;
        message = receivedMessage;
    }

    @Override
    public void run() {
        String[] messageHeader = message.getHeader().split(" ");
        System.out.println("params : " + message.getHeader());
        switch (messageHeader[0]) {
        case "PUTFILE":
            handlePutFile(messageHeader[1]);
            break;
        case "GETFILE":
            handleGetFile(messageHeader[1]);
            break;
        case "DELETE":
            handleDelete(messageHeader[1]);
            break;
        default:
            break;

        }
    }

    private void handlePutFile(String fileID) {
        byte[] body = message.getBody();
        Peer.storage.addFile(fileID, body);
        String response = "STORED " + fileID;
        Message respMsg=new Message(response);
        String pathBackup = "../PeerStorage/peer" + Peer.getPeerID() + "/" + "backup/";
        File backupDir = new File(pathBackup);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }

        String fileDir = pathBackup + fileID;
        File fileFile = new File(fileDir);

        System.out.println(fileDir);
        System.out.println(fileFile.exists());

        try{
            if (!fileFile.exists()) {
                FileOutputStream fos = new FileOutputStream(fileDir);
                fos.write(body);
                fos.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        try {
            writer.writeObject(respMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGetFile(String fileID) {
        byte[] body = Peer.storage.getFile(fileID);
        String response = "FILE " + fileID;
        Message respMsg=new Message(response, body);
        try {
            writer.writeObject(respMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(String fileID) {
        Peer.storage.deleteFile(fileID);
        String pathBackup = "../PeerStorage/peer" + Peer.getPeerID() + "/" + "backup";
        File backupDir = new File(pathBackup);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
        String pathFileId = pathBackup + "/" + fileID;
        File fileFile = new File(pathFileId);
        if(fileFile.exists()){
            fileFile.delete();
        }
    }
}
