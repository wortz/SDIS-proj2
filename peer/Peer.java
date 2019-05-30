package peer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Peer {

    private static PeerServer peerServer;
    private static ScheduledExecutorService exec;
    private static RegisterServer server;

    public Peer(String[] args){
        exec = new ScheduledThreadPoolExecutor(10);
        peerServer = new PeerServer(args[0],Integer.parseInt(args[1]));
        //server = new RegisterServer();
        exec.execute(peerServer);
        exec.execute(new Sender());
        //exec.scheduleWithFixedDelay(server,500,500,TimeUnit.MILLISECONDS);
    
        
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

    


    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Error, Usage: Peer peer_address peer_port");
            return;
        }
        new Peer(args);
    }

}