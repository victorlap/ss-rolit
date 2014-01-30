package rolit.test;

import rolit.Color;
import rolit.Player;

public class PlayerTest {
	
	public Player p;
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
		
		newPlayer();
		testSetNames();
		newPlayer();
		testSetColor();
		
		if (errors == 0) {
            System.out.println("    OK");
        }
		
        return errors;
	}
	
	public void newPlayer(){
		p = new Player();
	}
	
	
	// ============= Tests ==========================
	
	/**
	 * Tests setName method
	 */
	public void testSetNames() {
		beginTest("setName method");
		
		String orig = p.getName();
		if (orig.equals("aRandomStringThatIsNeverAPlayerName")) {
			p.setName("anotherRandomNameAPlayerNeverHas");
		} else {
			p.setName("aRandomStringThatIsNeverAPlayerName");
		}
		
		assertEquals ("Test is setName changes the original", true, !orig.equals(p.getName()));
	}
	
	/**
	 * Test if setColor sets a new color
	 */
	public void testSetColor() {
		beginTest("setColor method");
		
		Color orig = p.getColor();
		
		p.setColor(orig.next());
		
		assertEquals("Test if setColor works", true, (orig == (p.getColor())));
	}
	
	public void testSetReady() {
		beginTest("setReady method");
		
		boolean orig = p.isReady();
		p.setReady(!orig);
		
		assertEquals("Check is setReady flips the boolean", true, orig != p.isReady());
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
		new PlayerTest().runTest();
	}

}
