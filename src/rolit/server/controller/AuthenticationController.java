//package rolit.server.controller;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import java.security.PublicKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Scanner;
//
//import javax.xml.bind.DatatypeConverter;
//
//
//public class AuthenticationController extends Thread {
//
//	private ServerController controller;
//	private ClientHandlerController network;
//	private Socket sock;
//	private BufferedReader in;
//	private BufferedWriter out;
//	private String name;
//
//	public AuthenticationController(ServerController controller, ClientHandlerController network, String name) {
//		this.controller = controller;
//		this.network = network;
//		this.name = name;
//	}
//
//	public void run() {
//		try {
//
//			InetAddress host;
//			try {
//				host = InetAddress.getByName("ss-security.student.utwente.nl");
//			} catch (UnknownHostException e) {
//				host = null;
//				controller.addMessage("Coultn't connect to authentication server!");
//			}
//
//			int port = 2013;
//			controller.addMessage("Connecting to Authenticationserver on "+ host +":"+ port +".");
//			sock = new Socket(host, port);
//			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
//			
//			try {
//				out.write("PUBLICKEY "+ name +"\n");
//				out.flush();
//			} catch (IOException e) {
//				controller.addMessage("Authentication failed!");
//				network.sendMessage(NetworkController.JOINDENY + " 1");
//			}
//
//			while(true) {
//				if(in.ready()) {
//					String command = in.readLine();
//					if(command != null) {
//						execute(command);
//					} else {
//						shutdown();
//					}
//				}
//			}
//		} catch (IOException e) {
//			shutdown();
//		}
//	}
//	
//	public void shutdown() {
//		try {
//			in.close();
//			out.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//
//	public void execute(String msg) {
//		Scanner in = new Scanner(msg);
//		String cmd = in.next();
//
//		if(cmd.equals("ERROR")) {
//			String tempmsg = in.nextLine();
//			
//			controller.addMessage("ERROR: "+ tempmsg);
//		}
//		if(cmd.equals("PUBKEY")) {
//			System.out.println("PUBKEY RECIEVED");
//			X509EncodedKeySpec keySpec = new X509EncodedKeySpec ( DatatypeConverter.parseBase64Binary(in.next()) );
//			PublicKey pub= null;
//			try {
//				KeyFactory fact = KeyFactory . getInstance ("RSA");
//				pub = fact . generatePublic ( keySpec );
//				
//			} catch (NoSuchAlgorithmException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvalidKeySpecException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("PUBKEY DECRYPTED");
//			if(pub != null) {
//				network.setPublicKey(pub);
//				
//				try {
//					in.close();
//					out.close();
//					sock.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				System.out.println("PUBKEY SET");
//			} else {
//				controller.addMessage("Something went wrong while trying to obtain public key. Please try again.");
//				network.sendMessage(NetworkController.JOINDENY + " 1");
//			}
//
//		}
//		in.close();
//	}
//}
