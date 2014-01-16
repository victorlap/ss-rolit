package rolit;



public abstract class Player {
	
	private String name;
    private Color color;
    
    /**
     * Creates a new Player object.
     * 
     */
    public Player(String theName) {
        this.name = theName;
    }
    
    public void setColor (Color theColor) {
    	this.color = theColor;
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
    
    /**
     * Determines the field for the next move.
     * 
     * @param bord
     *            the current game board
     * @return the player's choice
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
