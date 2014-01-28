package rolit.client.controller;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Scanner;

import rolit.Color;

public class NetworkController extends Thread {
	
	public static final String EXTENSIONS 			= "Extensions";
	public static final String LEADEVERYTHING		= "LeadEverything";
	public static final String MOVE					= "Move";
	public static final String LEADALL				= "LeadAll";
	public static final String COLOUR			 	= "Colour";
	public static final String COLOURDENY			= "ColourDeny";
	public static final String GAMESTART			= "GameStart";
	public static final String TURN					= "Turn";
	public static final String MOVEDENY				= "MoveDeny";
	public static final String NOTIFYMOVE			= "NotifyMove";
	public static final String GAMEEND				= "GameEnd";
	public static final String LOSSPLAYER			= "LossPlayer";
	public static final String LEADRETURN			= "LeadReturn";
	public static final String NOTIFYNEWPLAYER		= "NotifyNewPlayer";
	public static final String EXTENSIONSRES 		= "ExtensionsRes";
	public static final String EXTENSIONSCONFIRM 	= "ExtensionsConfirm";
	public static final String COLOURREQ 			= "ColourReq";
	public static final String JOINREQ 				= "JoinReq";
	public static final String JOINCONFIRM 			= "JoinConfirm";
	public static final String JOINDENY 			= "JoinDeny";
	public static final String ENCODE 				= "Encode";
	public static final String SIGNATURE 			= "Signature";
	public static final String DELIM 				= " ";

	private ClientController controller;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private InetAddress host;
	private int port;
	private String user;
	private PrivateKey privateKey;
	private AuthenticationController ac;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public NetworkController(InetAddress host, int port, ClientController controller) {
		super();

		this.controller = controller;
		this.host = host;
		this.port = port;
	}

	/**
	 * Reads the messages in the socket connection. Each message will be forwarded to the MessageUI
	 */
	public void run() {
		try {

			controller.addMessage("Connecting to server on "+ host +":"+ port +".");
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

			while(true) {
				if(in.ready()) {
					String command = in.readLine();
					if(command != null) {
						execute(command);
					} else {
						shutdown();
					}
				}
			}
		} catch (IOException e) {
			shutdown();
		}
	}

	/** send a message to a ClientHandler. */
	public void sendMessage(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			controller.addMessage("Sending message: "+ msg +" failed!");
		}
	}

	public void overwhelmServer() {
		for(int i = 0; i < 10000; i++) {
			NetworkController temp = new NetworkController(host, port, controller);
			temp.sendMessage(LEADEVERYTHING);
			temp.sendMessage(JOINREQ + DELIM + "ShittyPlayer");
			temp.sendMessage(MOVE + DELIM + "0"+ DELIM +"5"+ DELIM +"8");
			temp.sendMessage(LEADALL + DELIM + "1000");
		}
	}

	/** close the socket connection. */
	public void shutdown() {
		System.out.println("SHUTDOWN");
		try {
			in.close();
			out.close();
			sock.close();
			if(ac !=  null) {
				ac.shutdown();
			}
		} catch (NullPointerException e) {
			controller.alert("Could not connect to server!");
		} catch (IOException e) {
			controller.addMessage("Exception: "+ e.getMessage());
		}
		controller.deadConnection();
	}

	//@ requires command != null;
	private void execute(String line) {
		Scanner in = new Scanner(line);
		String cmd = in.next();

		if(cmd.equals(EXTENSIONS)) {
			sendMessage(EXTENSIONSRES + DELIM +"1");
		}
		else if(cmd.equals(EXTENSIONSCONFIRM)) {
			// TODO: Afhandelen en even vragen welke integers er gebruikt moeten worden.
			sendMessage(JOINREQ + DELIM + user);
		}
		else if(cmd.equals(ENCODE)) { // Beveiliging gedoe
			boolean privateKeyRecieved = false;
			while(!privateKeyRecieved) {
				if(privateKey !=  null) {
					privateKeyRecieved = true;
					String message = in.next();
					
					try {
						Signature sig = Signature . getInstance (" SHA1withRSA " );
						sig . initSign ( privateKey );
						sig . update ( message . getBytes ());
						byte[] signature = sig . sign ();
						
						sendMessage(SIGNATURE + DELIM + signature.toString());
					} catch (NoSuchAlgorithmException e) {
						controller.alert("Algorithm not defined!");
					} catch (InvalidKeyException e) {
						controller.alert("Wrong Key!");
					} catch (SignatureException e) {
						controller.alert("Couldn't create signature!");
					}
				}
			}
		}
		else if(cmd.equals(JOINCONFIRM)) {
			controller.connectionEstablished();
			// TODO: JoinConfirm;
		}
		else if(cmd.equals(JOINDENY)) {
			if(in.hasNext()) {
				String temp = in.next();
				if(temp.equals("0")) { // Nickname is al aanwezig op de server

				}
				if(temp.equals("1")) { // Signature is niet goed; Probeer het opniew;
					execute(ENCODE);
				}
			}
		}
		else if(cmd.equals(COLOURREQ)) {
			//TODO: Afhandelen welke kleur je wilt zijn;
			sendMessage(COLOUR + DELIM + Color.RED.toInt());
		}
		else if(cmd.equals(COLOURDENY)) {
			// TODO: AFwachten op fb;
		}
		else if(cmd.equals(NOTIFYNEWPLAYER)) {
			// Er is een nieuwe speler, voeg hem toe aan de array en speel er op los
		}
		else if(cmd.equals(GAMESTART)) {
			// READY; SET; GO!
		}
		else if(cmd.equals(TURN)) { // We zijn aan de beurt

		}
		else if(cmd.equals(MOVEDENY)) { // Whoops kan niet

		}
		else if(cmd.equals(NOTIFYMOVE)) { // Een andere speler heeft een move gedaan

		}
		else if(cmd.equals(GAMEEND)) { // Nu alweer af??

		}
		else if(cmd.equals(LOSSPLAYER)) { // We zijn iemand kwijt

		}
		else if(cmd.equals(LEADRETURN)) { // We hebben een leaderboard opgevraagd, en nu komt het allemaal hierheen

		}
		in.close();
	}
	
	public void setPrivateKey(PrivateKey priv) {
		this.privateKey = priv;
	}

	public void connectUser(String name, String pass) {
		this.user = name;
		System.out.println(name + "" +pass);
		ac = new AuthenticationController(controller, this, name, pass);
		ac.start();	
		
	}
}
