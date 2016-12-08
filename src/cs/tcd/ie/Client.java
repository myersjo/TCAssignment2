/**
 * 
 */
package cs.tcd.ie;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.lang.Byte;

import tcdIO.*;

/**
 *
 * Client class
 * 
 * An instance accepts user input
 *
 */
public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";

	Terminal terminal;

	/**
	 * Constructor
	 * 
	 * Attempts to create socket at given port and create an InetSocketAddress
	 * for the destinations
	 */
	Client(Terminal terminal, int srcPort) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(srcPort);
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
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Sender Method
	 * 
	 */
	public synchronized void start() throws Exception {
		
	}

	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Client");
			(new Client(terminal, DEFAULT_SRC_PORT)).start();
			terminal.println("Program completed");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
