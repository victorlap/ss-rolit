package rolit;

import java.util.Arrays;
import java.util.Observable;

import rolit.client.ComputerPlayer;
import rolit.client.SmartStrategy;

/**
 * Class that initializes, maintains and analyzes the board of the RolIt game.
 * 
 * @author Victor Lap and Yuri van Midden
 * @version 1.6.0
 */

public class Board extends Observable {

	public static final int DIM = 8; // MUST BE EVEN AND 2-26
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
	} 
	
	public void init() {
		setField((DIM/2)-1, (DIM/2)-1, Color.RED);
		setField((DIM/2), (DIM/2)-1, Color.YELLOW);
		setField((DIM/2)-1, (DIM/2), Color.BLUE);
		setField((DIM/2), (DIM/2), Color.GREEN);
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
	public int index(int col, int row) {
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
	public boolean isEmptyField(int col, int row) {
		return isEmptyField(index(col, row));
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
	public Color getField(int col, int row) {
		return getField(index(col, row));
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
	public void setField(int col, int row, Color color) {
		setField(index(col, row), color);
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
	
	/**
	 * Does the move with <code>setField</code>, first converting the rows and columns into an index to test for.
	 * @param col to use
	 * @param row to use
	 * @param color to do the move for
	 */
	public void doMove(int col, int row, Color color) {
		doMove(index(col, row), color);
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
			
			/** Fills the fields lying between an existing field with the same color, 
			 * IF it exists. This case checks for fields in the <b>North</b> direction.	
			 */
			if(checkNorth(field, color) >= 0) {
				int newField = checkNorth(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField -= DIM;
					setField(tempField, color);
				}
			}
			
			/** Fills the fields lying between an existing field with the same color, 
			 * IF it exists. This case checks for fields in the <b>NorthEast</b> direction.	
			 */
			if(checkNorthEast(field, color) >= 0) {
				int newField = checkNorthEast(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField -= (DIM - 1);
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, 
			 * IF it exists. This case checks for fields in the <b>East</b> direction.	
			 */
			if(checkEast(field, color) >= 0) {
				int newField = checkEast(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField += 1;
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, 
			 * IF it exists. This case checks for fields in the <b>SouthEast</b> direction.	
			 */
			if(checkSouthEast(field, color) >= 0) {
				int newField = checkSouthEast(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField += (DIM + 1);
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, 
			 * IF it exists. This case checks for fields in the <b>South</b> direction.	
			 */
			if(checkSouth(field, color) >= 0) {
				int newField = checkSouth(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField += DIM;
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, 
			 * IF it exists. This case checks for fields in the <b>SouthWest</b> direction.	
			 */
			if(checkSouthWest(field, color) >= 0) {
				int newField = checkSouthWest(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField += (DIM -1);
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, 
			 * IF it exists. This case checks for fields in the <b>West</b> direction.	
			 */
			if(checkWest(field, color) >= 0) {
				int newField = checkWest(field, color);
				int tempField = field;
				while(tempField != newField) {
					tempField -= 1;
					setField(tempField, color);
				}
			}

			/** Fills the fields lying between an existing field with the same color, 
			 * IF it exists. This case checks for fields in the <b>NorthWest</b> direction.	
			 */
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
	 * Returns true is a move is valid, by converting the columns and rows into an index to test with <code>checkMove</code>.
	 * @param col to test for
	 * @param row to test for
	 * @param color to execute the test on
	 * @return <code>true</code> if the checked field lies bordering to a field that <code>!Color.NONE</code>
	 */
	public boolean checkMove(int col, int row, Color color) {
		return checkMove(index(col, row), color);
	}
	
	/**
	 * Returns true if a move is valid i.e. it lies next to another (yet occupied) field.
	 * @param field to check available options
	 * @param color to check the fields for
	 * @return <code>true</code> if the checked field lies bordering to a field that <code>!Color.NONE</code>
	 */
	public boolean checkMove(int field, Color color) {
		
		if(isEmptyField(field) && isBordering(field)) {
			//System.out.println(hasFlippableField(color));
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
	 * Returns a hint derived from the strategy used.
	 * @param color to find a hint for
	 * @return <code>int</code> with the hint index
	 */
	public int getHint(Color color) {
		ComputerPlayer cp = new ComputerPlayer(color, new SmartStrategy());
		return cp.determineMove(this);
		
	}
	
	/**
	 * Check if the chosen field lies next to a colored field, by testing the conditions for every color.
	 * @param field to check
	 * @return <code>true</code> if field lies next to another field with <br><code>!Color.NONE</code>
	 */
	public boolean isBordering(int field) {
		return ((checkNorth(field, Color.RED) >= 0 ||
				checkNorthEast(field, Color.RED) >= 0 ||
				checkEast(field, Color.RED) >= 0 ||
				checkSouthEast(field, Color.RED) >= 0 ||
				checkSouth(field, Color.RED) >= 0 ||
				checkSouthWest(field, Color.RED) >= 0 ||
				checkWest(field, Color.RED) >= 0 ||
				checkNorthWest(field, Color.RED) >= 0) ||
				
				(checkNorth(field, Color.YELLOW) >= 0 ||
				checkNorthEast(field, Color.YELLOW) >= 0 ||
				checkEast(field, Color.YELLOW) >= 0 ||
				checkSouthEast(field, Color.YELLOW) >= 0 ||
				checkSouth(field, Color.YELLOW) >= 0 ||
				checkSouthWest(field, Color.YELLOW) >= 0 ||
				checkWest(field, Color.YELLOW) >= 0 ||
				checkNorthWest(field, Color.YELLOW) >= 0) ||
				
				(checkNorth(field, Color.GREEN) >= 0 ||
				checkNorthEast(field, Color.GREEN) >= 0 ||
				checkEast(field, Color.GREEN) >= 0 ||
				checkSouthEast(field, Color.GREEN) >= 0 ||
				checkSouth(field, Color.GREEN) >= 0 ||
				checkSouthWest(field, Color.GREEN) >= 0 ||
				checkWest(field, Color.GREEN) >= 0 ||
				checkNorthWest(field, Color.GREEN) >= 0) ||
				
				(checkNorth(field, Color.BLUE) >= 0 ||
				checkNorthEast(field, Color.BLUE) >= 0 ||
				checkEast(field, Color.BLUE) >= 0 ||
				checkSouthEast(field, Color.BLUE) >= 0 ||
				checkSouth(field, Color.BLUE) >= 0 ||
				checkSouthWest(field, Color.BLUE) >= 0 ||
				checkWest(field, Color.BLUE) >= 0 ||
				checkNorthWest(field, Color.BLUE) >= 0));
	}

	/**
	 * Returns if the color has the most fields on the <code>Board</code>
	 * @param color
	 * @return <code>true</code> if the color has the most fields
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
	public int countFields(Color color) {
		int count = 0;
		
		for(int i = 0; i < fields.length; i++) {
			if(getField(i) == color) {
				count++;
			}
		} 
		return count;
	}
	
	/**
	 * Creates an array of booleans with the fields that would flip other's fields if picked.				
	 * @param color to get the flippable fields for
	 * @return <code>boolean[]</code> with DIM*DIM entries that shows at <code>index</code> which fields are flippable.
	 */
	public boolean[] getFlippableFields(Color color) {
		
		boolean[] flippableFields = new boolean[DIM*DIM];
		
		for (int i = 0; i < fields.length; i++) {
			/*if(i == 44) {
				System.out.println((i - checkNorth(i, color) >= DIM*2) && (checkNorth(i,color) >= 0));
				System.out.println((i - checkNorthEast(i, color) >= DIM * 2-2) && (checkNorthEast(i,color) >= 0));
				System.out.println((checkEast(i, color) - i >= 2));
				System.out.println((checkSouthEast(i, color) - i >= 18));
				System.out.println((checkSouth(i, color) - i >= 16));
				System.out.println((checkSouthWest(i, color) - i >= 14));
				System.out.println((i - checkWest(i, color) >= 2) && (checkWest(i, color) >= 0));
				System.out.println((i - checkNorthWest(i, color) >= 18) && (checkNorthWest(i, color) >= 0));
			}*/
			if (isEmptyField(i) && isBordering(i) && 
					(
						((i - checkNorth(i, color) >= DIM * 2) && (checkNorth(i,color) >= 0)) ||
						((i - checkNorthEast(i, color) >= DIM * 2-2) && (checkNorthEast(i,color) >= 0)) ||
						(checkEast(i, color) - i >= 2) ||
						(checkSouthEast(i, color) - i >= DIM * 2 + 2) ||
						(checkSouth(i, color) - i >= DIM * 2) ||
						(checkSouthWest(i, color) - i >= DIM * 2 - 2) ||
						((i - checkWest(i, color) >= 2) && (checkWest(i, color) >= 0)) ||
						((i - checkNorthWest(i, color) >= DIM * 2 + 2) && (checkNorthWest(i, color) >= 0)))
					) {
				flippableFields[i] = true;
				//System.out.print(i + ", ");
				
			}
		}
	    //System.out.println();
		return flippableFields;
	}

	/**
	 * Checks whether a color can do any move that generates field to be flipped, using <code>getFlippableFields</code>
	 * @param color to check for
	 * @return <code>true</code> if a player can do any move that makes fields flip.
	 */
	private boolean hasFlippableField(Color color) {
		for(int i = 0; i < fields.length; i++) {
			if (getFlippableFields(color)[i]) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculate the quality of every field, i.e. calculate the amount of flips that every move will generate.
	 * @param color to test qualities for
	 * @return <code>int[]</code> with the amounts of fields that every move flips.
	 */
	public int[] getQuality(Color color) {
		
		int[] qualityFields = new int[DIM * DIM];
		boolean[] tempFlippableFields = this.getFlippableFields(color);
		
		for (int i = 0; i < qualityFields.length; i++) {
			/*if(i == 43) {
			System.out.println(((checkNorth(i, color) >= 0) 		? ((i - checkNorth(i, color)) / DIM)				: 0));
			System.out.println(((checkNorthEast(i, color) >= 0) 	? (i - checkNorthEast(i, color) / (DIM - 1)) 	: 0));
			System.out.println(((checkEast(i, color) >= 0) 		? (checkEast(i, color) - i)						: 0));
			System.out.println(((checkSouthEast(i, color) >= 0)	? (checkSouthEast(i, color) - i / (DIM + 1)) 	: 0));
			System.out.println(((checkSouth(i, color) >= 0)		? (checkSouth(i, color) - i / DIM)				: 0));
			System.out.println((checkSouthWest(i, color) >= 0)    ? (checkSouthWest(i, color) - i / (DIM - 1))    : 0);
			System.out.println((checkWest(i, color) >= 0)         ? (i - checkWest(i, color))                     : 0);
			System.out.println((checkNorthWest(i, color) >= 0)    ? (i - checkNorthWest(i, color) / (DIM + 1))    : 0);
		}*/
			if (tempFlippableFields[i]) {
				qualityFields[i] = 
					((checkNorth(i, color) >= 0) 		? ((i - checkNorth(i, color)) / DIM) 			: 0) +
					((checkNorthEast(i, color) >= 0) 	? ((i - checkNorthEast(i, color)) / (DIM - 1)) 	: 0) +
					((checkEast(i, color) >= 0) 		? (checkEast(i, color) - i)						: 0) +
					((checkSouthEast(i, color) >= 0)	? ((checkSouthEast(i, color) - i) / (DIM + 1)) 	: 0) +
					((checkSouth(i, color) >= 0)		? ((checkSouth(i, color) - i) / DIM)			: 0) +
					((checkSouthWest(i, color) >= 0)    ? ((checkSouthWest(i, color) - i) / (DIM - 1))  : 0) +
					((checkWest(i, color) >= 0)         ? (i - checkWest(i, color))                     : 0) +
					((checkNorthWest(i, color) >= 0)    ? ((i - checkNorthWest(i, color)) / (DIM + 1))  : 0);
			} else {
				qualityFields[i] = 0;
			}
		}
		//System.out.println(Arrays.toString(qualityFields));
		return qualityFields;
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
				//if(field == 44) System.out.println(field +""+ color);
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

	/**
	 * Check if a field in on the board, i.e. whether it is one of the 64 indexes of <code>fields</code>
	 * @param field to test
	 * @return <code>true</code> if the tested field is a genuine field.
	 */
	private boolean onBoard(int field) {
		return field >= 0 && field < DIM*DIM;
	}
}
