package protocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import peer.Peer;
import peer.RegisterServer;
import utility.Message;

public class Restore implements Runnable {
    private String path;

    public Restore(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        RegisterServer registerServer = Peer.getServer();
        registerServer.sendServerMessage("RESTORE " + path + '\n');
        String responseServer = registerServer.receiveServerMessage();
        String[] parts = responseServer.split(" ");
        String fileID = parts[1];
        String address = parts[2];
        int port = Integer.parseInt(parts[3]);
        String header = "GETFILE " + fileID;
        Message msg = new Message(header);
        Message response = PeerToPeer(msg, address, port);
        String pathRestore = "../PeerStorage/peer" + Peer.getPeerID() + "/" + "restore";
        File restoreDir = new File(pathRestore);
        if (!restoreDir.exists()) {
            restoreDir.mkdirs();
        }
        String restoreFileDir = pathRestore + "/" + path;;
        File restoreFile = new File(restoreFileDir);
        if (restoreFile.exists()) {
            restoreFile.delete();
        }
        try{
            FileOutputStream fos = new FileOutputStream(restoreFile);
            fos.write(response.getBody());
            fos.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private Message PeerToPeer(Message msg, String ip, int portt) {
        InetAddress address;
        Socket socket;
        try {
            address = InetAddress.getByName(ip);
            socket = new Socket(address, portt);
            System.out.println("sent to peer: ip: " + ip + " port: " + portt);
            ObjectInputStream inFromPeer = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outToPeer = new ObjectOutputStream(socket.getOutputStream());
            outToPeer.writeObject(msg);
            return ((Message) inFromPeer.readObject());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}