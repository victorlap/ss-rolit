package rolit;

/**
 * Initializes and maintains a player object, with its attached properties
 * @author Yuri van Midden and Victor Lap
 * @version 1.0.0
 */

public abstract class Player {
	
	private String name;
    private Color color;
    private boolean takesPart;
    
    /**
     * Creates a new Player object and defines the name.
     */
    public Player(String theName) {
        this.name = theName;
    }
    
    /**
     * Sets the color of <code>this</code> player object.
     * @param theColor of type Color.VALUE
     */
    public void setColor (Color theColor) {
    	this.color = theColor;
    	takesPart = true;
    }
    
    /**
     * Returns the name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mark of the player.
     */
    public Color getColor() {
        return color;
    }
    
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
