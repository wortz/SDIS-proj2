import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

public class ClientSSL
{
	//argv[0] portNumber
	//argv[1] clientName
	public static void main(String [] arstring) throws UnknownHostException, IOException
	{
		System.setProperty("javax.net.ssl.trustStore","sdis.store");
		Socket socket = ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket("localhost",Integer.parseInt(arstring[0]));
		BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader commandPromptBufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		printWriter.println(arstring[1]);
		String message = null;
		while(true) {
			System.out.println("Please enter a messsage to send to the server: ");
			message = commandPromptBufferedReader.readLine();
			if (message.equals("quit")) {
				socket.close();
				break;
			}
			printWriter.println(message);
			System.out.print("message repy from server:");
			System.out.println(socketBufferedReader.readLine());
		}
		
	}
}