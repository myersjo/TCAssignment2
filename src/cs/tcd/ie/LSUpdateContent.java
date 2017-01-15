package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LSUpdateContent extends PacketContent {

	// add private member variables here as needed - i.e. content of packet
	// e.g. private String Message;
	// generate getters and setters

	public LSUpdateContent() {
	}

	public LSUpdateContent(ObjectInputStream oin) {
		this.header = new Header(oin);
		// use setters to set member variables read from InputStream
		// e.g. this.setMessage(oin.readUTF());
		// they must be read in the same order as they were written below
	}

	@Override
	protected void toObjectOutputStream(ObjectOutputStream out) {
		try {
			header.toObjectOutputStream(out);
			// write all member variables to OutputStream
			// e.g. out.writeUTF(getMessage());
			// the order here must be the same as the order above
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
