package cs.tcd.ie;

import java.net.InetSocketAddress;
import java.util.HashMap;

public interface IRouting {
	/**
	 * Called by a router to update its routing table.
	 */
	public void updateRoutingTable();

	/**
	 * Called by a router when its list of neighbours is updated.
	 * 
	 * @param neighbours
	 */
	public void setNeighbours(HashMap<String, InetSocketAddress> neighbours);

	/**
	 * Called by a router when its list of clients is updated.
	 * 
	 * @param clients
	 */
	public void setClients(HashMap<String, InetSocketAddress> clients);
}
