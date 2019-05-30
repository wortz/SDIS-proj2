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

	SSLSocket socket;
	Timer timer;
	Thread peertimeout;
	ScheduledFuture<?> future;
	Boolean peerOn;
	String msg;
	
	public ServerMessageHandler( String msg ) {
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

	private void handleBackup( String file_path, int rd ) {

		Server.getPeersOn();

	}

}