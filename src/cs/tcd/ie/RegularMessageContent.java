package cs.tcd.ie;
/**
 * @Author Jordan Myers 
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegularMessageContent extends PacketContent {

	private String message;
	private String dstFamilyName;
	private String dstClientName;

	public RegularMessageContent() {
	}

	public RegularMessageContent(ObjectInputStream oin) {
		this.header = new Header(oin);
		try {
			this.setMessage(oin.readUTF());
			this.setDstFamilyName(oin.readUTF());
			this.setDstClientName(oin.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			header.toObjectOutputStream(oout);
			oout.writeUTF(getMessage());
			oout.writeUTF(getDstFamilyName());
			oout.writeUTF(getDstClientName());
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

	public String getDstFamilyName() {
		return dstFamilyName;
	}

	public void setDstFamilyName(String dstFamilyName) {
		this.dstFamilyName = dstFamilyName;
	}

	public String getDstClientName() {
		return dstClientName;
	}

	public void setDstClientName(String dstClientName) {
		this.dstClientName = dstClientName;
	}

}
