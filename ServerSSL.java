import java.io.IOException;
import java.net.ServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class ServerSSL
{
	//argv[0] portNumber
	public static void main(String [] arstring) throws IOException
	{
		System.setProperty("javax.net.ssl.keyStore","sdis.store");
		System.setProperty("javax.net.ssl.keyStorePassword","password");
		ServerSocket serverSocket = ((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).createServerSocket(Integer.parseInt(arstring[0]));
		System.out.println("Server up & ready for connections");
		
		while(true) {
			new ServerThread(serverSocket.accept()).start();
		}
	}
}