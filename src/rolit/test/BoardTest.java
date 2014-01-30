package rolit.test;

import rolit.*;

public class BoardTest {

	public Board b;
	/** Number of errors. */
	private int errors;
	/** Notice belonging to test method. */
	private boolean isPrinted;
	/** Indication that an errors was found in test method. */
	private String description;

	/**
	 * Insert the tests that have to be done here
	 */
	public int runTest(){
		newBoard();
		doIllegalMove();
		newBoard();
		doLegalMove();
		newBoard();
		testForEmptyField();
		newBoard();
		testGridAndIndex();
		newBoard();
		testColorAtGridAndIndex();
		newBoard();
		testCheckMoveGrid();
		newBoard();
		testCopyBoard();
		newBoard();
		testIsFull();
		newBoard();
		testCountFields();
		//newBoard();
		//testHasMostFields();
		newBoard();
		testGetQuality();
		
		if (errors == 0) {
            System.out.println("    OK");
        }
        return errors;
	}
	
	public void newBoard(){
		b = new Board();
	}
	
	/**
	 * This method fills a random amount of fields, at random places, with random colors.
	 */
	private void fillBoardRandomly() {
		
		int amount = ((int) (Math.random()*(Board.DIM * Board.DIM)));
		
		/*
		  @ loop_invariant i < (b.DIM*b.DIM);
		 */
		for (int i = 0; i < amount; i++){
			
			Color newColor = Color.fromInt((int) (Math.random()*4));
			int newField = (int) (Math.random()*(Board.DIM*Board.DIM));
			
			b.setField(newField, newColor);
		}
	}
	
	/**
	 * Fills the board completely with random colors
	 */
	private void fillBoardCompletely() {
		
		for (int i = 0; i < Board.DIM*Board.DIM; i++){
			Color newColor = Color.fromInt((int) (Math.random()*4));
			b.setField(i, newColor);
		}
	}
	
	/**
	 * Fills the board with amount moves
	 * @param amount
	 */
	/*
	  @ requires amount < Board.DIM*Board.DIM
	 */
	private void fillBoardGameLike(int amount) {
		
		b.init();
		Color color = Color.RED;
		for (int i = 0; i < amount; i++){
			b.doMove(b.getHint(color), color);
			color = color.next();
		}
	}
	
	
	// ============= Tests ==========================
	
	/**
	 * Tests whether illegal moves are denied.
	 */
	public void doIllegalMove(){
		beginTest("doIllegalMove");
		if (b.isEmptyField(0, 0) && b.isEmptyField(0, 1) && b.isEmptyField(1, 0) && b.isEmptyField(1, 1)){
			if (b.checkMove(0, Color.RED)){
				b.setField(0, Color.RED);
			}
		}
		assertEquals("Test if illegal moves are denied", b.getField(0), Color.NONE);
	}
	
	/**
	 * Tests whether a legal move is done, when it's supposed to :)
	 */
	public void doLegalMove(){
		beginTest("doLegalMove");
		b.init();
		boolean[] temp = b.getFlippableFields(Color.RED);
		int moveField = 0;
		for (int i = 0; i < temp.length; i++){
			if (temp[i]){
				b.doMove(i, Color.RED);
				moveField = i;
				break;
			}
		}
		assertEquals("Test if legal moves are allowed", Color.RED, b.getField(moveField));
	}
	
	/**
	 * Tests for every field (that satisfies that it has Color.NONE) if isEmptyField returns true.
	 */
	public void testForEmptyField(){
		beginTest("isEmptyField method");
		/*
		  @ loop_invariant i < (b.DIM * b.DIM);
		  @ loop_invariant i >= 0;
		  @ loop_invariant (b.DIM * b.DIM) > 0;
		 */
		for (int i = 0; i < (Board.DIM * Board.DIM); i ++){
			if (b.isEmptyField(i)){
						assertEquals("Test if field "+i+" with Color.NONE is an empty field", true, (b.getField(i) == Color.NONE));
			}
		}
	}
	
	/**
	 * Checks whether the index of the fields correspond when determined by their rows and columns.
	 */
	public void testGridAndIndex() {
		beginTest("index(row,col) method");
		for(int i = 0; i < Board.DIM*Board.DIM; i++){
			int testfield = i; //(int) Math.random()*(b.DIM * b.DIM);
			assertEquals("Test if grid id is same as index for a random field", 
					true, 
					(b.index (b.indexToCol(testfield), b.indexToRow(testfield))) == testfield);
		}
	}
	
	/**
	 * Tests for every field if the grid approach result in the same value as the index approach
	 */
	public void testColorAtGridAndIndex() {
		beginTest("getField methods");
		
		fillBoardRandomly();
		
		boolean foundMistake = false;
		
		for (int i = 0; i < Board.DIM*Board.DIM; i++) {
			if (!b.getField(i).equals(b.getField(b.indexToCol(i), b.indexToRow(i)))){
				foundMistake = true;
				break;
			}
		}
		
		assertEquals("Test if colors correspond", false, foundMistake);
	}
	
