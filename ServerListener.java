import java.net.*;
import java.io.*;

public class ServerListener implements Runnable {

	private ServerSocket socket;

	public ServerListener (int port, InetAddress address) {

		try {socket = new ServerSocket(port, 50, address);} catch (IOException e){ e.printStackTrace();}

	}

	public void run () {

		try {

			while(true) {

				System.out.println("Server listening...");

				Socket connectionSocket = socket.accept();
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				String receivedmessage = inFromClient.readLine();
				System.out.println("Received: " + receivedmessage);
				//capitalizedSentence = clientSentence.toUpperCase() + 'n';
				outToClient.writeBytes(receivedmessage);

				long start = System.nanoTime();    
				handledMessage(receivedmessage);
				System.out.println(System.nanoTime() - start);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handledMessage(String receivedmessage) {

		

	}

}