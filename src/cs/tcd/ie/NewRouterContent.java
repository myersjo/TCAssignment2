package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class NewRouterContent extends PacketContent {

	public NewRouterContent () {
	}
	
	public NewRouterContent (ObjectInputStream oin) {
		this.header = new Header (oin);
	}
	
	@Override
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			header.toObjectOutputStream(oout);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
