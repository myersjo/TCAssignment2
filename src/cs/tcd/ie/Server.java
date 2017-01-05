package cs.tcd.ie;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import tcd.lossy.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import tcdIO.Terminal;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;
	byte nextSeqNumber = 0;

	Terminal terminal;
	InetAddress routerIP;
	int routerPort;
	String familyName;
	ArrayList<InetSocketAddress> connectedClients;
	ArrayList<InetSocketAddress> connectedRouters; // neighbours (directly
													// connected routers only)

	boolean initialised = false;

	/*
	 * 
	 */
	Server(Terminal terminal, String ownIP, int ownPort, String familyName, ArrayList<InetSocketAddress> neighbours) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(ownPort);
			this.routerIP = InetAddress.getByName(ownIP);
			this.routerPort = ownPort;
			this.familyName = familyName;
			this.connectedRouters = neighbours;
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
			e.printStackTrace();
		}
	}

	public synchronized void start() throws Exception {
		if (!initialised) {
			initialise();
			initialised = true;
		}
		terminal.println("Waiting for contact");
		this.wait();
	}

	/**
	 * Sends 'new router' handshake to all directly connected routers
	 * 
	 * @throws IOException
	 */
	private void initialise() throws IOException {
		if (!connectedRouters.isEmpty()) {
			for (InetSocketAddress connectedRouter : connectedRouters) {
				PacketContent init = new NewRouterContent();
				init.header.setPacketType(PacketType.NEW_ROUTER);
				init.header.setSrc(this.routerIP);
				init.header.setDst(connectedRouter.getAddress());
				init.header.setDstType(DeviceType.ROUTER);
				init.header.setFamilyName(familyName);
				DatagramPacket packet = init.toDatagramPacket();
				packet.setAddress(connectedRouter.getAddress());
				packet.setPort(connectedRouter.getPort());
				socket.send(packet);
			}
		}
		// TODO: add neighbour routers to routing table
		// TODO: initiate routing protocol
	}

	/**
	 * Initialise router; Cmd line args: Own IP, Own port, Own family name, zero
	 * or more connected routers (IP address, port)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Router (Server) - " + args[2]);
			// (new Server(terminal, DEFAULT_PORT)).start();
			ArrayList<InetSocketAddress> neighbours = new ArrayList<InetSocketAddress>();
			if (args.length < 3) {
				System.err.println("ERROR: Incorrect Command line Parameters. "
						+ "\nParameters: <Own IP> <Own port> <Own family name> [<Router IP> <Router port> ...]"
						+ "(Zero or more connected routers)");
				System.exit(1);
			} else if (args.length >= 4) {
				for (int i = 3; i < args.length; i += 2) {
					neighbours.add((new InetSocketAddress(args[i], Integer.parseInt(args[i + 1]))));
				}
			}
			(new Server(terminal, args[0], Integer.parseInt(args[1]), args[2], neighbours)).start();
			terminal.println("\nProgram completed");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
