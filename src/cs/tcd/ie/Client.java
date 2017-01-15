/**
 * @Author Jordan Myers
 */
package cs.tcd.ie;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.io.IOException;

import tcdIO.*;

/**
 *
 * Client class
 * 
 * An instance accepts user input
 *
 */
public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";

	Terminal terminal;
	InetAddress clientIP;
	int clientPort;
	String clientName;
	DeviceType type;
	InetAddress routerIP;
	int routerPort;

	boolean initialised;

	/**
	 * Constructor
	 * 
	 * Attempts to create socket at given port and create an InetSocketAddress
	 * for the destinations
	 */
	Client(Terminal terminal, String ownIP, int ownPort, String ownName, String routerIP, int routerPort, String type) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(ownPort);
			this.clientIP = InetAddress.getByName(ownIP);
			this.clientPort = ownPort;
			this.clientName = ownName;
			this.routerIP = InetAddress.getByName(routerIP);
			this.routerPort = routerPort;

			if (type.toUpperCase().equals("PC"))
				this.type = DeviceType.PC;
			else if (type.toUpperCase().equals("TV") || type.toUpperCase().equals("TELEVISION")
					|| type.toUpperCase().equals("TELLY"))
				this.type = DeviceType.TV;
			else if (type.toUpperCase().equals("FRIDGE"))
				this.type = DeviceType.FRIDGE;
			else if (type.toUpperCase().equals("ROUTER")) {
				System.err.println("Please use the Server application for routers.");
				System.exit(1);
			} else {
				System.err.println(type + " is not a supported device type. Supported Devices: \n\t" + DeviceType.PC
						+ "\n\t" + DeviceType.TV + "\n\t" + DeviceType.FRIDGE);
				System.exit(1);
			}
			initialised = false;
			listener.go();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Device type defaults to PC if no type is specified
	 * 
	 * @param terminal
	 * @param ownIP
	 * @param ownPort
	 * @param ownName
	 * @param routerIP
	 * @param routerPort
	 */
	Client(Terminal terminal, String ownIP, int ownPort, String ownName, String routerIP, int routerPort) {
		this(terminal, ownIP, ownPort, ownName, routerIP, routerPort, "PC");
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			if (packet == null)
				return;

			PacketContent content = PacketContent.fromDatagramPacket(packet);
			System.out.println("Client onReceipt: " + content.header.getPacketType() + " from: "
					+ ((content.header.getFamilyName() == null) ? content.header.getClientName()
							: content.header.getFamilyName()));

			switch (content.header.getPacketType()) {
			case PING:
				onRecPing(packet);
				break;
			case TO_TYPE:
				onRecToType(packet);
				break;
			case REGULAR:
				onRecRegular(packet);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void onRecPing(DatagramPacket packet) {

	}

	private void onRecToType(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		if (content.header.getDstType() == this.type)
			onRecRegular(packet);
	}

	private void onRecRegular(DatagramPacket packet) {
		try {
			RegularMessageContent content = (RegularMessageContent) PacketContent.fromDatagramPacket(packet);
			terminal.print(content.header.getClientName() + ": " + content.getMessage());
			this.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sender Method
	 * 
	 */

	private synchronized void send() throws Exception {
		while (true) {
			String input = terminal.readString().trim();
			if (input.length() > 0) {
				String[] clientMessageString = input.split("-", 2);
				String client = clientMessageString[0].trim();
				String[] clientNames = client.split(":", 2);
				if (clientNames.length < 2 || clientMessageString.length < 2) {
					terminal.println("Invalid input.\n" + getInstructions());
					break;
				}
				String familyName = clientNames[0].trim();
				String clientName = clientNames[1].trim();
				String message = clientMessageString[1].trim();

				PacketType packetType;
				if (familyName.length() <= 2 && familyName.substring(0, 1).equals("*")) {
					packetType = PacketType.TO_TYPE;
				} else
					packetType = PacketType.REGULAR;

				if (packetType == PacketType.TO_TYPE) {
					ToTypeMessageContent content = new ToTypeMessageContent();
					content.header.setPacketType(packetType);
					content.header.setSrc(clientIP);
					content.header.setSrcPort(clientPort);
					content.header.setClientName(clientName);

					switch (clientName.toUpperCase()) {
					case "FRIDGE":
						content.header.setDstType(DeviceType.FRIDGE);
						break;
					case "TV":
						content.header.setDstType(DeviceType.TV);
						break;
					default:
						break;
					}
					content.setMessage(message);
					DatagramPacket packet = content.toDatagramPacket();
					packet.setAddress(routerIP);
					packet.setPort(routerPort);
					socket.send(packet);
				} else {
					RegularMessageContent content = new RegularMessageContent();
					content.header.setPacketType(packetType);
					content.header.setSrc(clientIP);
					content.header.setSrcPort(clientPort);
					content.header.setClientName(clientName);
					content.header.setDstType(DeviceType.PC);
					content.header.setDstName(client);
					content.setDstClientName(clientName);
					content.setDstFamilyName(familyName);

					content.setMessage(message);
					DatagramPacket packet = content.toDatagramPacket();
					packet.setAddress(routerIP);
					packet.setPort(routerPort);
					socket.send(packet);
				}
			}
			this.wait(100);
		}
		send();
	}

	public synchronized void start() throws Exception {
		if (!initialised) {
			initialiseRouterConnection();
			initialised = true;
			terminal.println(getInstructions());
		}
		this.send();
	}

	private String getInstructions() {
		String instructions = "To send a message to a user, enter the recipients name in the form \" [familyName] : [clientName] \", followed by \" - \" (without quotes). "
				+ "\nTo send a message to all devices of a specific type, enter the type in the form \" * : [type] \" followed by \" - \" (without quotes)."
				+ "\nTo send a message to all users associated with a router, enter the name in the form \" [familyName] : * \", followed by \" - \" (without quotes)";
		return instructions;
	}

	private void initialiseRouterConnection() throws IOException {
		PacketContent init = new NewClientContent();
		init.header.setPacketType(PacketType.NEW_CLIENT);
		init.header.setSrc(this.clientIP);
		init.header.setSrcPort(this.clientPort);
		init.header.setDst(routerIP);
		init.header.setDstType(DeviceType.ROUTER);
		init.header.setClientName(clientName);
		DatagramPacket packet = init.toDatagramPacket();
		packet.setAddress(routerIP);
		packet.setPort(routerPort);
		socket.send(packet);
	}

	/**
	 * Initialise client; Cmd line args: Own IP, Own port, Own name, Router IP,
	 * Router port
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 5) {
				System.err.println("ERROR: Incorrect Command line Parameters. "
						+ "\nParameters: <Own IP> <Own port> <Own name> <Router IP> <Router port>");
				System.exit(1);
			}
			Terminal terminal = new Terminal("Client - " + args[2]);
			(new Client(terminal, args[0], Integer.parseInt(args[1]), args[2], args[3], Integer.parseInt(args[4])))
					.start();
			terminal.println("Program completed");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
