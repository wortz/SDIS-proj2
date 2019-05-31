package peer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import utility.Message;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;

public class PeerServer implements Runnable{
    private static InetAddress peerAddress;
    private static int port;
    private static ServerSocket peerSocket;

    public PeerServer(String address, int portt){
        try{
        peerAddress=InetAddress.getByName(address);
        port = portt;
        peerSocket = new ServerSocket(port,10,peerAddress);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public synchronized static InetAddress getInetAddress(){
        return peerAddress;
    }


    public synchronized static int getPort(){
        return port;
    }

    @Override
    public void run(){
        String request;
        String capitalizedRequest;
        try{
            while (true) {
                Socket connectionSocket = peerSocket.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                Message receivedMessage = (Message) inFromClient.readObject();
                System.out.println(receivedMessage.header);
                System.out.println(new String(receivedMessage.body, StandardCharsets.UTF_8));
                //System.out.println("Received: " + request);
                connectionSocket.close();
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}