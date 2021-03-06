package protocol;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import peer.RegisterServer;
import utility.*;
import peer.Peer;

public class Backup implements Runnable {

    protected String path;
    protected int replicationDegree;

    public Backup(String file_path, int replicationDegree) {
        this.path = file_path;
        this.replicationDegree = replicationDegree;
    }

    @Override
    public void run() {
        File file = new File(this.path);
        String fileID = Utility.getFileSHA(file);

        RegisterServer registerServer = Peer.getServer();
        registerServer.sendServerMessage("BACKUP " + path + " " + replicationDegree + " " + fileID + '\n');

        try {
            String headerAux = "PUTFILE " + fileID;
            byte[] body = Files.readAllBytes(Paths.get(this.path));
            Message msg = new Message(headerAux, body);
            String responseServer = registerServer.receiveServerMessage();
            System.out.println("server response : " + responseServer);
            String[] parts = responseServer.split(" ");

            System.out.println("PutChunk : " + fileID + '\n');

            for(int i = 0; i < replicationDegree; i++){
                String peerIp = parts[3+i*2];
                int peerPort = Integer.parseInt(parts[4+i*2]);
                PeerToPeer(msg, peerIp, peerPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sends the message with file from peer to another peer receiving as arguments the message the ip
    //and port  of the receiving peer
    public Message PeerToPeer(Message msg, String ip, int portt){
        InetAddress address;
        Socket socket;
        try{
            address = InetAddress.getByName(ip);
            socket = new Socket(address, portt);
            ObjectOutputStream outToPeer = new ObjectOutputStream(socket.getOutputStream());
            outToPeer.writeObject(msg);
            ObjectInputStream inFromPeer = new ObjectInputStream(socket.getInputStream());
            Message response = (Message) inFromPeer.readObject();
            socket.close();
            return response;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
        

    }
}