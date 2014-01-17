package rolit.client;

import rolit.Board;
import rolit.client.view.ClientGUI;

public class RolitClient {
	
	/**
	 * Test method
	 */
	public static void main(String[] args) {
		
		Board board = new Board();
		ClientGUI client = new ClientGUI(board);
		
		
	}

}
