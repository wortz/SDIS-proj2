package peer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.security.Security;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class RegisterServer implements Runnable{
    
    private InetAddress address;
    private SSLSocket socket;

    public RegisterServer(){

        try{
            address = InetAddress.getByName("127.0.0.2");
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.setProperty("javax.net.ssl.trustStore","myTrustStore.jts");
        System.setProperty("javax.net.ssl.trustStorePassword","password");
		int port = 4040;
        try {
            socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(address, port);
            socket.startHandshake();
            DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes("REGISTRY " + PeerServer.getInetAddress() + " " + PeerServer.getPort() + '\n');
        } catch(IOException e){
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
        System.out.println("new thread");
            try{
                System.out.println("INside task thread");
                DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                outToServer.writeBytes("ONLINE " + PeerServer.getInetAddress() + " " + PeerServer.getPort() + '\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void sendServerMessage(){       // fazer uma thread para enviar?
        try{
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes("BOAS UM PROTOCOLO\n");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    

}