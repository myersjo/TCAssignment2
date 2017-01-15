package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class TestRouting implements IRouting {

	private Server router;
	private ArrayList<RoutingTableEntry> routingTable;
	private HashMap<String, InetSocketAddress> neighbours;
	private HashMap<String, InetSocketAddress> clients;

	public TestRouting(Server router, ArrayList<RoutingTableEntry> routingTable,
			HashMap<String, InetSocketAddress> neighbourRouters, HashMap<String, InetSocketAddress> connectedClients) {
		this.router = router;
		this.routingTable = routingTable;
		this.neighbours = neighbourRouters;
		this.clients = connectedClients;
	}

	@Override
	public void updateRoutingTable() {
		routingTable = new ArrayList<RoutingTableEntry>();
		if (router.familyName.toUpperCase().equals("BURKE")) {
			routingTable.add((new RoutingTableEntry("Walsh", (new InetSocketAddress("localhost", 50002)),
					(new InetSocketAddress("localhost", 50002)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Walsh:Fridge", (new InetSocketAddress("localhost", 50008)),
					(new InetSocketAddress("localhost", 50002)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Walsh:Bob", (new InetSocketAddress("localhost", 50003)),
					(new InetSocketAddress("localhost", 50002)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Burke:Alice", (new InetSocketAddress("localhost", 50000)),
					(new InetSocketAddress("localhost", 50000)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Burke:Aoife", (new InetSocketAddress("localhost", 50004)),
					(new InetSocketAddress("localhost", 50004)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Burke:Fridge", (new InetSocketAddress("localhost", 50007)),
					(new InetSocketAddress("localhost", 50007)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));

		} else if (router.familyName.toUpperCase().equals("WALSH")) {

			routingTable.add((new RoutingTableEntry("Burke", (new InetSocketAddress("localhost", 50001)),
					(new InetSocketAddress("localhost", 50001)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Walsh:Fridge", (new InetSocketAddress("localhost", 50008)),
					(new InetSocketAddress("localhost", 50008)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Walsh:Bob", (new InetSocketAddress("localhost", 50003)),
					(new InetSocketAddress("localhost", 50003)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Burke:Alice", (new InetSocketAddress("localhost", 50000)),
					(new InetSocketAddress("localhost", 50001)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Burke:Aoife", (new InetSocketAddress("localhost", 50004)),
					(new InetSocketAddress("localhost", 50001)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
			routingTable.add((new RoutingTableEntry("Burke:Fridge", (new InetSocketAddress("localhost", 50007)),
					(new InetSocketAddress("localhost", 50001)), router.familyName,
					(new InetSocketAddress(router.routerIP, router.routerPort)))));
		} else if (router.familyName.toUpperCase().equals("MURPHY")) {

		} else if (router.familyName.toUpperCase().equals("R2")) {

		}
	}

	@Override
	public void onReceipt(DatagramPacket packet) {
		// Called by the router when an update packet is received from another
		// router.
	}

	public ArrayList<RoutingTableEntry> getRoutingTable() {
		return routingTable;
	}

	public void setRoutingTable(ArrayList<RoutingTableEntry> routingTable) {
		this.routingTable = routingTable;
	}

	public HashMap<String, InetSocketAddress> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(HashMap<String, InetSocketAddress> neighbours) {
		this.neighbours = neighbours;
	}

	public HashMap<String, InetSocketAddress> getClients() {
		return clients;
	}

	public void setClients(HashMap<String, InetSocketAddress> clients) {
		this.clients = clients;
	}
}