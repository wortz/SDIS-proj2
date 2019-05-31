package peer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public class MessageHandler implements Runnable {
    private DataOutputStream writer;
    private String[] messageSplit;
    private String message;

    MessageHandler(DataOutputStream outToClient, String receivedMessage) {
        writer = outToClient;
        messageSplit = getHeaderPacket(receivedMessage);
        message = receivedMessage;
    }

    @Override
    public void run() {
        String[] messageHeader = messageSplit[0].split(" ");
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
        String body = message.substring(messageSplit[0].length(), message.length() - 1); // talvez seja -1
        Peer.storage.addFile(fileID, body);
        String response = "STORED " + fileID + '\n';
        String pathBackup = "../PeerStorage/peer" + Peer.getPeerID() + "/" + "backup";
        File backupDir = new File(pathBackup);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
        String pathFileId = pathBackup + "/" + fileID;
        File pathToFile = new File(pathFileId);
        if (!pathToFile.exists()) {
            pathToFile.mkdir();
        }
        String fileDir = pathToFile + fileID;
        File fileFile = new File(fileDir);
        try{
            if (!fileFile.exists()) {
                FileOutputStream fos = new FileOutputStream(fileDir);
                fos.write(body.getBytes());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try {
            writer.writeBytes(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGetFile(String fileID) {
        String body = Peer.storage.getFile(fileID);
        String response = "FILE " + fileID + '\n' + body;
        try {
            writer.writeBytes(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(String fileID) {
        Peer.storage.deleteFile(fileID);
    }

    public String[] getHeaderPacket(String receivedMessage) {
        String[] lines = receivedMessage.split(System.getProperty("line.separator"));
        return lines;
    }
}
