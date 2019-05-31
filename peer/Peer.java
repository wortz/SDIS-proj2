package peer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import storage.*;

public class Peer {

    private static PeerServer peerServer;
    private static ScheduledExecutorService exec;
    private static RegisterServer server;
    public static PeerStorage storage;
    private static String peerID;

    public Peer(String[] args){
        peerID=args[4];
        storage=new PeerStorage();
        exec = new ScheduledThreadPoolExecutor(99);
        peerServer = new PeerServer(args[0],Integer.parseInt(args[1]));
        server = new RegisterServer(args[2], Integer.parseInt(args[3]));
        exec.execute(peerServer);
        exec.execute(new Sender());
        //new Thread(server).start();
        //exec.execute(server);

        exec.scheduleWithFixedDelay(server,2000,2000,TimeUnit.MILLISECONDS);
        
    }



    public synchronized static PeerServer getPeerServer(){
        return peerServer;
    }

    public synchronized static ScheduledExecutorService getExec(){
        return exec;
    }

    public synchronized static RegisterServer getServer(){
        return server;
    }

    public static String getPeerID(){
        return peerID;
    }

    


    public static void main(String[] args){
        if(args.length < 6){
            System.out.println("Error, Usage: Peer peer_address peer_port server_ip server_port peerID");
            return;
        }
        new Peer(args);
    }

}