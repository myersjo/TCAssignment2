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
	HashMap<String, InetSocketAddress> connectedClients;
	HashMap<String, InetSocketAddress> connectedRouters; // neighbours who have
															// sent a reply
	ArrayList<InetSocketAddress> pendingRouters; // neighbours (directly
													// connected routers) who
													// have not replied
	boolean initialised;

	Server(Terminal terminal, String ownIP, int ownPort, String familyName, ArrayList<InetSocketAddress> neighbours) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(ownPort);
			this.routerIP = InetAddress.getByName(ownIP);
			this.routerPort = ownPort;
			this.familyName = familyName;
			if (neighbours == null)
				this.pendingRouters = new ArrayList<InetSocketAddress>();
			else
				this.pendingRouters = neighbours;
			this.connectedClients = new HashMap<String, InetSocketAddress>();
			this.connectedRouters = new HashMap<String, InetSocketAddress>();
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
			if (packet == null)
				return;

			PacketContent content = PacketContent.fromDatagramPacket(packet);
			System.out.println(
					"Server onReceipt: " + content.header.getPacketType() + " from: " + content.header.getFamilyName());

			switch (content.header.getPacketType()) {
			case NEW_ROUTER_REPLY:
				onRecNewRouterReply(packet);
				break;
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

	/**
	 * Adds the src router to connectedRouters and removes it from the
	 * pendingRouters list
	 * @param packet
	 */
	private void onRecNewRouterReply(DatagramPacket packet) {
		NewRouterReplyContent content = (NewRouterReplyContent) PacketContent.fromDatagramPacket(packet);
		for (InetSocketAddress router : pendingRouters) {
			if (router.getAddress().equals(content.header.getSrc())
					&& router.getPort() == content.header.getSrcPort()) {
				connectedRouters.put(content.getRouterName(),
						(new InetSocketAddress(content.header.getSrc(), content.header.getSrcPort())));
				pendingRouters.remove(router);
				break;
			}
		}

		printConnectedRouters();
	}

	/**
	 * Adds the new router to the connectedRouters list and sends a response
	 * @param packet
	 * @throws IOException
	 */
	private void onRecNewRouter(DatagramPacket packet) throws IOException {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		connectedRouters.put(content.header.getFamilyName(),
				(new InetSocketAddress(content.header.getSrc(), content.header.getSrcPort())));

		NewRouterReplyContent reply = new NewRouterReplyContent();
		reply.header.setPacketType(PacketType.NEW_ROUTER_REPLY);
		reply.header.setSrc(routerIP);
		reply.header.setSrcPort(routerPort);
		reply.header.setDst(content.header.getSrc());
		reply.header.setDstName(content.header.getFamilyName());
		reply.header.setDstType(DeviceType.ROUTER);
		// TODO: Update; name included twice
		reply.header.setFamilyName(familyName);
		reply.setRouterName(familyName);
		DatagramPacket replyPacket = reply.toDatagramPacket();
		replyPacket.setAddress(content.header.getSrc());
		replyPacket.setPort(content.header.getSrcPort());
		System.out.println(replyPacket.getAddress().toString() + ":" + replyPacket.getPort());
		socket.send(replyPacket);

		printConnectedRouters();
	}

	/**
	 * Adds the new client to the connectedClients list
	 * @param packet
	 */
	private void onRecNewClient(DatagramPacket packet) {
		PacketContent content = PacketContent.fromDatagramPacket(packet);
		System.out.println("New Client: " + content.header.getClientName() + "\tIP: "
				+ content.header.getSrc().toString() + "\t" + packet.getAddress().toString() + ":" + packet.getPort()
				+ "\t" + content.header.getSrc().toString() + ":" + content.header.getSrcPort());

		connectedClients.put(content.header.getClientName(),
				(new InetSocketAddress(content.header.getSrc(), content.header.getSrcPort())));

		printConnectedClients();
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
			terminal.println("Waiting for contact");
		}
		this.wait();
	}

	/**
	 * Prints the list of all routers directly connected to this router
	 */
	private void printConnectedRouters() {
		System.out.println("************ Connected Routers ************");
		for (Map.Entry<String, InetSocketAddress> connectedRouter : connectedRouters.entrySet()) {
			System.out.println("Address: " + connectedRouter.getValue() + "\tName: " + connectedRouter.getKey());
		}
		System.out.println("************ ***************** ************");
	}

	/**
	 * Prints the list of all clients directly connected to this router
	 */
	private void printConnectedClients() {
		System.out.println("************ Connected Clients ************");
		for (Map.Entry<String, InetSocketAddress> client : connectedClients.entrySet()) {
			System.out.println("Address : " + client.getValue() + "\tName: " + client.getKey());
		}
		System.out.println("************ ***************** ************");
	}

	/**
	 * Sends 'new router' handshake to all directly connected routers
	 * @throws IOException
	 */
	private void initialise() throws IOException {
		if (!pendingRouters.isEmpty()) {
			System.out.println("******** Connection Pending Routers ********");
			for (InetSocketAddress router : pendingRouters) {
				PacketContent init = new NewRouterContent();
				init.header.setPacketType(PacketType.NEW_ROUTER);
				init.header.setSrc(this.routerIP);
				init.header.setSrcPort(this.routerPort);
				init.header.setDst(router.getAddress());
				init.header.setDstType(DeviceType.ROUTER);
				init.header.setFamilyName(familyName);
				DatagramPacket packet = init.toDatagramPacket();
				packet.setAddress(router.getAddress());
				packet.setPort(router.getPort());
				socket.send(packet);

				System.out.println("Address: " + router.getAddress().toString());
			}
			System.out.println("************ ****************** ************");
		}

		// TODO: add neighbour routers to routing table
		// TODO: initiate routing protocol
	}

	/**
	 * Initialise router; Cmd line args: Own IP, Own port, Own family name, zero
	 * or more connected routers (IP address, port)
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Router (Server) - " + args[2]);
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
