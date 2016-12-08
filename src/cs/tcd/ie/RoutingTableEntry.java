package cs.tcd.ie;

import java.net.InetAddress;

public class RoutingTableEntry {
	private String dstName;
	private InetAddress dstAddress; 
	private String srcName;
	private InetAddress srcAddress;
	
	private int cost;
}
