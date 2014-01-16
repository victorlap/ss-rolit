package rolit;

import java.util.Observable;


public class Board extends Observable {

	public static final int DIM = 8;
	public int lastChangedField;

	/**
	 * The DIM by DIM fields of the Tic Tac Toe board. See NUMBERING for the
	 * coding of the fields.
	 */
	private Color[] fields;

	/**
	 * Constructs a new board with the four initial RolIt balls in the middle.
	 */
	public Board() {
		fields = new Color[DIM * DIM];
		for (int i = 0; i< fields.length; i++) {
			fields[i] = Color.NONE;
		}
		
		setField(3, 3, Color.RED);
		setField(3, 4, Color.YELLOW);
		setField(4, 3, Color.BLUE);
		setField(4, 4, Color.GREEN);
	} 
	
	/**
	 * Returns the <code>index</code> of a field in <code>row</code>,<code>col</code>
	 * @param row of the desired field
	 * @param col of the desired field
	 * @return <b>int</b> with the index of the field
	 */
	public int index(int row, int col) {
		return DIM * row + col;
	}
	
	/**
	 * Returns <code>true</code> if field at index <code>field</code> has <code>Color.NONE</code>
	 * @param field to test
	 * @return <code>true</code> if field has <code>Color.NONE</code>
	 */
	public boolean isEmptyField(int field) {
		return getField(field) == Color.NONE;
	}
	
	/**
	 * Returns <code>true</code> if field in <code>row</code>,<code>col</code> has <code>Color.NONE</code>
	 * @param row of desired field
	 * @param col of desired field
	 * @return <code>true</code> if field has <code>Color.NONE</code>
	 */
	public boolean isEmptyField(int row, int col) {
		return isEmptyField(index(row, col));
	}
	
	/**
	 * Returns value of field at index <code>field</code>
	 * @param field to test
	 * @return <code>Color</code> of the tested field
	 */
	public Color getField(int field) {
		return fields[field];
	}
	
	/**
	 * Returns value of field at <code>row</code>,<code>col</code>
	 * @param row of desired field
	 * @param col of desired field
	 * @return <code>Color</code> of tested field
	 */
	public Color getField(int row, int col) {
		return getField(index(row, col));
	}

	/**
	 * Sets a field to the desired <code>Color</code>
	 * @param field to change value of
	 * @param color the new value
	 */
	public void setField(int field, Color color) {
		fields[field] = color;
		lastChangedField = field;
	}
	
	/**
	 * Sets a field to the desired <code>Color</code>
	 * @param row row of desired field to change value of
	 * @param col col of desired field to change value of
	 * @param color the new value
	 */
	public void setField(int row, int col, Color color) {
		setField(index(row, col), color);
	}
	
	/**
	 * Return the last changed field
	 * @return <code>int
	 */
	public int getlastChangedField() {
		return lastChangedField;
	}

	/**
	 * Check if game is over
	 * @return <code>true</code> if <code>isFull()</code> returns <code>true</code>
	 */
	public boolean gameOver() {
		return isFull();
	}
	
	/**
	 * Check whether a specific player is the winner
	 * @param color to test
	 * @return <code>true</code> if the tested player is the winner
	 */
	public boolean isWinner(Color color) {
		return hasMostFields(color);
	}

	/**
	 * Check whether the game has a winner
	 * @return <code>true</code> if the game has a winner
	 */
	public boolean hasWinner() {
		return isWinner(Color.GREEN) || 
				isWinner(Color.RED)  || 
				isWinner(Color.BLUE) || 
				isWinner(Color.YELLOW);
	}
	
	/**
	 * Returns <code>true</code> when every field is <code>Color.NONE</code>
	 * @return <b>true</b> if every field is <code>Color.NONE</code>
	 */
	public boolean isFull() {
		for (int i=0; i<fields.length; i++){
			if (fields[i] == Color.NONE) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns if the color has the most fields on the <code>Board</code>
	 * @param color
	 * @return boolean if the color has the most fields
	 */ 
	private boolean hasMostFields(Color color) {
		int green = countFields(Color.GREEN);
		int blue = countFields(Color.GREEN);
		int yellow = countFields(Color.GREEN);
		int red = countFields(Color.GREEN);

		if(color == Color.RED) {
			return red > green && red > blue && red > yellow;
		} else if(color == Color.BLUE) {
			return blue > green && blue > red && blue > yellow;
		} else if(color == Color.GREEN) {
			return green > red && green > blue && green > yellow;
		} else {
			return yellow > green && yellow > blue && yellow > red;
		}
	}

	/**
	 * Count the amount of fields a player has with its own color
	 * @param color to test
	 * @return <code>int</code> with the amount of fields a player has
	 */
	private int countFields(Color color) {
		int count = 0;
		
		for(int i = 0; i < fields.length; i++) {
			if(getField(i) == color) {
				count++;
			}
		}
		return count;
	}

}
