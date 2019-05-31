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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.*;

public class ServerMessageHandler implements Runnable {

	//	BACKUPIPS file_name 2 IP1 porta1 IP2 porta2 ...

	SSLSocket socketTopeer;
	Timer timer;
	Thread peertimeout;
	ScheduledFuture<?> future;
	Boolean peerOn;
	String msg;
	
	public ServerMessageHandler( String msg, SSLSocket socketTopeer) {
		this.socketTopeer = socketTopeer;
		this.msg = msg;
	}
	
	public void run() {

		System.out.println("new MSG HANDLER");

		String[] params = msg.split(" ");

		switch (params[0]) {
			case "BACKUP":
				handleBackup( params[1], Integer.parseInt(params[2]) );
				break;
		}

	}

	private int handleBackup( String file_path, int rd ) {

		ArrayList<String> availablePeers = Server.getPeersOn();
		int npeers = availablePeers.size();

		System.out.println("availablePeers : " + availablePeers + "size: " + npeers);

		String message = "BACKUPIPS " + file_path + " " + rd;

		try {

	        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
	        //outToServer.writeBytes("BACKUPIPS "'\n');


			if( npeers < rd ) {
				System.out.pritln(" Not enough available peers ");
				outToServer.writeBytes("ERROR - Not enough peers available for specified replication degree");
				return 0;
			}

			HashMap<String, Map.Entry<Integer, SSLSocket>> peers = Server.getPeers();

			for (int i = 0 ; i < rd ; i++ ) {

				String addr = availablePeers.get(i);
				Integer port = peers.get(addr).getKey();

				try {
					message += " ";
					message += addr;
					message += " ";
					message += port;

				} catch(NullPointerException e) {
					e.printStackTrace();
				}

			}

			System.out.println("MESSAGE : " + message);

			outToServer.writeBytes(message);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return 1;

	}

}