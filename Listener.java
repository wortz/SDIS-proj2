import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;
import javax.net.ssl.SSLSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class Listener extends Thread{

	SSLSocket socket;
	
	public Listener( SSLSocket socket ) {
		this.socket=socket;
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

		try {

			DataOutputStream sendResponse = new DataOutputStream(socket.getOutputStream());
			sendResponse.writeUTF("Ur registered successfully. Ready to receive messages.");
		
		} catch (Exception e) {
			e.printStackTrace();
		}

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

		String[] params = msg.split("");

		if( params[0] == "BACKUP" ){
			System.out.println("client wanna do a BACKUP");
		}

	}

}
