import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;
import javax.net.ssl.SSLSocket;
import java.io.DataOutputStream;
import java.util.*;

public class ServerThread extends Thread {

	SSLSocket socket;
	
	ServerThread( SSLSocket socket ) {
		this.socket = socket;
	}
	
	public void run() {

        // Get an SSLParameters object from the SSLSocket
        SSLParameters sslp = this.socket.getSSLParameters();

        // Populate SSLParameters with the ALPN values
        // As this is server side, put them in order of preference
        //String[] serverAPs ={ "one", "two", "three" };
        //sslp.setApplicationProtocols(serverAPs);


        // Populate the SSLSocket object with the ALPN values
        socket.setSSLParameters(sslp);

        // After the handshake, get the application protocol that 
        // has been negotiated

        //String ap = socket.getApplicationProtocol();
        //System.out.println("Application Protocol server side: \"" + ap + "\"");
        
        /*try {
        	socket.startHandshake();
        } catch (Exception e) {
        	e.printStackTrace();
        }*/

        try {

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 			String msg = bufferedReader.readLine();
			System.out.println("Client message: " + msg);
			//Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
			//System.out.println("THREADS: " + threadSet);

			handleMessage( msg );

        } catch(IOException e) {
        	e.printStackTrace();
        }
	}

	private void handleMessage( String msg ) {

		String[] params = msg.split(" ");

		System.out.println( "params: " + params[0] + "..." + params[1] + "..." + params[2] );
			System.out.println( ':' + params[0] + ':' );
		if (params[0].equals("REGISTRY")) {
			System.out.println( "Inside REGISTRY" );
			String peeraddress = params[1].substring(1);
			int peerport = Integer.parseInt(params[2]);

			System.out.println( peeraddress + ' ' + peerport );
			System.out.println(Server.getInstance());
			Server.getInstance().addPeer( peeraddress, peerport, this.socket);
		}
		/*
		try {
			System.out.println("socket: " + socket);
			//DataOutputStream sendResponse = new DataOutputStream(socket.getOutputStream());
			//sendResponse.writeUTF("Ur registered successfully");
		} catch(IOException e) {
			e.printStackTrace();
		}*/
	}

}
