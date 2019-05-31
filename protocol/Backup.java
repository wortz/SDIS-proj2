package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import utility.Utility;

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

        try {
            String headerAux = "PUTFILE " + " " + fileID + " ";
            byte[] header = headerAux.getBytes("US-ASCII");
            byte[] body = Files.readAllBytes(Paths.get(this.path));
            byte[] message = new byte[header.length + body.length];
            System.arraycopy(header, 0, message, 0, header.length);
            System.arraycopy(body, 0, message, header.length, body.length);
            System.out.println("PutChunk : " + fileID + '\n');
            PeerToPeer(message);
            //PeerToPeer(message);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sends the message with file from peer to another peer receiving as arguments the message the ip 
    //and port  of the receiving peer
    
    //public void PeerToPeer(byte[] message, String peerAddress, int peerPort)
    //public void PeerToPeer(byte[] message, InetAddress peerAddress, int peerPort){
    public void PeerToPeer(byte[] message){
        
        InetAddress address;
        Socket socket;
        try{
            address = InetAddress.getByName("127.0.0.6");
            //address = InetAddress.getByName(peerAdress);
            //address = peerAddress;
            int port = 4040;
            //int port = peerPort;
            socket = new Socket(address, port);
            DataInputStream inFromPeer = new DataInputStream(socket.getInputStream());

            DataOutputStream outToPeer = new DataOutputStream(socket.getOutputStream());
            outToPeer.write(message);
        } catch(Exception e) {
            e.printStackTrace();
        }
        

    }
}