import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Security;
import java.io.*;
import com.sun.net.ssl.internal.ssl.Provider;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.*;
import java.net.InetAddress;
import java.io.DataInputStream;

public class ClientSSL
{
	//argv[0] portNumber
	//argv[1] clientName
	public static void main(String [] arstring) throws UnknownHostException, IOException
	{
		InetAddress address;
		try {address = InetAddress.getByName( arstring[0] );
			System.setProperty("javax.net.ssl.trustStore","sdis.store");
			System.setProperty("javax.net.ssl.trustStorePassword","password");
			//System.setProperty("javax.net.debug","all");
			Security.addProvider(new Provider());
			int port = Integer.parseInt(arstring[1]);
			SSLSocket socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(address, port);
			DataInputStream inFromServer = new DataInputStream(socket.getInputStream());
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
			//PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);


	        //socket.startHandshake();

	        InetAddress myaddress =  InetAddress.getByName( arstring[2] );
	        int myport = Integer.parseInt(arstring[3]);

			outToServer.writeBytes("Hi server. My IP and port are:" + myaddress + ":" + myport + '\n');

			System.out.println("teeest");
			System.out.println("Server response: " + inFromServer.readUTF());

			try{
				Thread.sleep(4000);
			} catch(Exception e){
				e.printStackTrace();
			}
			System.out.println("lets send");
			
			outToServer.writeBytes("BACKUP merda merda merda\n");
			
			//System.out.println("teeest2");

		} catch (UnknownHostException e){e.printStackTrace();}
		while(true){}

		//BufferedReader commandPromptBufferedReader = new BufferedReader(new InputStreamReader(System.in));
		/*
		printWriter.println(arstring[1]);
		String message = null;
		while(true) {
			System.out.println("Please enter a messsage to send to the server: ");
			if (message.equals("quit")) {
				socket.close();
				break;
			}
			printWriter.println(message);
			System.out.print("message repy from server:");
			System.out.println(socketBufferedReader.readLine());
		}
		*/
	}
}