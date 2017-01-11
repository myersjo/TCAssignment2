package cs.tcd.ie;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class DistanceVectorRouting implements IRouting {

	private ArrayList<RoutingTableEntry> routingTable;
	private HashMap<String, InetSocketAddress> neighbours;
	private HashMap<String, InetSocketAddress> clients;

	public DistanceVectorRouting(ArrayList<RoutingTableEntry> routingTable,
			HashMap<String, InetSocketAddress> neighbourRouters, HashMap<String, InetSocketAddress> connectedClients) {
		this.routingTable = routingTable;
		this.neighbours = neighbourRouters;
		this.clients = connectedClients;
	}

	@Override
	public void updateRoutingTable() {
		// This method is called by the router(s)
		// TODO: Implement. this.routingTable points to the same routingTable as
		// that of the router - any changes here will update it for the router
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
