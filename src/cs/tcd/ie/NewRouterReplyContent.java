package cs.tcd.ie;
/**
 * @Author Jordan Myers 
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NewRouterReplyContent extends PacketContent {
	
	private String routerName;

	public NewRouterReplyContent() {
	}
	public NewRouterReplyContent(ObjectInputStream oin) {
		this.header = new Header (oin);
		try {
			this.routerName = oin.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			header.toObjectOutputStream(oout);
			oout.writeUTF(routerName);
		}
		catch(Exception e) {e.printStackTrace();}
	}
	public String getRouterName() {
		return routerName;
	}
	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

}
