import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.security.Security;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.sun.net.ssl.internal.ssl.Provider;


public class RegisterServer implements Runnable{
    
    private InetAddress address;
    private SSLSocket socket;

    public RegisterServer(){
        address = InetAddress.getByName("127.0.0.2");
        System.setProperty("javax.net.ssl.trustStore","sdis.store");
        System.setProperty("javax.net.ssl.trustStorePassword","password");
        Security.addProvider(new Provider());
		int port = 4040;
        socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(address, port);
        socket.startHandshake();
    }


    @Override
    public void run() {
        try{
            
			DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes("ONLINE " + PeerServer.getInetAddress() + " " + PeerServer.getPort() + '\n');
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void sendServerMessage(){
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        outToServer.writeBytes("BOAS UM PROTOCOLO\n");
    }

    

}