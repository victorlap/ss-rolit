package rolit.client;

import rolit.Board;
import rolit.client.controller.ClientController;
import rolit.client.view.ClientGUI;

public class RolitClient {
	
	/**
	 * Test method
	 */
	public static void main(String[] args) {
		
		Board board = new Board(); // MODEL
		ClientGUI client = new ClientGUI(board); // VIEW
		ClientController c = new ClientController(client); // CONTROLLER
		
		
	}

}
