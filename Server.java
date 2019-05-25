import java.net.InetAddress;

public class Server {

	private InetAddress address;
	private port;

	public Server ( String ipAddress ) {

		address = InetAddress.getByName( ipAddress );

	}

	// ip
	// tamanho do cluster
	public void main ( String args[] ) {

   		if( args.length != 2 ) {

   	 		System.out.println( "WRONG No OF ARGUMENTS" );
   	 		return;

   	 	}

        Server server = new Server( args[0], args[1] );

        Thread listener = new Thread( new ServerListener() );

        listener.start();

	}

}