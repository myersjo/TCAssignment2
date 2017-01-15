package cs.tcd.ie;
/**
 * @Author Jordan Myers 
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ToTypeMessageContent extends PacketContent {

	private String message;
	
	public ToTypeMessageContent() {	
	}
	
	public ToTypeMessageContent (ObjectInputStream oin) {
		this.header = new Header (oin);
		try {
			this.message = oin.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			header.toObjectOutputStream(oout);
			oout.writeUTF(getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
