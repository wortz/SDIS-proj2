import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;
import javax.net.ssl.SSLSocket;

public class ServerThread extends Thread{

	SSLSocket socket;
	
	ServerThread(SSLSocket socket) {
		this.socket=socket;
	}
	
	public void run() {
		System.out.println("in server thread");

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
        try {
        	socket.startHandshake();
        } catch (Exception e) {
        	e.printStackTrace();
        }

		try {

			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			System.out.println("user " + bufferedReader.readLine() + " is now connected to the server.\n");
			while (true) {
				printWriter.println(bufferedReader.readLine() + " echo");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
