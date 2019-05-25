import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client
{

	private Socket socket;
	private String name;
	private int port;
	private InetAddress address;

	public Client(String name, int port) throws Exception {

		this.port = port;

    	this.createSocket(name, port);

	}

/*
	java Client <host_name> <port_number> <oper> <opnd> *

	where:
	<host_name> is the name of the host where the server runs;
	<port_number> is the port number where the server provides the service ;
	<oper> is ''register'' or ''lookup'', depending on the operation to invoke;
	<opnd> * is the list of operands of the specified operation:
	<plate number> <owner name>, for register;
	<plate number>, for lookup.P
*/

	private void createSocket(String name, int port) throws Exception {

      	InetAddress address = InetAddress.getByName(name);

      	/*Pattern pattern = Pattern.compile("/(.*)");
        Matcher matcher = pattern.matcher(address);
        String ip;

        if(matcher.find()) ip = matcher.group(1);
        else System.out.println("NOT FOUND match");*/

      	System.out.println("Creating socket " + address + ' ' + port);

      	try {
			this.socket = new Socket(address, port);
		} catch (Exception e) {

			e.printStackTrace();
		    System.exit(1);
	      
	    }

		System.out.println("Created Socket at port " + port);

	}

	private void sendData(String data) throws IOException {
		String receivedData;

		DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());

		out.writeBytes(data + '\n');

		BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

		receivedData = in.readLine();

		System.out.println("Data from server: " + receivedData);

  		this.socket.close();

	}

    public static void main(String args[]) throws Exception
    {

    	if(args.length < 4){
    		System.out.println("WRONG No OF ARGUMENTS");
    		System.exit(-1);
    	}

    	Client client = new Client(args[0], Integer.parseInt(args[1]));

    	client.sendData("teste data\n");

   }
}