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

        try{
            socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(address, port);
            socket.startHandshake();
            DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes("REGISTRY " + PeerServer.getInetAddress() + " " + PeerServer.getPort() + '\n');
<<<<<<< HEAD
        } catch(IOException e){
=======
        } catch(Exception e){
>>>>>>> 80a224bd343b0cd67715eaf97ece49c187f56682
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
<<<<<<< HEAD
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
=======
        System.out.println("new thread RegisterServer");

        //timer = new Timer();
        //task = new TimerTask() {
            //public void run () {
                try{
                    System.out.println("INside task thread");
                    DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
                    DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                    outToServer.writeBytes("ONLINE " + PeerServer.getInetAddress() + " " + PeerServer.getPort() + '\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
        //   }
        //};
        //timer.scheduleAtFixedRate(task, 1*500, 1*500);
        System.out.println("end thread");
>>>>>>> 80a224bd343b0cd67715eaf97ece49c187f56682
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