package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NewClientContent extends PacketContent {

	public NewClientContent () {
	}
	
	public NewClientContent (ObjectInputStream oin) {
		this.header = new Header (oin);
	}
	
	@Override
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			header.toObjectOutputStream(oout);
		}
		catch(Exception e) {e.printStackTrace();}
	}

}
