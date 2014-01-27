package rolit.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;


public class NetworkController extends Thread {
	private int port;
	private ServerSocket ss;
	private Collection<ClientHandlerController> threads;
	private ServerController controller;

        /** Constructs a new Server object */
	public NetworkController(int portArg, ServerController controller) {
		this.port = portArg;
		this.threads = new ArrayList<ClientHandlerController>();
		this.controller = controller;
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
				ClientHandlerController newHandler = new ClientHandlerController(this, newSocket, controller);
				controller.addMessage("Trying to connect to new Client");
				addHandler(newHandler);
				newHandler.sendMessage("Extensions");
			} catch(IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public boolean isUsernameInUse(String username) {
		for(ClientHandlerController handler : threads) {
			if(handler.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sends a message using the collection of connected ClientHandlers
         * to all connected Clients.
	 * @param msg message that is sent
	 */
	public void broadcast(String msg) {
		for(ClientHandlerController handler : threads) {
			handler.sendMessage(msg);
		}
	}

	/**
	 * Add a ClientHandler to the collection of ClientHandlers.
	 * @param handler ClientHandler that will be added
	 */
	public void addHandler(ClientHandlerController handler) throws IOException {
		threads.add(handler);
		handler.start();
	}

	/**
	 * Remove a ClientHandler from the collection of ClientHanlders. 
	 * @param handler ClientHandler that will be removed
	 */
	public void removeHandler(ClientHandlerController handler) {
		threads.remove(handler);
	}
}
