package rolit.client.controller;

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
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class AuthenticationController extends Thread {

	private ClientController controller;
	private NetworkController network;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String name;
	private String pass;

	public AuthenticationController(ClientController controller, NetworkController network, String name, String pass) {
		this.controller = controller;
		this.network = network;
		this.name = name;
		this.pass = pass;
	}

	public void run() {
		try {

			InetAddress host;
			try {
				host = InetAddress.getByName("ss-security.student.utwente.nl");
			} catch (UnknownHostException e) {
				host = null;
				controller.alert("Coultn't connect to authentication server!");
			}

			int port = 2013;
			controller.addMessage("Connecting to Authenticationserver on "+ host +":"+ port +".");
			sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			
			try {
				System.out.println(name + "" +pass);
				out.write("IDPLAYER "+ name +" "+ pass + "\n");
				out.flush();
			} catch (IOException e) {
				controller.addMessage("Authentication failed!");
			}

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

	public void execute(String msg) {
		Scanner in = new Scanner(msg);
		String cmd = in.next();

		if(cmd.equals("ERROR")) {
			String tempmsg = in.nextLine();
			
			controller.alert("ERROR: "+ tempmsg);
		}
		if(cmd.equals("PRIVKEY")) {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec ( DatatypeConverter.parseBase64Binary(in.next()) );
			PrivateKey priv = null;
			try {
				KeyFactory fact = KeyFactory . getInstance ("RSA");
				priv = fact . generatePrivate ( keySpec );
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(priv != null) {
				network.setPrivateKey(priv);
				shutdown();
			} else {
				controller.alert("Something went wrong while trying to obtain private key. Please try again.");
			}
		}
		in.close();
	}
	
	public void shutdown() {
		try {
			in.close();
			out.close();
			sock.close();
		} catch (IOException e) {
			/* Doesn't matter */
		}
	}
}
