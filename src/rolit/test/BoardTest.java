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
		
		if (errors == 0) {
            System.out.println("    OK");
        }
        return errors;
	}
	
	public void newBoard(){
		b = new Board();
	}
	
	// Tests
	
	public void doIllegalMove(){
		if (b.isEmptyField(0, 0) && b.isEmptyField(0, 1) && b.isEmptyField(1, 0) && b.isEmptyField(1, 1)){
			b.doMove(0, Color.RED);
		}
		assertEquals("Test if illegal moves are denied", b.getField(0), Color.NONE);
	}
	
	public void doLegalMove(){
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
	
	// AssertEquals

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
