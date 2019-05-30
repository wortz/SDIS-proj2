package utility;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Utility {
    public static int MAX_WAIT_TIME = 400;
  
    public static int PUTCHUNK_TRIES = 5;
  
    public static String CRLF = "\r\n";
  
    public static int INITIAL_WAIT_TIME = 1000;
  
    public static int DELETE_TRIES = 5;
  
    public static int CHUNK_SIZE = 64000;
  
    public static long CAPACITY = 8000000;
  
    public static final String getFileSHA(File file) {
  
      String fileUniqueStr = file.getName() + file.lastModified();
  
      try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(fileUniqueStr.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while (hashtext.length() < 32) {
          hashtext = "0" + hashtext;
        }
  
        return hashtext;
      } catch (NoSuchAlgorithmException e) {
        System.out.println("Exception thrown" + " for incorrect algorithm: " + e);
  
        return null;
      }
    }
  
    public static int getRandomValue(int value){
      Random gerador = new Random();
      return gerador.nextInt(value+1);
    }
}