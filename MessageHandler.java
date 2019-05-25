import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandler implements Runnable {

    byte[] packet;
    BufferedReader reader;
    String fileID;
    double version;
    String protocol;
    int senderID;
    String[] params;


    public MessageHandler(byte[] packet) {

        this.packet = packet;

        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(packet)));

    }

    public void run() {

        try {

            String header = reader.readLine();

            params = header.split(" ");

            type = params[0];

            Pattern r = Pattern.compile("\\((\d*.)*\d+)");
            Matcher m = r.matcher(params[1]);

            if (m.find( )) {
                 System.out.println("Found value: " + m.group(0) );
              } else {
                 System.out.println("NO MATCH");
              }

            ip = m.group[0];

            port = params[2];




            ip = Double.parseDouble(params[1]);
            port = Integer.parseInt(params[2]);

            Peer.logger.info("MESSAGE IS " + params[0] + "\n");

            if(senderID == Peer.getID())
                return;

            fileID = params[3];

            switch(params[0]) {
                case "PUTCHUNK":

                    int chunkNO = Integer.parseInt(params[4]);
                    int rd = Integer.parseInt(params[5]);

                    handlePutchunk(chunkNO, rd);

                    break;
                case "STORED":

                    chunkNO = Integer.parseInt(params[4]);

                    handleStored(chunkNO, senderID);

                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handlePutchunk(int chunkNO, int rd) {

        byte[] response = createStoredMessage(chunkNO);

        int rn = Peer.random.nextInt(400);

        try {

            Thread.sleep(rn);

        } catch(Exception e) {
            e.printStackTrace();
        }

        Peer.getDB().createFile(fileID, version, protocol, chunkNO, rd, readBody());

        Peer.getMC().sendMessage(response, "STORED");

    }

    private void handleStored(int chunkNO,int senderID) {

        Peer.getDB().addSMR(senderID);

    }

    private byte[] createStoredMessage(int chunkNO){

        String message = "STORED " + version + " " + Peer.getInstance().getID() + " " + fileID + " " + Integer.toString(chunkNO) + " \r\n\r\n";

        byte[] msgBytes = message.getBytes();

        return msgBytes;

    }

    private byte[] readBody() {     //  ferrolho

        ByteArrayInputStream stream = new ByteArrayInputStream(this.packet);
        BufferedReader treader = new BufferedReader(new InputStreamReader(stream));

        String bodyStr = "";
        String line = "";

        int headerLinesLengthSum = 0;
        int numLines = 0;
        byte[] body;


        try {


            String line1 = treader.readLine();
            headerLinesLengthSum += line1.getBytes().length;
            String line2 = treader.readLine();
            headerLinesLengthSum += line2.getBytes().length;


        } catch(Exception e) {
            e.printStackTrace();
        }

        String crlf = "\r\n\r\n";

        int bodyStartIndex = headerLinesLengthSum + crlf.getBytes().length; // 4

        body = Arrays.copyOfRange(packet, bodyStartIndex, packet.length);

        return body;

    }

}