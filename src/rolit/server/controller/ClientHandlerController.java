package rolit.server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Random;
import java.util.Scanner;


/**
 * ClientHandler.
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class ClientHandlerController extends Thread {

	private NetworkController network;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String username;
	private ServerController controller;
	private PublicKey publicKey;
	private byte[] nonce;

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
				String command = in.readLine();
				if(command != null) {
					execute(command);
				} else {
					shutdown();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void execute(String command) {
		Scanner in = new Scanner(command);
		String cmd = in.next();
		
		if(cmd.equals("ExtensionsRes")) {
			network.broadcast("ExtensionsConfirm 1");
		}
		if(cmd.equals("JoinReq")) {
			if(network.isUsernameInUse(username)) {
				network.broadcast("JoinDeny 0");
				shutdown();
			} else {
				this.username = in.next();
				
				byte[] nonce = new byte[16];
				Random rand;
				try {
					rand = SecureRandom.getInstance("SHA1PRNG");
					rand.nextBytes (nonce);
					this.nonce = nonce;
					network.broadcast("Encode "+ nonce);
				} catch (NoSuchAlgorithmException e) {
					controller.addMessage("Algorithm Exception");
				}
			}
		}
		if(cmd.equals("Signature")) {
			AuthenticationController ac = new AuthenticationController(controller, this, username);
			ac.start();
			boolean publicKeyRecieved = false;
			while(!publicKeyRecieved) {
				if(publicKey !=  null) {
					publicKeyRecieved = true;
					
					byte[] signature = null ; //Signature to be checked

					boolean check = false;
					try {
						Signature sig = Signature . getInstance (" SHA1withRSA " );
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
						sendMessage("JoinConfirm");
						sendMessage("ColourReq"); //TODO: nog even kijken naar welke id's vrij zijn en hoe ze hier komen
					} else {
						sendMessage("JoinDeny 1");
					}
					

				}
			}
		}
		in.close();
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
	public void shutdown() {
		network.removeHandler(this);
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getUsername() {
		return username;
	}

	public void setPublicKey(PublicKey pub) {
		this.publicKey = pub;
		
	}

} // end of class ClientHandler
