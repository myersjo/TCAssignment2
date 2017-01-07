package cs.tcd.ie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegularMessageContent extends PacketContent {

	private String message;

	public RegularMessageContent() {
	}

	public RegularMessageContent(ObjectInputStream oin) {
		this.header = new Header(oin);
		try {
			this.setMessage(oin.readUTF());
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