	/**
	 * Tests if grid approach for checkMove returns same as index approach
	 */
	public void testCheckMoveGrid() {
		beginTest("checkMove (row,col) method");
		
		fillBoardGameLike(20);
		
		boolean foundMistake = false;
		
		for (int i = 0; i < Board.DIM*Board.DIM; i++) {
			if (b.checkMove(i, Color.RED) != b.checkMove(b.indexToCol(i), b.indexToRow(i), Color.RED)) {
				foundMistake = true;
			}
		}
		
		assertEquals ("Test if grid approach for checkMove returns same as index approach", false, foundMistake);
		
		
	}
	
	/**
	 * Fills the board randomly, then makes a copy and checks if the fields correspond.
	 */
	public void testCopyBoard() {
		beginTest("copy of board");
		
		fillBoardRandomly();
		
		Board copy = b.copy();
		boolean foundMistake = false;
		
		for (int i = 0; i < Board.DIM*Board.DIM; i++){ 	
			if (!b.getField(i).toString().equals(copy.getField(i).toString())){
				foundMistake = true;
			}
		}
		
		assertEquals("Test if b.copy equals b", false, foundMistake);
	}
	
	/**
	 * Tests isFull method after completely filling the board.
	 */
	public void testIsFull() {
		beginTest("isFull method");
		
		fillBoardCompletely();
		
		assertEquals("Test if isFull works after filling board", true, b.isFull());
	}
	
	/**
	 * Test the countFields method against an own method.
	 */
	public void testCountFields() {
		beginTest("testCountFields method");
		
		fillBoardRandomly();
		
		int red = 0;
		int yellow = 0;
		int green = 0;
		int blue = 0;
		
		for (int i = 0; i < Board.DIM*Board.DIM; i++) {
			if (b.getField(i) == Color.RED){
				red++;
			} else if (b.getField(i) == Color.YELLOW){
				yellow++;
			} else if (b.getField(i) == Color.GREEN){
				green++;
			} else if (b.getField(i) == Color.BLUE){
				blue++;
			}
		}
		
		boolean passed = true;
		
		if (!(b.countFields(Color.RED) == red && 
				b.countFields(Color.YELLOW) == yellow &&
				b.countFields(Color.GREEN) == green &&
				b.countFields(Color.BLUE) == blue)) {
			passed = false;
		}
		
		assertEquals("Test is countFields works", true, passed);
	}
	
	/**
	 * Tests if hasMostFields returns the same values as the manual method
	 */
	public void testHasMostFields() {
		beginTest("hasMostFields method");
		
		fillBoardRandomly();
		
		int red = 0;
		int yellow = 0;
		int green = 0;
		int blue = 0;
		
		for (int i = 0; i < Board.DIM*Board.DIM; i++) {
			if (b.getField(i) == Color.RED){
				red++;
			} else if (b.getField(i) == Color.YELLOW){
				yellow++;
			} else if (b.getField(i) == Color.GREEN){
				green++;
			} else if (b.getField(i) == Color.BLUE){
				blue++;
			}
		}
		
		System.out.println("Red: "+red+", yellow: "+yellow+", green: "+green+ ", blue: "+blue);
		
		boolean passed = true;
		
		if ((red >= yellow && red >= green && red >= blue) != b.hasMostFields(Color.RED)) {
			passed = false;
			System.out.println("Fout bij RED");
		} else if ((yellow >= green && yellow >= blue && yellow >= red) != b.hasMostFields(Color.YELLOW)) {
			passed = false;
			System.out.println("Fout bij YELLOW");
		} else if ((green >= blue && green >= red && green >= yellow) != b.hasMostFields(Color.GREEN)){
			passed = false;
			System.out.println("Fout bij GREEN");
		} else if ((blue >= red && blue >= yellow && blue >= green) != b.hasMostFields(Color.BLUE)) {
			passed = false;
			System.out.println("Fout bij BLUE");
		}
		
		assertEquals ("Test if hasMostFields works", true, passed);
	}
	
	/**
	 * Tests if getQuality gives the right values
	 */
	public void testGetQuality() {
		beginTest("getQuality method");
		
		fillBoardGameLike(20);
				
		/*for (int i = 0; i < Board.DIM*Board.DIM; i++) {
			System.out.println("Field " + i + " - " + b.getField(i).toString());
		}*/
		
		// TODO evaluate the quality stuff!!!
		
	}
	
	//=============== AssertEquals en co ===================
	
	private void beginTest(String text) {
        description = text;
        // the description hasn't been printed yet
        isPrinted = false;
    }

	/**
     * Tests if the resulting value of a tested expression equals the 
     * expected (correct) value. This implementation prints both values, 
     * with an indication of what was tested, to the standard output. The 
     * implementation does not actually do the comparison.
     * 
     * @author Arend Rensink
     */
	private void assertEquals(String text, Object expected, Object result) {
		boolean equal;
		// tests equality between expected and result
		// accounting for null
		if (expected == null) {
			equal = result == null;
		} else {
			equal = result != null && expected.equals(result);
		}
		if (!equal) {
			// prints the description if necessary
			if (!isPrinted) {
				System.out.println("    Test: " + description);
				// now the description is printed
				isPrinted = true;
			}
			System.out.println("        " + text);
			System.out.println("            Expected:  " + expected);
			System.out.println("            Result: " + result);
			errors++;
		}
	}
	
	public static void main(String[] args){
		new BoardTest().runTest();
	}
}
