import java.net.InetAddress;
import java.net.ServerSocket;

public class Peer {

    private static InetAddress peerAddress;
    private static ServerSocket peerSocket;

    public Peer(String[] args){
        String address = args[0];
        int port = Integer.parseInt(args[1]);
        try{
        peerAddress = InetAddress.getByName(address);
        peerSocket= new ServerSocket(port,10,peerAddress);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args){
        if(args.length < 3){
            System.out.println("Error, Usage: Peer peer_address peer_port");
        }
    }

}