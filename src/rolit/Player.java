package rolit;

import rolit.client.Strategy;

/**
 * Initializes and maintains a player object, with its attached properties
 * @author Yuri van Midden and Victor Lap
 * @version 1.0.0
 */

public abstract class Player {
	
	private String name;
    private Color color;
    public Strategy strategy;
    private boolean takesPart;
    
    /**
     * Constructs a new Player object and defines the name.
     * @param theColor 
     */
    public Player(String theName, Color theColor) {
        this.name = theName;
        this.color = theColor;
    }
    
    public Player(String theName) {
    	this.name = theName;
    }
    
    public void setName(String name) {
		this.name = name;
	}

	/**
     * Returns the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
	 * Sets the color of <code>this</code> player object.
	 * @param theColor of type <code>Color</code>
	 */
	public void setColor (Color theColor) {
		this.color = theColor;
		takesPart = true;
	}

	/**
     * Returns the color of the player.
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Returns boolean derived from <code>takesPart</code>
     * @return <code>true</code> if <code>takesPart</code> is true
     */
    public boolean isTakingPart() {
    	return takesPart;
    }
    
    /**
     * Determines the field for the next move.
     * 
     * @param board
     *            the current game board
     * @return <code>int</code> with the player's choice
     */
    public abstract int determineMove(Board board);
    
    /**
     * Makes a move on the board. <br>
     * 
     * @param board
     *            the current board
     */
    public void makeMove(Board board) {
        int choice = determineMove(board);
        board.setField(choice, getColor());
    }
}
