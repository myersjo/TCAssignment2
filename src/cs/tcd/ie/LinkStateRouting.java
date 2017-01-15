package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class LinkStateRouting implements IRouting {

	private Server router;
	private ArrayList<RoutingTableEntry> routingTable;
	private HashMap<String, InetSocketAddress> neighbours;
	private HashMap<String, InetSocketAddress> clients;

	public LinkStateRouting(Server router, ArrayList<RoutingTableEntry> routingTable,
			HashMap<String, InetSocketAddress> neighbourRouters, HashMap<String, InetSocketAddress> connectedClients) {
		this.router = router;
		this.routingTable = routingTable;
		this.neighbours = neighbourRouters;
		this.clients = connectedClients;
	}

	@Override
	public void updateRoutingTable() {
		// This method is called by the router(s)
		// TODO: Implement. this.routingTable points to the same routingTable as
		// that of the router - any changes here will update it for the router
		RoutingTableEntry.setSrcAddress();
		this.routingTable = routingTable;
		addDistance(routingTable);
		RoutingTableEntry.getNextHop();
		
	}

	@Override
	public void onReceipt(DatagramPacket packet) {
		// Called by the router when an update packet is received from another
		// router.
		LSUpdateContent content = (LSUpdateContent) PacketContent.fromDatagramPacket(packet);
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
		
	public int getDistance(){
		Random rand = new Random();
		int distance = rand.nextInt(10+1); //generate random distance
		return distance;
	}
	
	public void addDistance(ArrayList<RoutingTableEntry> routingTable){
		this.routingTable = routingTable;
		RoutingTableEntry.setCost(getDistance()); //put into array list
	}
	
}
}
