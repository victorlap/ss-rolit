package rolit.client;

import rolit.Board;
import rolit.client.view.ClientGUI;

public class RolitClient {
	
	/**
	 * Method to run the client side of Rolit
	 */
	public static void main(String[] args) {
		
		Board board = new Board(); // MODEL
		ClientGUI view = new ClientGUI(); // VIEW
		
		board.addObserver(view);
		
		ClientController cc = new ClientController(); // CONTROLLER
		cc.addModel(board);
		cc.addView(view);
		
		view.addController(cc);
		
		cc.init();
		
	}

}
