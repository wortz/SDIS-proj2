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

public class ClientSSL
{
	//argv[0] portNumber
	//argv[1] clientName
	public static void main(String [] arstring) throws UnknownHostException, IOException
	{
		System.setProperty("javax.net.ssl.trustStore","sdis.store");
		System.setProperty("javax.net.ssl.trustStorePassword","password");
		//System.setProperty("javax.net.debug","all");
		Security.addProvider(new Provider());
		SSLSocket socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket("localhost",Integer.parseInt(arstring[0]));
		BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
		//PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);

        //socket.startHandshake();

		outToServer.writeBytes("test message " + "\n");

		outToServer.writeBytes("test message 2" + "\n");
		System.out.println(socketBufferedReader.readLine());

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