package rolit.client;

import rolit.Board;
import rolit.Player;

public class HumanPlayer extends Player {

	/**
	 * Creates a new human player object.
	 * 
	 */
	public HumanPlayer(String theName) {
		super(theName);
	}

	public int determineMove(Board board) {
		return 0;
	}

}
