package utility;

public class Message implements java.io.Serializable {

	public String header;
	public byte[] body;

	public Message(String header, byte[] body) {

		this.header = header;
		this.body = body;

	}

}