package protocol;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import peer.Peer;
import peer.RegisterServer;
import utility.Message;

public class Delete implements Runnable {

    private String path;

    public Delete(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        RegisterServer registerServer = Peer.getServer();
        registerServer.sendServerMessage("DELETE " + path + '\n');
        String responseServer = registerServer.receiveServerMessage();
        //DELETE HASH PEERIP PEERPORT .....
        String[] parts = responseServer.split(" ");
        String fileID=parts[1];
        Message msg = new Message("DELETE " + fileID);
        for(int i=2; i < parts.length;i+=2){
            String peerIp = parts[i];
            int peerPort = Integer.parseInt(parts[i+1]);
            PeerToPeer(msg, peerIp, peerPort);
        }
    }

    private void PeerToPeer(Message msg, String peerIp, int peerPort){
        InetAddress address;
        Socket socket;
        try{
            address = InetAddress.getByName(peerIp);
            socket = new Socket(address, peerPort);
            System.out.println("sent to peer: ip: " + peerIp + " port: " + peerPort);
            ObjectOutputStream outToPeer = new ObjectOutputStream(socket.getOutputStream());
            outToPeer.writeObject(msg);
            socket.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}