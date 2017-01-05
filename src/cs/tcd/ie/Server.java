package cs.tcd.ie;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import tcd.lossy.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tcdIO.Terminal;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;
	byte nextSeqNumber = 0;

	Terminal terminal;
	InetAddress routerIP;
	int routerPort;
	String familyName;
//	ArrayList<InetSocketAddress> connectedClients;
	HashMap<InetSocketAddress, String> connectedClients = new HashMap<InetSocketAddress, String>();
//	ArrayList<InetSocketAddress> connectedRouters; // neighbours (directly
													// connected routers only)
	HashMap<InetSocketAddress, String> connectedRouters = new HashMap<InetSocketAddress, String>();

	boolean initialised = false;

	/*
	 * 
	 */
	// ArrayList<InetSocketAddress> neighbours
	Server(Terminal terminal, String ownIP, int ownPort, String familyName, HashMap<InetSocketAddress, String> neighbours) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(ownPort);
			this.routerIP = InetAddress.getByName(ownIP);
			this.routerPort = ownPort;
			this.familyName = familyName;
			if (neighbours==null)
//				connectedRouters = new ArrayList<InetSocketAddress>();
				connectedRouters = new HashMap<InetSocketAddress, String>();
			else
				this.connectedRouters = neighbours;
//			this.connectedClients = new ArrayList<InetSocketAddress>();
			this.connectedClients = new HashMap<InetSocketAddress, String>();
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
			if (packet == null)
				return;
			
			PacketContent content = PacketContent.fromDatagramPacket(packet);
			switch (content.header.getPacketType()) {
			case NEW_ROUTER:
				onRecNewRouter(packet);
				break;
			case NEW_CLIENT:
				onRecNewClient(packet);
				break;
			case PING:
				onRecPing(packet);
				break;
			case LS_UPDATE:
				onRecLsUpdate(packet);
				break;
			case DV_UPDATE:
				onRecDvUpdate(packet);
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
	
	private void onRecNewRouter(DatagramPacket packet) {
		// TODO: Send return packet with this router's name
	}
	
	private void onRecNewClient(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		System.out.println("New Client: " + content.header.getClientName() + "\tIP: " + content.header.getSrc().toString()
				+ "\t" + packet.getAddress().toString() + ":" + packet.getPort()
				+ "\t" + content.header.getSrc().toString() + ":" + content.header.getSrcPort());
		connectedClients.put((new InetSocketAddress(content.header.getSrc(), content.header.getSrcPort())), content.header.getFamilyName());
		
		System.out.println("************ Connected Clients ************");
		for (Map.Entry<InetSocketAddress, String> client : connectedClients.entrySet()) {
			System.out.println("Address : " + client.getKey() + "\tName: " + client.getValue());
		}
		System.out.println("************ ***************** ************");
	}
	
	private void onRecPing(DatagramPacket packet) {
		
	}
	
	private void onRecLsUpdate(DatagramPacket packet) {
		
	}
	
	private void onRecDvUpdate(DatagramPacket packet) {
		
	}
	
	private void onRecToType(DatagramPacket packet) {
		
	}
	
	private void onRecRegular(DatagramPacket packet) {
		
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
//			for (InetSocketAddress connectedRouter : connectedRouters) {
			System.out.println("************ Connected Routers ************");
			for (Map.Entry<InetSocketAddress, String> connectedRouter : connectedRouters.entrySet()) {
				PacketContent init = new NewRouterContent();
				init.header.setPacketType(PacketType.NEW_ROUTER);
				init.header.setSrc(this.routerIP);
				init.header.setDst(connectedRouter.getKey().getAddress());
				init.header.setDstType(DeviceType.ROUTER);
				init.header.setFamilyName(familyName);
				DatagramPacket packet = init.toDatagramPacket();
				packet.setAddress(connectedRouter.getKey().getAddress());
				packet.setPort(connectedRouter.getKey().getPort());
				socket.send(packet);
				
				System.out.println("Address: " + connectedRouter.getKey() + "\tName: " + connectedRouter.getValue());
			}
			System.out.println("************ ***************** ************");
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
//			ArrayList<InetSocketAddress> neighbours = new ArrayList<InetSocketAddress>();
			HashMap<InetSocketAddress, String> neighbours = new HashMap<InetSocketAddress, String>();
			if (args.length < 3) {
				System.err.println("ERROR: Incorrect Command line Parameters. "
						+ "\nParameters: <Own IP> <Own port> <Own family name> [<Router IP> <Router port> ...]"
						+ "(Zero or more connected routers)");
				System.exit(1);
			} else if (args.length >= 4) {
				for (int i = 3; i < args.length; i += 2) {
					neighbours.put((new InetSocketAddress(args[i], Integer.parseInt(args[i + 1]))), null);
				}
			}
			(new Server(terminal, args[0], Integer.parseInt(args[1]), args[2], neighbours)).start();
			terminal.println("\nProgram completed");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
