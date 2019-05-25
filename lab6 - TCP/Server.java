import java.io.*;
import java.net.*;

public class Server
{

   private int port;
   private ServerSocket serversocket;
   private Socket socket;

	public Server(int port) throws IOException{

      this.port = port;

      this.createServerSocket();

      this.acceptRequest();

      this.writeInSocket();

   }

   private void acceptRequest(){

      try {

         System.out.println("Waiting for request...");

         this.socket = this.serversocket.accept();

      } catch (IOException e) {

         System.err.println("Accept failed: " + this.port);
         System.exit(1);
      
      }

      System.out.println("Accepted");

   }  

   private void createServerSocket(){

      try {

         this.serversocket = new ServerSocket(this.port);

      } catch (IOException e) {

      System.out.println("Could not listen on port: " + port);
      System.exit(-1);

      }
   }

   private void writeInSocket() throws IOException {

      PrintWriter out = null;
      BufferedReader in = null;

      String receivedMessage;

      in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      out = new PrintWriter(this.socket.getOutputStream(), true);

      receivedMessage = in.readLine();

      System.out.println(receivedMessage);

      out.println(receivedMessage);

      out.close();
      in.close();
      this.socket.close();
      this.serversocket.close();

   }


   public static void main(String args[]) throws Exception
   {

   		if(args.length != 1){
   	 		System.out.println("WRONG No OF ARGUMENTS");
   	 		return;
   	 	}

         Server server = new Server(Integer.parseInt(args[0]));

         server.listen();

   }

   private void listen() throws Exception {

      while(true) {
         System.out.println("looping...");
      }

   }

}