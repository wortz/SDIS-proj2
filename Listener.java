import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;
import javax.net.ssl.SSLSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.*;

public class Listener implements Runnable {

	SSLSocket socket;
	Timer timer;
	Thread peertimeout;
	ScheduledFuture<?> future;
	Boolean run;
	
	public Listener( SSLSocket socket, String address ) {

		this.socket = socket;

		peertimeout = new Thread( new Runnable() {public void run () {
			System.out.println("timeout thread");
			System.out.println("sessino : " + socket.getSession());
			Server.removePeer(address);
			//System.out.println(Server.getInstance().getPeers());
			run = false;
			System.out.println(run);
			future.cancel(false);
			//Server.getScheduler().
			}		//falta parar a thread Listener
		} );
		future = Server.getScheduler().scheduleAtFixedRate( peertimeout, 1100, 1100, TimeUnit.MILLISECONDS );

	}
	
	public void run() {
		run = true;
		System.out.println("new Listener");

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


		while(run){
			System.out.println("inside while Listener");
			try {

					BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					System.out.println("before readline LISTENER");
					String msg = inFromClient.readLine();
					System.out.println("after readline LISTENER");
					if(!run){ return;}										//já deu timeout
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
			future.cancel(false);							// para dar reset ao timeout
			future = Server.getScheduler().scheduleAtFixedRate( peertimeout, 1100, 1100, TimeUnit.MILLISECONDS );
			break;

			case "BACKUP":
			System.out.println("client wanna do a BACKUP");
			break;

		}

	}

}
