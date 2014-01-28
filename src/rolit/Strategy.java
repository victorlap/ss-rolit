package rolit;


public interface Strategy {
	
	/**
	 * Return name of the strategy
	 * @return name of the strategy
	 */
	public String getName();
	
	/**
	 * Determine move according to the strategy algorithm.
	 * @param b Board
	 * @param c Color
	 * @return <code>int</code> with the index of the next move
	 */
	public int determineMove(Board b, Color c);
}
