package rolit.client;

import rolit.Board;
import rolit.Color;

public interface Strategy {
	
	/**
	 * Return name of the strategy
	 * @return name of the strategy
	 */
	public String getName();
	
	/**
	 * 
	 * @param b Board
	 * @param c Color
	 * @return next legal move
	 */
	public int determineMove(Board b, Color c);
}
