import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.Exception;

public class HeartBeat implements Runnable {

	private HashMap<InetAddress,Integer> peers;

	public HeartBeat (HashMap<InetAddress,Integer> peers) {
		this.peers = peers;
	}

	public void run () {

		//System.out.println("HeartBeat");
/*
		String sentence;

		try {
			for (Map.Entry<InetAddress, Integer> entry : peers.entrySet()) {	// 1 thread por mensagem enviada?
			    System.out.println(entry.getKey() + " = " + entry.getValue());

				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				Socket clientSocket = new Socket(entry.getKey(), entry.getValue());
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				sentence = inFromUser.readLine();
				outToServer.writeBytes(sentence + 'n');
				//modifiedSentence = inFromServer.readLine();
				//System.out.println("FROM SERVER: " + modifiedSentence);
				clientSocket.close();
				
			}
		} catch(IOException e) {
			e.printStackTrace();
		}*/

	}

}