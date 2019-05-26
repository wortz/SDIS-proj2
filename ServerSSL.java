import java.io.IOException;
import java.net.ServerSocket;
import javax.net.ssl.*;
import com.sun.net.ssl.internal.ssl.Provider;
import java.security.Security;
import java.net.InetAddress;

public class ServerSSL
{
	//argv[0] portNumber
	public static void main(int port, InetAddress address) throws IOException
	{
		Security.addProvider(new Provider());
		System.setProperty("javax.net.ssl.keyStore","sdis.store");
		System.setProperty("javax.net.ssl.keyStorePassword","password");
		//System.setProperty("javax.net.debug","all");
		SSLServerSocket serverSocket = (SSLServerSocket)((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).createServerSocket(port, 50, address);
		System.out.println("Server up & ready for connections");

        //serverSocket.startHandshake();
		
		while(true) {
			new ServerThread((SSLSocket) serverSocket.accept()).start();
			System.out.println("in while");
		}
	}
}