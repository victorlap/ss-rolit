package rolit.server;

import rolit.server.view.ServerGUI;


/**
 * Deze klasse regelt de inkomende connecties en laat spelers samen in een game
 * 
 * @author Victor Lap en Yuri van Midden
 * @version 1.0.0
 *
 */

public class RolitServer {

	public static final int defaultPort = 1337;
	
	//private boolean mustListen = false;

	public RolitServer() {

	}

	// Awaiting other people
	/*public void startServer(int portNumber) throws IOException {

		ServerSocket serverSocket = new ServerSocket(portNumber);
		
		mustListen = true;
		
		while(mustListen) {
			Socket socket = serverSocket.accept();
		}
	}*/
	
	public static void main (String[] args) {
		new ServerGUI("192.168.2.1", "1337");
	}
}
