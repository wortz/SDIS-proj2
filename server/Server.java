package server;

import java.util.concurrent.ScheduledExecutorService;
import java.util.Random;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
import java.time.*;
import java.io.*;
import java.net.*;
import java.lang.Exception;
import java.net.ServerSocket;
import javax.net.ssl.*;
import java.security.Security;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class Server {

	private InetAddress address;
	private int port;
    private static ScheduledExecutorService scheduler;
    private Thread electionTimeoutThread;
    private SSLServerSocket serverSocket;
    private static ConcurrentHashMap<String, Map.Entry<Integer, SSLSocket>> peers;	// ip,	port, socket
	
	//for a file ID saves the peers it is backed up 
	private static ConcurrentHashMap<String, ArrayList<String>> filesPeers;	// File, ip, port	
	private static ArrayList<String> peersOn;
    //private ServerSSL sslServer;

    private static Server server_instance;	// fazer synchronized ???

    public Server () {}
	public Server ( String ipAddress, int port ) {

		peers = new ConcurrentHashMap<String, Map.Entry<Integer, SSLSocket>>();
		filesPeers = new ConcurrentHashMap<String, ArrayList<String>>();
		peersOn = new ArrayList<String>();

		try {address = InetAddress.getByName( ipAddress );} catch (UnknownHostException e){e.printStackTrace();}
		this.port = port;

		//sslServer = new SSlServer(port, address);

		scheduler = Executors.newScheduledThreadPool(10);
		//startElectionTimeout();
		//startHeartBeat();

		//createListener();

		if( SSlAccepter( port, address ) == -1 )
			System.out.println(" Error creating SSLSocket");

	}

	public synchronized void addPeer( String address, int port, SSLSocket socket ) {

		System.out.println("AddPeer");

		InetAddress addr = null;
		try {addr = InetAddress.getByName( address );} catch (UnknownHostException e){e.printStackTrace();}
		peers.put(address, new SimpleEntry<Integer,SSLSocket>(port, socket));
		peersOn.add(address);
		System.out.println(peers);
		System.out.println(peersOn);
		new Thread( new Listener(socket, address)).start();

	}

	private int SSlAccepter( int port, InetAddress address ) {

		System.setProperty("javax.net.ssl.keyStore","../server.keys");
		System.setProperty("javax.net.ssl.keyStorePassword","123456");
        System.setProperty("javax.net.ssl.trustStore","../truststore");
        System.setProperty("javax.net.ssl.trustStorePassword","123456");
		//System.setProperty("javax.net.debug","all");
		try {
			serverSocket = (SSLServerSocket)((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).createServerSocket(port, 50, address);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}

		System.out.println("Server up & ready for connections");

        //serverSocket.startHandshake();
		
		try {

			while(true) {

				new ServerThread((SSLSocket) serverSocket.accept()).start();
				System.out.println("accepted connection");

			}

		} catch( IOException e){
			e.printStackTrace();
		}

		return 1;

	}

	private void startElectionTimeout() {

		scheduler.scheduleAtFixedRate(new Thread( new Runnable() {public void run (){
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

/*
	private void createListener() {		 // 1 listener por cada peer conectado. Eficiente??

        Thread listener = new Thread( new ServerListener(port, address));
        listener.start();

	}
*/

	public void startHeartBeat () {

		int period = new Random().nextInt(150 + 1)  + 150;	// 150 a 300 ms
		//ScheduledFuture<?> result = scheduler.scheduleAtFixedRate(new Thread(new HeartBeat(friendPeers)), period, period, TimeUnit.MILLISECONDS);

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

	public static void peerTimeout(String address) {

		peersOn.remove(peersOn.indexOf(address));
		System.out.println("Peer removed : " + peersOn);

	}

	public static synchronized void removePeer(String address) {

		String addr = address.toString();
		System.out.println("addr : " + addr);

		System.out.println("pers before remove" + peers);
		peers.remove(addr);
		System.out.println("pers after remove" + peers);

	}

	// static method to create instance of Peer class
	public static Server getInstance() {
		if( server_instance == null ){
			server_instance = new Server();
		}
		return server_instance;
	}

	public static ScheduledExecutorService getScheduler() {
		return scheduler;
	}

	public static synchronized ConcurrentHashMap<String, Map.Entry<Integer, SSLSocket>> getPeers() { return peers; }

	public static synchronized ArrayList<String> getPeersOn() { return peersOn; }

	/**
	 * @return the filesPeers
	 */
	public static synchronized ConcurrentHashMap<String, ArrayList<String>> getFilesPeers() {
		return filesPeers;
	}

	public static void main( String[] args ) {
   	 	if ( args.length == 2 ) {
    		server_instance = new Server( args[0], Integer.parseInt( args[1]) );
   	 	}
        else {
			System.out.println("ERROR - Format must be: Server <ServerIP> <ServerPort>");
        	return;
        }
	}
}