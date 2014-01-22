package rolit.server;

import java.util.Collection;

import rolit.MessageUI;

public class Server extends Thread {
	private int port;
	private MessageUI mui;
	private Collection<ClientHandler> threads;

        /** Constructs a new Server object */
	public Server(int portArg, MessageUI muiArg) {
	}

	/**
	 * Listens to a port of this Server if there are any Clients that 
         * would like to connect. For every new socket connection a new
         * ClientHandler thread is started that takes care of the further
         * communication with the Client. 
	 */
	public void run() {
	}

	/**
	 * Sends a message using the collection of connected ClientHandlers
         * to all connected Clients.
	 * @param msg message that is send
	 */
	public void broadcast(String msg) {
	}

	/**
	 * Add a ClientHandler to the collection of ClientHandlers.
	 * @param handler ClientHandler that will be added
	 */
	public void addHandler(ClientHandler handler) {
	}

	/**
	 * Remove a ClientHandler from the collection of ClientHanlders. 
	 * @param handler ClientHandler that will be removed
	 */
	public void removeHandler(ClientHandler handler) {
	}
}
