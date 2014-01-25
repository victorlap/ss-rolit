package rolit.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * ClientHandler.
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class ClientHandler extends Thread {

	private Server server;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName;

	/**
	 * Constructs a ClientHandler object
	 * Initialises both Data streams.
	 *@ requires server != null && sock != null;
	 */
	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		this.server = serverArg;
		this.sock = sockArg;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
	}

	/**
	 * Reads the name of a Client from the input stream and sends 
         * a broadcast message to the Server to signal that the Client
         * is participating in the chat. Notice that this method should 
         * be called immediately after the ClientHandler has been constructed.
	 */
	public void announce() throws IOException {
		clientName = in.readLine();
		server.broadcast("[" + clientName + " has entered]");
	}

	/**
         * This method takes care of sending messages from the Client.
         * Every message that is received, is preprended with the name
         * of the Client, and the new message is offered to the Server
         * for broadcasting. If an IOException is thrown while reading
         * the message, the method concludes that the socket connection is
         * broken and shutdown() will be called. 
	 */
	public void run() {
		try {
			while(true) {
				String msg = "[" + this.clientName + "] " + in.readLine();
				server.broadcast(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method can be used to send a message over the socket
         * connection to the Client. If the writing of a message fails,
         * the method concludes that the socket connection has been lost
         * and shutdown() is called.
	 */
	public void sendMessage(String msg) {
		try {
			System.out.println("[NEW MSG] " + msg);
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This ClientHandler signs off from the Server and subsequently
         * sends a last broadcast to the Server to inform that the Client
         * is no longer participating in the chat. 
	 */
	/*private void shutdown() {
		server.removeHandler(this);
		server.broadcast("[" + clientName + " has left]");
	}*/

} // end of class ClientHandler
