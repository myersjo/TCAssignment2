package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.InetAddress;

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
	static enum PacketType {
		REGULAR, TO_TYPE, DV_UPDATE, LS_UPDATE, PING
	};
//	static enum DeviceType {
//		CLIENT, ROUTER, FRIDGE, TV
//	};
	class Header {
		// type
		private PacketType packetType;
		// source info
		private InetAddress src;
		private String clientName;
		private String familyName;
		// destination info
		private InetAddress dst;
		private DeviceType dstType;
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
	}
	
}