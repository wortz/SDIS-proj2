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
    private int port;
    private static SSLSocket socket;
    Timer timer;
    TimerTask task;

    public RegisterServer(){

        try{
            address = InetAddress.getByName("127.0.0.2");
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.setProperty("javax.net.ssl.trustStore","truststore");
        System.setProperty("javax.net.ssl.trustStorePassword","123456");
		port = 4040;

        try{
            socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(address, port);
            socket.startHandshake();
            DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes("REGISTRY " + PeerServer.getInetAddress() + " " + PeerServer.getPort() + '\n');
        } catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void run() {
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
    }

    public synchronized void sendServerMessage(String message){       // fazer uma thread para enviar?
        try{
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes(message);

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public String receiveServerMessage(){
        String serverResponse = " ";
        try{
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverResponse = inFromClient.readLine();
        }catch(IOException e){
            e.printStackTrace();
        }
        return serverResponse;
    }

    /**
     * @return the socket
     */
    public static SSLSocket getSocket() {
        return socket;
    }

    

}