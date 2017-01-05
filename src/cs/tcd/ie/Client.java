/**
 * 
 */
package cs.tcd.ie;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.lang.Byte;

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
	InetAddress routerIP;
	int routerPort;

	boolean initialised;

	/**
	 * Constructor
	 * 
	 * Attempts to create socket at given port and create an InetSocketAddress
	 * for the destinations
	 */
	Client(Terminal terminal, String ownIP, int ownPort, String ownName, String routerIP, int routerPort) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(ownPort);
			this.clientIP = InetAddress.getByName(ownIP);
			this.clientPort = ownPort;
			this.clientName = ownName;
			this.routerIP = InetAddress.getByName(routerIP);
			this.routerPort = routerPort;
			initialised = false;
			listener.go();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 */
	public synchronized void onReceipt(DatagramPacket packet) {

		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Sender Method
	 * 
	 */
	public synchronized void start() throws Exception {
		if (!initialised) {
			initialiseRouterConnection();
			initialised = true;
		}

	}

	private void initialiseRouterConnection() throws IOException {
		PacketContent init = new NewClientContent();
		init.header.setPacketType(PacketType.NEW_CLIENT);
		init.header.setSrc(this.clientIP);
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
	 * 	Router port
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
