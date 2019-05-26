package peer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Peer {

    private static PeerServer peerServer;
    private static ScheduledExecutorService exec;

    public Peer(String[] args){
        exec = new ScheduledThreadPoolExecutor(10);

        String address = args[0];
        int port = Integer.parseInt(args[1]);
        peerServer = new PeerServer(address,port);
        exec.execute(peerServer);
        exec.execute(new Sender());

    }

    


    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Error, Usage: Peer peer_address peer_port");
        }
        new Peer(args);
    }

}