package rolit.client.controller;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import rolit.Color;

public class NetworkController extends Thread {
	
	private ClientController controller;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;

	/**
	 * Constructs a Client-object and tries to make a socket connection
	 */
	public NetworkController(InetAddress host, int port, ClientController controller) {
		super();

		this.controller = controller;
		
		try {
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			
			controller.addMessage("Connecting to server on "+ host +":"+ port +".");

		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * Reads the messages in the socket connection. Each message will be forwarded to the MessageUI
	 */
	public void run() {
		while(true) {
			try {
				if(in.ready()) {
					String command = in.readLine();
					if(command != null) {
						execute(command);
					} else {
						shutdown();
					}
				}
			} catch (IOException e) {
				shutdown();
			}	
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

	/** close the socket connection. */
	public void shutdown() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (NullPointerException e) {
			controller.addMessage("Could not connect to server");
		} catch (IOException e) {
			e.printStackTrace();
		}
		controller.deadConnection();
	}
	
	//@ requires command != null;
	private void execute(String line) {
		Scanner in = new Scanner(line);
		String cmd = in.next();
		
		if(cmd.equals("Extensions")) {
			sendMessage("ExtensionsRes 1");
		}
		else if(cmd.equals("ExtensionsConfirm")) { // Onze username is victoryuri dacht ik
			// TODO: Afhandelen en even vragen welke integers er gebruikt moeten worden.
			sendMessage("JoinReq victoryuri");
		}
		else if(cmd.equals("Encode")) {
			// TODO: Hier gaan kloten met die authenticatie;
		}
		else if(cmd.equals("JoinConfirm")) {
			controller.connectionEstablished();
			// TODO: JoinConfirm;
		}
		else if(cmd.equals("JoinDeny")) {
			if(in.hasNext()) {
				String temp = in.next();
				if(temp.equals("0")) { // Nickname is al aanwezig op de server
					
				}
				if(temp.equals("1")) { // Signature is niet goed; Probeer het opniew;
					execute("Encode");
				}
			}
		}
		else if(cmd.equals("ColourReq")) {
			//TODO: Afhandelen welke kleur je wilt zijn;
			sendMessage("Colour "+ Color.RED.toInt());
		}
		else if(cmd.equals("ColourDeny")) {
			// TODO: AFwachten op fb;
		}
		else if(cmd.equals("NotifyNewPlayer")) {
			// Er is een nieuwe speler, voeg hem toe aan de array en speel er op los
		}
		else if(cmd.equals("GameStart")) {
			// READY; SET; GO!
		}
		else if(cmd.equals("Turn")) { // We zijn aan de beurt
			
		}
		else if(cmd.equals("MoveDeny")) { // Whoops kan niet
			
		}
		else if(cmd.equals("NotifyMove")) { // Een andere speler heeft een move gedaan
			
		}
		else if(cmd.equals("GameEnd")) { // Nu alweer af??
			
		}
		else if(cmd.equals("LossPlayer")) { // We zijn iemand kwijt
			
		}
		else if(cmd.equals("LeadReturn")) { // We hebben een leaderboard opgevraagd, en nu komt het allemaal hierheen
			
		}
		in.close();
	}
}
