package utility;

public class Message implements java.io.Serializable {

	private String header;
	private byte[] body;

	public Message(String header){
		this.header=header;
	}

	public Message(String header, byte[] body) {

		this.header = header;
		this.body = body;

	}

	public String getHeader() { return header; }
	public byte[] getBody() { return body; }

}