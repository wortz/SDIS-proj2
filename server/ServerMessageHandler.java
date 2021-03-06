package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.ssl.*;
import javax.net.ssl.SSLSocket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMessageHandler implements Runnable {

	//	BACKUPIPS file_name 2 IP1 porta1 IP2 porta2 ...

	SSLSocket socketTopeer;
	Timer timer;
	Thread peertimeout;
	ScheduledFuture<?> future;
	Boolean peerOn;
	String msg;
	String peerAddress;
	int peerPort;
	
	public ServerMessageHandler( String msg, SSLSocket socketTopeer, String peerAddress, int peerPort) {
		this.peerPort = peerPort;
		this.peerAddress = peerAddress;
		this.socketTopeer = socketTopeer;
		this.msg = msg;
	}
	
	public void run() {

		System.out.println("new MSG HANDLER");

		String[] params = msg.split(" ");
		


		System.out.println(msg);

		switch (params[0]) {
			case "BACKUP":
				handleBackup( params[1], Integer.parseInt(params[2]), "hash");
				break;
			case "RESTORE":
				handleRestore( params[0]);
				break;
		}

	}

	private int handleRestore( String file_path ) { // resotreips hash ip_peer ip_port

		ConcurrentHashMap<String, ArrayList<String>> filesPeers = Server.getFilesPeers();
		ArrayList<String> peerFiles = (ArrayList<String>) filesPeers.get(file_path);
		String selectedPeer = peerFiles.get(0);
		String[] params = selectedPeer.split(" ");

		String message = "RESTOREIPS " + params[2] + " " + params[0] + " " + params[1];

		DataOutputStream outToPeer;
		try {

			outToPeer = new DataOutputStream(socketTopeer.getOutputStream());

			outToPeer.writeBytes(message + '\n');

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;

	}

	private int handleBackup( String file_path, int rd, String fileHash) { 

		System.out.println("availafwfwfwefewf ");

		ArrayList<String> availablePeers = (ArrayList<String>) Server.getPeersOn().clone();
		availablePeers.remove(peerAddress + ":" + peerPort);
		int npeers = availablePeers.size();

		System.out.println("availablePeers : " + availablePeers + "size: " + npeers);

		String message = "BACKUPIPS " + file_path + " " + rd;
		ArrayList<String> futureBackups = new ArrayList();


		try {

	        DataOutputStream outToPeer = new DataOutputStream(socketTopeer.getOutputStream());
	        //outToServer.writeBytes("BACKUPIPS "'\n');


			if( npeers < rd ) {
				System.out.println(" Not enough available peers ");
				outToPeer.writeBytes("ERROR - Not enough peers available for specified replication degree\n");
				return 0;
			}

			ConcurrentHashMap<String, Map.Entry<Integer, SSLSocket>> peers = Server.getPeers();

			for (int i = 0 ; i < rd ; i++ ) {

				String addr = availablePeers.get(i).split(":")[0];
				System.out.println("peers " + peers);
				System.out.println("peers " + peers.get(addr));

				Integer port = peers.get(addr).getKey();

				futureBackups.add(addr + ":" + port + ":" +fileHash);

				try {
					message += " ";
					message += addr;
					message += " ";
					message += port;

				} catch(NullPointerException e) {
					e.printStackTrace();
				}

			}
			Server.getFilesPeers().put(file_path, futureBackups);
			
			System.out.println("HASH MAP IS HERE" + Server.getFilesPeers());

			System.out.println("MESSAGE : " + message);

			outToPeer.writeBytes(message + '\n');

		} catch (IOException e) {
			e.printStackTrace();
		}

		return 1;

	}

}