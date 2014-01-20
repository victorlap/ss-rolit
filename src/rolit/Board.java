package rolit;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Class that initializes, maintains and analyzes the board of the RolIt game.
 * 
 * @author Victor Lap and Yuri van Midden
 * @version 1.2.0
 */

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
	 * Creates a new copy of the board from the current <code>fields</code> array.
	 * @return <code>Board</code>
	 */
	public Board copy() {
		Board b = new Board();
		for (int i = 0; i < fields.length; i++) {
            b.fields[i] = this.fields[i];
        }
		return b;
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
		setChanged();
		notifyObservers();
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
	 * Returns <code>true</code> when every field is <br><code>!Color.NONE</code>
	 * @return <b>true</b> if every field is <code>!Color.NONE</code>
	 */
	public boolean isFull() {
		for (int i=0; i < fields.length; i++){
			if (fields[i] == Color.NONE) {
				return false;
			}
		}
		return true;
	}
	
	/*@ requires checkMove == true;
	  
	 */
	/**
	 * Does the move with <code>setField</code>, assuming it can be done and does not violate the rules i.e. there is at least one bordering field that has a color.
	 * @param field to change <code>Color.VALUE</code> of
	 * @param color to change the field to.
	 */
	public void doMove(int field, Color color) {
		if(checkMove(field, color)) {
			setField(field, color);
			
			/** Fills the fields lying between an existing field with the same color, IF it exists. This case checks for fields in the North direction.	*/
			if(checkNorth(field, color) >= 0) {
				int newField = checkNorth(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField -= DIM;
					setField(tempField, color);
				}
			}
			
			/** Fills the fields lying between an existing field with the same color, IF it exists. This case checks for fields in the NorthEast direction.	*/
			if(checkNorthEast(field, color) >= 0) {
				int newField = checkNorthEast(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField -= (DIM - 1);
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, IF it exists. This case checks for fields in the East direction.	*/
			if(checkEast(field, color) >= 0) {
				int newField = checkEast(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField += 1;
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, IF it exists. This case checks for fields in the SouthEast direction.	*/
			if(checkSouthEast(field, color) >= 0) {
				int newField = checkSouthEast(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField += (DIM + 1);
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, IF it exists. This case checks for fields in the South direction.	*/
			if(checkSouth(field, color) >= 0) {
				int newField = checkSouth(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField += DIM;
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, IF it exists. This case checks for fields in the SouthWest direction.	*/
			if(checkSouthWest(field, color) >= 0) {
				int newField = checkSouthWest(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField += (DIM -1);
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, IF it exists. This case checks for fields in the West direction.	*/
			if(checkWest(field, color) >= 0) {
				int newField = checkWest(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField -= 1;
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, IF it exists. This case checks for fields in the NorthWest direction.	*/
			if(checkNorthWest(field, color) >= 0) {
				int newField = checkNorthWest(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField -= (DIM + 1);
					setField(tempField, color);
				}
			}
			
		} 
		
	}

	/**
	 * Returns true if a move is valid i.e. it lies next to another (yet occupied) field.
	 * @param field to check available options
	 * @param color to check the fields for
	 * @return <code>true</code> if the checked field lies bordering to a field that <code>!Color.NONE</code>
	 */
	public boolean checkMove(int field, Color color) {
		
		if(isEmptyField(field) && isBordering(field, color)) {
			System.out.println(hasFlippableField(color));
			if(getFlippableFields(color)[field]) {
				return true;
			} else if (hasFlippableField(color)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checkt of het veld naast een ander veld ligt
	 * @param field
	 * @param color
	 * @return
	 */
	private boolean isBordering(int field, Color color) {
		return (checkNorth(field, color) >= 0 ||
				checkNorthEast(field, color) >= 0 ||
				checkEast(field, color) >= 0 ||
				checkSouthEast(field, color) >= 0 ||
				checkSouth(field, color) >= 0 ||
				checkSouthWest(field, color) >= 0 ||
				checkWest(field, color) >= 0 ||
				checkNorthWest(field, color) >= 0);
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

	
	
					
	public boolean[] getFlippableFields(Color color) {
		
		boolean[] flippableFields = new boolean[64];
		
		for (int i = 0; i < fields.length; i++) {
			if (isEmptyField(i) && isBordering(i, color) && 
					(
						(i - checkNorth(i, color) >= DIM*2) && (checkNorth(i,color) >= 0) ||
						(i - checkNorthEast(i, color) >= DIM * 2-2) && (checkNorth(i,color) >= 0)||
						(checkEast(i, color) - i >= 2) /*&& (checkEast(i,color) >= 0)*/ ||
						(checkSouthEast(i, color) - i >= 18) /*&& (checkSouthEast(i,color) >= 0)*/ ||
						(checkSouth(i, color) - i >= 16) /*&& (checkSouth(i,color) >= 0)*/ ||
						(checkSouthWest(i, color) - i >= 14) /*&& (checkSouthWest(i,color) >= 0)*/ ||
						((i - checkWest(i, color) >= 2) && (checkWest(i, color) >= 0)) ||
						((i - checkNorthWest(i, color) >= 18) && (checkNorthWest(i, color) >= 0)))
					) {
				flippableFields[i] = true;
				System.out.print(i + ", ");
				
			}
		}
	    System.out.println();
		return flippableFields;
	}

	private boolean hasFlippableField(Color c) {
		for(int i = 0; i < fields.length; i++) {
			if (getFlippableFields(c)[i]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Recursively finds, for the <b>North</b> direction, the first field that has the same <code>Color</code> as the new desired <code>Color</code>. This works, because it doesn't need to find more fields with the same <code>Color</code> in that direction.
	 * @param field to start from
	 * @param color that has to be found
	 * @return <code>int</code> with the first field that meets the condition (same <code>Color</code> as the desired <code>Color</code>), <br><code>-1</code> if there is no such field.
	 */
	private int checkNorth(int field, Color color) {
		int north = field - DIM;
		
		while(onBoard(north) && getField(north) != Color.NONE) {
			if (getField(north) == color) {
				return north;
			}
			return checkNorth(north, color);
		}
		return -1;
	}

	/**
	 * Recursively finds, for the <b>NorthEast</b> direction, the first field that has the same <code>Color</code> as the new desired <code>Color</code>. This works, because it doesn't need to find more fields with the same <code>Color</code> in that direction.
	 * @param field to start from
	 * @param color that has to be found
	 * @return <code>int</code> with the first field that meets the condition (same <code>Color</code> as the desired <code>Color</code>), <br><code>-1</code> if there is no such field.
	 */
	private int checkNorthEast(int field, Color color) {
		int northeast = field - DIM + 1;
		
		while(onBoard(northeast) && getField(northeast) != Color.NONE && (northeast % DIM != 0)) {
			if (getField(northeast) == color) {
				return northeast;
			}
			return checkNorthEast(northeast, color);
		}
		return -1;
	}

	/**
	 * Recursively finds, for the <b>East</b> direction, the first field that has the same <code>Color</code> as the new desired <code>Color</code>. This works, because it doesn't need to find more fields with the same <code>Color</code> in that direction.
	 * @param field to start from
	 * @param color that has to be found
	 * @return <code>int</code> with the first field that meets the condition (same <code>Color</code> as the desired <code>Color</code>), <br><code>-1</code> if there is no such field.
	 */
	private int checkEast(int field, Color color) {
		int east = field + 1;
		
		while(onBoard(east) && getField(east) != Color.NONE && (east % DIM != 0)) {
			if (getField(east) == color) {
				return east;
			}
			return checkEast(east, color);
		}
		return -1;
	}
	
	/**
	 * Recursively finds, for the <b>SouthEast</b> direction, the first field that has the same <code>Color</code> as the new desired <code>Color</code>. This works, because it doesn't need to find more fields with the same <code>Color</code> in that direction.
	 * @param field to start from
	 * @param color that has to be found
	 * @return <code>int</code> with the first field that meets the condition (same <code>Color</code> as the desired <code>Color</code>), <br><code>-1</code> if there is no such field.
	 */
	private int checkSouthEast(int field, Color color) {
		int southEast = field + DIM + 1;
		
		while(onBoard(southEast) && getField(southEast) != Color.NONE && (southEast % DIM != 0)) {
			if (getField(southEast) == color) {
				return southEast;
			}
			return checkSouthEast(southEast, color);
		}
		return -1;
	}

	/**
	 * Recursively finds, for the <b>South</b> direction, the first field that has the same <code>Color</code> as the new desired <code>Color</code>. This works, because it doesn't need to find more fields with the same <code>Color</code> in that direction.
	 * @param field to start from
	 * @param color that has to be found
	 * @return <code>int</code> with the first field that meets the condition (same <code>Color</code> as the desired <code>Color</code>), <br><code>-1</code> if there is no such field.
	 */
	private int checkSouth(int field, Color color) {
		int south = field + DIM;
		
		while(onBoard(south) && getField(south) != Color.NONE) {
			if (getField(south) == color) {
				return south;
			}
			return checkSouth(south, color);
		}
		return -1;
	}
	
	/**
	 * Recursively finds, for the <b>SouthWest</b> direction, the first field that has the same <code>Color</code> as the new desired <code>Color</code>. This works, because it doesn't need to find more fields with the same <code>Color</code> in that direction.
	 * @param field to start from
	 * @param color that has to be found
	 * @return <code>int</code> with the first field that meets the condition (same <code>Color</code> as the desired <code>Color</code>), <br><code>-1</code> if there is no such field.
	 */
	private int checkSouthWest(int field, Color color) {
		int southwest = field + DIM - 1;
		
		while(onBoard(southwest) && getField(southwest) != Color.NONE && (((southwest + 1) % DIM) != 0)) {
			if (getField(southwest) == color) {
				return southwest;
			}
			return checkSouthWest(southwest, color);
		}
		return -1;
	}
	
	/**
	 * Recursively finds, for the <b>West</b> direction, the first field that has the same <code>Color</code> as the new desired <code>Color</code>. This works, because it doesn't need to find more fields with the same <code>Color</code> in that direction.
	 * @param field to start from
	 * @param color that has to be found
	 * @return <code>int</code> with the first field that meets the condition (same <code>Color</code> as the desired <code>Color</code>), <br><code>-1</code> if there is no such field.
	 */
	private int checkWest(int field, Color color) {
		int west = field - 1;
		
		while(onBoard(west) && getField(west) != Color.NONE && (((west + 1) % DIM) != 0)) {
			if (getField(west) == color) {
				return west;
			}
			return checkWest(west, color);
		}
		return -1;
	}
	
	/**
	 * Recursively finds, for the <b>NorthWest</b> direction, the first field that has the same <code>Color</code> as the new desired <code>Color</code>. This works, because it doesn't need to find more fields with the same <code>Color</code> in that direction.
	 * @param field to start from
	 * @param color that has to be found
	 * @return <code>int</code> with the first field that meets the condition (same <code>Color</code> as the desired <code>Color</code>), <br><code>-1</code> if there is no such field.
	 */
	private int checkNorthWest(int field, Color color) {
		int northWest = field - DIM - 1;
		
		while(onBoard(northWest) && getField(northWest) != Color.NONE && (((northWest + 1) % DIM) != 0)) {
			if (getField(northWest) == color) {
				return northWest;
			}
			return checkNorthWest(northWest, color);
		}
		return -1;
	}

	private boolean onBoard(int field) {
		return field >= 0 && field < 64;
	}
}
