package simulator;

public class Message {

	String[] contents;
	
	public Message(String ... contents) {
		this.contents = contents;
	}
	
	public String getType() {
		return contents[0];
	}
	
	public String[] getContents() {
		return contents;
	}
}
