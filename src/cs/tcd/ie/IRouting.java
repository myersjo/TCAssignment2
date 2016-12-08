package cs.tcd.ie;

import java.net.InetAddress;

public interface IRouting {
//	public RoutingTableEntry[] routingTable;
	public void populateRoutingTable();
	public InetAddress getNextHop(InetAddress dst);
}
