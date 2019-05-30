package peer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class PeerServer implements Runnable{
    private static InetAddress peerAddress;
    private static int port;
    private static SSLServerSocket peerSocket;

    public PeerServer(String address, int portt){
        try{
        peerAddress=InetAddress.getByName(address);
        port = portt;
        System.setProperty("javax.net.ssl.keyStore","myKeyStore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword","password");
        peerSocket = (SSLServerSocket)((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).createServerSocket(port, 50, peerAddress);
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
        try{
            while (true) {
                
                SSLSocket connectionSocket = (SSLSocket) peerSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                request = inFromClient.readLine();
                System.out.println("Received: " + request);
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}