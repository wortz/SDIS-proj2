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
    private BufferedReader inFromServer;
    private DataOutputStream outToServer;

    public RegisterServer(String address, int port){

        try{
            this.address = InetAddress.getByName(address);
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.setProperty("javax.net.ssl.trustStore","../truststore");
        System.setProperty("javax.net.ssl.trustStorePassword","123456");
        System.setProperty("javax.net.ssl.keyStore","../client.keys");
        System.setProperty("javax.net.ssl.keyStorePassword","123456");
		this.port = port;

        try{
            socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(address, port);
            socket.startHandshake();
            inFromServer  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes("REGISTRY " + PeerServer.getInetAddress() + " " + PeerServer.getPort() + '\n');
        } catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void run() {

        //timer = new Timer();
        //task = new TimerTask() {
            //public void run () {
                try{
                    //DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
                    //DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                    outToServer.writeBytes("ONLINE " + PeerServer.getInetAddress() + " " + PeerServer.getPort() + '\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
        //   }
        //};
        //timer.scheduleAtFixedRate(task, 1*500, 1*500);
    }

    public synchronized void sendServerMessage(String message){       // fazer uma thread para enviar?
        try{
            //DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes(message);

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public String receiveServerMessage(){
        String serverResponse = " ";
        try{

            System.out.println("Server response before: " + serverResponse);
            serverResponse = inFromServer.readLine();
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