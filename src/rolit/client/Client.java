package rolit.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import rolit.MessageUI;

public class Client {
	private String clientName;
	private MessageUI mui;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public Client(String name, InetAddress host, int port, MessageUI muiArg) throws IOException {
		// try to open a Socket to the server
		/*try {
			sock = new Socket(addr, port);
		} catch (IOException e) {
			System.out.println("ERROR: could not create a socket on " + addr
					+ " and port " + port);
		}*/
	}

	/**
	 * Reads the messages in the socket connection. Each message will be forwarded to the MessageUI
	 */
	public void run() {
	}

	/** send a message to a ClientHandler. */
	public void sendMessage(String msg) {
	}

	/** close the socket connection. */
	public void shutdown() {
	}

	/** returns the client name */
	public String getClientName() {
		return clientName;
	}
}
