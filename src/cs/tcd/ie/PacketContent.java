package cs.tcd.ie;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

//public interface PacketContent {
//	
//	public static byte HEADERLENGTH = 10;
//	public static byte PAYLOAD_LENGTH = 2;
//	public static int SEQUENCE_NUMBER = 0;
//	
//	public String toString();
//	public DatagramPacket toDatagramPacket();
//}

public abstract class PacketContent {
	// public class PacketContent {
	// static enum PacketType {
	// REGULAR, TO_TYPE, DV_UPDATE, LS_UPDATE, PING, NEW_CLIENT
	// };
	class Header {
		// type
		private PacketType packetType;
		// source info
		private InetAddress src;
		private int srcPort;
		private String clientName;
		private String familyName;
		// destination info
		private InetAddress dst;
		private DeviceType dstType;

		protected Header() {
		}

		protected Header(ObjectInputStream oin) {
			try {
				int type = oin.readInt();
				if (type == PacketType.NEW_ROUTER.ordinal()) {
					packetType = PacketType.NEW_ROUTER;
				} else if (type == PacketType.NEW_CLIENT.ordinal()) {
					packetType = PacketType.NEW_CLIENT;
				} else if (type == PacketType.PING.ordinal()) {
					packetType = PacketType.PING;
				} else if (type == PacketType.LS_UPDATE.ordinal()) {
					packetType = PacketType.LS_UPDATE;
				} else if (type == PacketType.DV_UPDATE.ordinal()) {
					packetType = PacketType.DV_UPDATE;
				} else if (type == PacketType.TO_TYPE.ordinal()) {
					packetType = PacketType.TO_TYPE;
				} else if (type == PacketType.REGULAR.ordinal()) {
					packetType = PacketType.REGULAR;
				}

				// src = InetAddress.getByAddress((byte[]) oin.readObject());
				src = InetAddress.getByName(oin.readUTF());
				srcPort = oin.readInt();
				clientName = oin.readUTF();
				familyName = oin.readUTF();
				// dst = InetAddress.getByAddress((byte[]) oin.readObject());
				dst = InetAddress.getByName(oin.readUTF());

				int dsType = oin.readInt();
				if (dsType == DeviceType.CLIENT.ordinal()) {
					dstType = DeviceType.CLIENT;
				} else if (dsType == DeviceType.ROUTER.ordinal()) {
					dstType = DeviceType.ROUTER;
				} else if (dsType == DeviceType.FRIDGE.ordinal()) {
					dstType = DeviceType.FRIDGE;
				} else if (dsType == DeviceType.TV.ordinal()) {
					dstType = DeviceType.TV;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public PacketType getPacketType() {
			return packetType;
		}

		public void setPacketType(PacketType packetType) {
			this.packetType = packetType;
		}

		public InetAddress getSrc() {
			return src;
		}

		public void setSrc(InetAddress src) {
			this.src = src;
		}

		public int getSrcPort() {
			return srcPort;
		}

		public void setSrcPort(int port) {
			this.srcPort = port;
		}

		public String getClientName() {
			return clientName;
		}

		public void setClientName(String clientName) {
			this.clientName = clientName;
		}

		public String getFamilyName() {
			return familyName;
		}

		public void setFamilyName(String familyName) {
			this.familyName = familyName;
		}

		public InetAddress getDst() {
			return dst;
		}

		public void setDst(InetAddress dst) {
			this.dst = dst;
		}

		public DeviceType getDstType() {
			return dstType;
		}

		public void setDstType(DeviceType dstType) {
			this.dstType = dstType;
		}

		/**
		 * If values are null, they are set to a default value
		 * 
		 * @param oout
		 */
		protected void toObjectOutputStream(ObjectOutputStream oout) {
			try {
				if (packetType == null)
					packetType = PacketType.REGULAR;
				oout.writeInt(packetType.ordinal());
				if (src == null)
					src = InetAddress.getByName("localhost");
				// oout.write(src.getAddress());
				oout.writeUTF(src.getHostAddress());
				oout.writeInt(srcPort);
				if (clientName == null)
					clientName = "null";
				oout.writeUTF(clientName);
				if (familyName == null)
					familyName = "null";
				oout.writeUTF(familyName);
				if (dst == null)
					dst = InetAddress.getByName("localhost");
				// oout.write(dst.getAddress());
				oout.writeUTF(dst.getHostAddress());
				if (dstType == null)
					dstType = DeviceType.CLIENT;
				oout.writeInt(dstType.ordinal());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Header header;

	public PacketContent() {
		this.header = new Header();
	}

	/**
	 * This method is used to transform content into an output stream.
	 * 
	 * @param out
	 *            Stream to write the content for the packet to.
	 */
	protected abstract void toObjectOutputStream(ObjectOutputStream out);

	/**
	 * Returns the content of the object as a datagram packet
	 */
	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet = null;

		try {
			ByteArrayOutputStream bout;
			ObjectOutputStream oout;
			byte[] data;

			bout = new ByteArrayOutputStream();
			oout = new ObjectOutputStream(bout);

			oout.writeInt((header.packetType.ordinal())); // write type to
															// stream
			toObjectOutputStream(oout); // write content to stream depending on
										// type

			oout.flush();
			data = bout.toByteArray(); // convert content to byte array

			packet = new DatagramPacket(data, data.length); // create packet
															// from byte array
			oout.close();
			bout.close();
		} catch (Exception e) {

		}

		return packet;
	}

	/**
	 * Constructs an object from a datagram packet
	 * 
	 * @param packet
	 * @return
	 */
	public static PacketContent fromDatagramPacket(DatagramPacket packet) {
		PacketContent content = null;

		try {

			byte[] data;
			ByteArrayInputStream bin;
			ObjectInputStream oin;

			data = packet.getData(); // use packet content as seed for stream
			bin = new ByteArrayInputStream(data);
			oin = new ObjectInputStream(bin);

			// Header header = content.new Header (oin);
			final int type = oin.readInt();

			if (type == PacketType.NEW_CLIENT.ordinal()) {
				content = new NewClientContent(oin);
			} else if (type == PacketType.NEW_ROUTER.ordinal()) {
				content = new NewRouterContent(oin);
			} else {
				content = null;
			}
			// TODO: Add additional packet types

			oin.close();
			bin.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

}