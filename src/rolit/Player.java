package rolit;


/**
 * Initializes and maintains a player object, with its attached properties
 * @author Yuri van Midden and Victor Lap
 * @version 1.0.0
 */

public class Player {
	
	private String name;
    private Color color;
    private boolean takesPart;
    private boolean isReady;
    
    /**
     * Constructs a new Player object and defines the name.
     */    
    public Player() {
    	name = "[NOTCONNECTED]";
    	color = Color.NONE;
    }
    
    public boolean isReady() {
    	return isReady;
    }
    
    public void setReady(boolean ready) {
    	isReady = ready;
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
		if(theColor != Color.NONE && theColor != Color.HINT) {
			this.color = theColor;
		}
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
    
    @Override
    public String toString() {
    	return name +" : " + color;
    }
 
}
