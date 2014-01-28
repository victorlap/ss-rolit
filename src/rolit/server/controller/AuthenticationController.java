package rolit.server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class AuthenticationController extends Thread {

	private ServerController controller;
	private ClientHandlerController network;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String name;

	public AuthenticationController(ServerController controller, ClientHandlerController network, String name) {
		this.controller = controller;
		this.network = network;
		this.name = name;
	}

	public void run() {
		try {

			InetAddress host;
			try {
				host = InetAddress.getByName("ss-security.student.utwente.nl");
			} catch (UnknownHostException e) {
				host = null;
				controller.addMessage("Coultn't connect to authentication server!");
			}

			int port = 2013;
			controller.addMessage("Connecting to Authenticationserver on "+ host +":"+ port +".");
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			
			try {
				out.write("PUBLICKEY "+ name +"\n");
				out.flush();
			} catch (IOException e) {
				controller.addMessage("Authentication failed!");
				network.sendMessage(NetworkController.JOINDENY + " 1");
			}

			while(true) {
				if(in.ready()) {
					String command = in.readLine();
					if(command != null) {
						execute(command);
					} else {
						network.shutdown();
					}
				}
			}
		} catch (IOException e) {
			network.shutdown();
		}
	}

	public void execute(String msg) {
		Scanner in = new Scanner(msg);
		String cmd = in.next();

		if(cmd.equals("ERROR")) {
			String tempmsg = in.nextLine();
			
			controller.addMessage("ERROR: "+ tempmsg);
		}
		if(cmd.equals("PUBKEY")) {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec ( DatatypeConverter.parseBase64Binary(in.next()) );
			PublicKey pub= null;
			try {
				KeyFactory fact = KeyFactory . getInstance (" RSA " );
				pub = fact . generatePublic ( keySpec );
				
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(pub != null) {
				network.setPublicKey(pub);
			} else {
				controller.addMessage("Something went wrong while trying to obtain public key. Please try again.");
				network.sendMessage(NetworkController.JOINDENY + " 1");
			}

		}
		in.close();
	}
}
