package rolit.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import rolit.Board;
import rolit.client.view.ClientGUI;

public class RolitClient {
	
	/**
	 * Method to run the client side of Rolit
	 */
	public static void main(String[] args) {
		
	/*	Board board = new Board(); // MODEL
		ClientGUI view = new ClientGUI(); // VIEW
		
		board.addObserver(view);
		
		ClientController cc = new ClientController(); // CONTROLLER
		cc.addModel(board);
		cc.addView(view);
		
		view.addController(cc);
		
		cc.init();	*/
				
		try {
			Client c = new Client("Client", InetAddress.getLocalHost(), 1337, new ClientConnectGUI());
			/*c.run();
			c.sendMessage("Hallo");*/
			System.out.println("Hoiiii");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
