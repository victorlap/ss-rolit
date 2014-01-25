package rolit.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class Server extends Thread {
	private int port;
	private ServerSocket ss;
	private Collection<ClientHandler> threads;

        /** Constructs a new Server object */
	public Server(int portArg) {
		this.port = portArg;
		this.threads = new ArrayList<ClientHandler>();
	}

	/**
	 * Listens to a port of this Server if there are any Clients that 
         * would like to connect. For every new socket connection a new
         * ClientHandler thread is started that takes care of the further
         * communication with the Client. 
	 */
	public void run() {
		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while(true) {
			try {
				Socket newSocket = ss.accept();
				ClientHandler newHandler = new ClientHandler(this, newSocket);
				addHandler(newHandler);
			} catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}

	/**
	 * Sends a message using the collection of connected ClientHandlers
         * to all connected Clients.
	 * @param msg message that is send
	 */
	public void broadcast(String msg) {
		for(ClientHandler handler : threads) {
			handler.sendMessage(msg);
		}
	}

	/**
	 * Add a ClientHandler to the collection of ClientHandlers.
	 * @param handler ClientHandler that will be added
	 */
	public void addHandler(ClientHandler handler) {
		threads.add(handler);
		try {
			handler.start();
			handler.announce();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Remove a ClientHandler from the collection of ClientHanlders. 
	 * @param handler ClientHandler that will be removed
	 */
	public void removeHandler(ClientHandler handler) {
		threads.remove(handler);
	}
}
