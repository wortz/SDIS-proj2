package peer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerServer implements Runnable{
    private static InetAddress peerAddress;
    private static ServerSocket peerSocket;

    public PeerServer(String addr, int port){
        try{
        peerAddress = InetAddress.getByName(addr);
        peerSocket= new ServerSocket(port,10,peerAddress);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String request;
        String capitalizedRequest;
        try{
            while (true) {
                Socket connectionSocket = peerSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                request = inFromClient.readLine();
                System.out.println("Received: " + request);
                capitalizedRequest = request.toUpperCase() + 'n';
                outToClient.writeBytes(capitalizedRequest);
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



}