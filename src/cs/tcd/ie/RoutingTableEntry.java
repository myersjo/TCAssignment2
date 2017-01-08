package cs.tcd.ie;

import java.net.InetSocketAddress;

public class RoutingTableEntry {
	private String dstName;
	private InetSocketAddress dstAddress;
	private InetSocketAddress nextHop;
	private String srcName;
	private InetSocketAddress srcAddress;
	private int cost;

	public String getDstName() {
		return dstName;
	}

	public void setDstName(String dstName) {
		this.dstName = dstName;
	}

	public InetSocketAddress getDstAddress() {
		return dstAddress;
	}

	public void setDstAddress(InetSocketAddress dstAddress) {
		this.dstAddress = dstAddress;
	}

	public InetSocketAddress getNextHop() {
		return nextHop;
	}

	public void setNextHop(InetSocketAddress nextHop) {
		this.nextHop = nextHop;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public InetSocketAddress getSrcAddress() {
		return srcAddress;
	}

	public void setSrcAddress(InetSocketAddress srcAddress) {
		this.srcAddress = srcAddress;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
}
