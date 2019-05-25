
import java.util.concurrent.ScheduledExecutorService;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.time.*;
import java.io.*;
import java.net.*;
import java.lang.Exception;

public class ServerPeer {

	private InetAddress address;
	private int port;
	private int nServers = 5;
    private ScheduledExecutorService scheduler;
    private Thread electionTimeoutThread;
    public HashMap<InetAddress, Integer> friendPeers;

    public static ServerPeer peer_instance;	// fazer synchronized ???

    private State state;

    enum State {
  		FOLLOWER,
  		CANDIDATE,
  		LEADER
	}

	public ServerPeer ( String ipAddress, int port ) {

		state = State.FOLLOWER;

		try {address = InetAddress.getByName( ipAddress );} catch (UnknownHostException e){e.printStackTrace();}
		this.port = port;
		friendPeers = new HashMap<InetAddress,Integer>();

		scheduler = Executors.newScheduledThreadPool(2);
		startElectionTimeout();
		startHeartBeat();

		createListener();

	}

	public ServerPeer ( String ipAddress, int port, String ipAddressDest, int portDest ) {

		state = State.FOLLOWER;

		InetAddress addressDest = null;

		try {
			address = InetAddress.getByName( ipAddress );
			addressDest = InetAddress.getByName( ipAddressDest );
		} catch (UnknownHostException e){e.printStackTrace();}
		this.port = port;
		friendPeers = new HashMap<InetAddress,Integer>();

		tryToRegistry( addressDest, portDest );
		//startHeartBeat();

		createListener();

	}

	private void startElectionTimeout() {

		ScheduledFuture<?> result = scheduler.scheduleAtFixedRate(new Thread( new Runnable() {public void run (){
			System.out.println("created thread");
		}} ), 1000, 1000, TimeUnit.MILLISECONDS);

	}

	private void tryToRegistry( InetAddress addressDest, int portDest ) {

		try {

		Socket clientSocket = new Socket( addressDest, portDest );
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes("REGISTRY " + address + ' ' + port + "\n");
		String response = inFromServer.readLine();
		System.out.println("FROM SERVER: " + response);
		clientSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void createListener() {

        Thread listener = new Thread( new ServerListener(port, address));
        listener.start();

	}

	public void startHeartBeat () {

		int period = new Random().nextInt(150 + 1)  + 150;	// 150 a 300 ms
		ScheduledFuture<?> result = scheduler.scheduleAtFixedRate(new Thread(new HeartBeat(friendPeers)), period, period, TimeUnit.MILLISECONDS);

	}

	public void sendMessage( String message ) {

		try {

		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", port);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		//sentence = inFromUser.readLine();
		outToServer.writeBytes(message + '\n');
		String response = inFromServer.readLine();
		System.out.println("FROM SERVER: " + response);
		clientSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ip
	// port
	// tamanho do cluster
	public static void main( String[] args ) {

   	 	if ( args.length == 2 ) 
        	peer_instance = new ServerPeer( args[0], Integer.parseInt( args[1]) );
    	else if ( args.length == 4 )
        	peer_instance = new ServerPeer( args[0], Integer.parseInt( args[1]), args[2],  Integer.parseInt( args[3]) );
        else {
        	System.out.println( "WRONG No OF ARGUMENTS" );
        	return;
        }
	}

}