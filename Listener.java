import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;
import javax.net.ssl.SSLSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class Listener extends Thread {

	SSLSocket socket;
	TimerTask peerTimeout;
	
	public Listener( SSLSocket socket ) {
		this.socket = socket;

		Timer timer = new Timer();
		peerTimeout = new TimerTask() {
			Server.getInstance().peers.remove(socket.getInetAddress());
		}
		timer.schedule(peerTimeout, 1*1100);
	}
	
	public void run() {

		// Get an SSLParameters object from the SSLSocket
		//SSLParameters sslp = this.socket.getSSLParameters();

		// Populate SSLParameters with the ALPN values
		// As this is server side, put them in order of preference
		//String[] serverAPs ={ "one", "two", "three" };
		//sslp.setApplicationProtocols(serverAPs);


		// Populate the SSLSocket object with the ALPN values
		//socket.setSSLParameters(sslp);

		// After the handshake, get the application protocol that 
		// has been negotiated

		//String ap = socket.getApplicationProtocol();
		//System.out.println("Application Protocol server side: \"" + ap + "\"");

		/*
		try {

			DataOutputStream sendResponse = new DataOutputStream(socket.getOutputStream());
			sendResponse.writeUTF("CONFIRMATION");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/

		while(true){

			try {

					BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					System.out.println("before readline LISTENER");
					String msg = inFromClient.readLine();
					System.out.println("after readline LISTENER");
					System.out.println("Client message: " + msg);

					handleMessage( msg );

			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleMessage( String msg ) {

		String[] params = msg.split(" ");

		switch(params[0]){

			case "ONLINE":
			System.out.println("client is ONLINE");
			peerTimeout.cancel();							// para dar reset ao timeout
			timer.schedule(peerTimeout, 1*1100);
			break;

			case "BACKUP":
			System.out.println("client wanna do a BACKUP");
			break;

		}
		if( params[0] == "BACKUP" ){
		}

	}

}
