package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import tcd.lossy.DatagramSocket;

import tcdIO.Terminal;

public class Server extends Node {
	static final int DEFAULT_PORT = 50001;
	byte nextSeqNumber = 0;

	Terminal terminal;

	/*
	 * 
	 */
	Server(Terminal terminal, int port) {
		try {
			this.terminal = terminal;
			socket = new DatagramSocket(port);
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
			e.printStackTrace();
		}
	}

	public synchronized void start() throws Exception {
		terminal.println("Waiting for contact");
		this.wait();
	}

	/*
	 * 
	 */
	public static void main(String[] args) {
		try {
			Terminal terminal = new Terminal("Server");
			(new Server(terminal, DEFAULT_PORT)).start();
			terminal.println("\nProgram completed");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
