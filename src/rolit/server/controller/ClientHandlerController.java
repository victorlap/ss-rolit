package rolit.server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import rolit.Player;


/**
 * ClientHandler.
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class ClientHandlerController extends Thread {

	private ServerController controller;
	private NetworkController network;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private boolean isRunning;
	private Player player;
	//private PublicKey publicKey = null;
	//private byte[] nonce;

	/**
	 * Constructs a ClientHandler object
	 * Initialises both Data streams.
	 *@ requires server != null && sock != null;
	 */
	public ClientHandlerController(NetworkController serverArg, Socket sockArg, ServerController controller) throws IOException {
		this.network = serverArg;
		this.sock = sockArg;
		this.controller = controller;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		isRunning = true;
		player = new Player();
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
			while(isRunning) {
				String command = in.readLine();
				if(command != null) {
					network.execute(command, this);
				} else {
					shutdown();
				}
			}
		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * This method can be used to send a message over the socket
         * connection to the Client. If the writing of a message fails,
         * the method concludes that the socket connection has been lost
         * and shutdown() is called.
	 */
	public void sendMessage(String msg) {
		if(msg != null) {
			try {
				out.write(msg + "\n");
				out.flush();
			} catch (IOException e) {
				controller.addMessage("[ERROR] Couldn't send message: "+ msg);
			}
		}
	}

	/**
	 * This ClientHandler signs off from the Server and subsequently
         * sends a last broadcast to the Server to inform that the Client
         * is no longer participating in the chat. 
	 */
	public void shutdown() {
		network.removeHandler(this);
		isRunning = false;
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Player getPlayer() {
		return player;
	}

	public Socket getSocket() {
		return sock;
	}

	/*public void setPublicKey(PublicKey pub) {

		System.out.println("Aan de slag");

		byte[] signature = null ; //Signature to be checked

		boolean check = false;
		try {
			Signature sig = Signature . getInstance ("SHA1withRSA" );
			sig . initVerify ( publicKey );
			sig . update ( nonce );
			check = sig . verify ( signature );
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(check) {
			sendMessage(NetworkController.JOINCONFIRM);
			sendMessage(NetworkController.COLOURREQ); //TODO: nog even kijken naar welke id's vrij zijn en hoe ze hier komen
		} else {
			sendMessage(NetworkController.JOINDENY + NetworkController.DELIM + "1");
		}
	}*/

} // end of class ClientHandler
