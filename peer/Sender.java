package peer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import protocol.*;

public class Sender implements Runnable{

    @Override
    public void run() {
        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
        String request;
        try{
            while(true){
                System.out.println("Enter your request:");
                request=reader.readLine();
                String[] requestSplit = request.split(" ");
                if (requestSplit.length < 2 || requestSplit.length > 3){
                    System.out.println("Error: The format must be : <sub_protocol> <opnd_1> <opnd_2>");
                    continue;
                }
                switch(requestSplit[0]){
                    case "Backup":
                        if(requestSplit.length != 3){
                            System.out.println("Error: The format must be : BACKUP <filePath> <replicationDegree>");
                            break;
                        }
                        Peer.getExec().execute(new Backup(requestSplit[1],Integer.parseInt(requestSplit[2])));
                        break;
                    case "Restore":
                        if(requestSplit.length != 2){
                            System.out.println("Error: The format must be : RESTORE <filePath>");
                            break;
                        }
                        Peer.getServer().sendServerMessage();
                        break;
                    case "Delete":
                        if(requestSplit.length != 2){
                            System.out.println("Error: The format must be : DELETE <filePath>");
                            break;
                        }
                        Peer.getServer().sendServerMessage();
                        break;
                    case "Reclaim":
                        System.out.println("That protocol was not implemented.");
                        break;
                    default:
                        System.out.println("Not a valid protocol.");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    
}