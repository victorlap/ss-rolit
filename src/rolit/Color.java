package rolit;

public enum Color {

	NONE,
	HINT,
	RED,
	YELLOW,
	GREEN,
	BLUE;

	public Color next() {
		switch(this) {
		case RED:
			return YELLOW;
		case YELLOW:
			return GREEN;
		case GREEN:
			return BLUE;
		case BLUE:
			return RED;
		default:
			return NONE;
		}
	}
	
	public int toInt() {
		switch(this) {
		case RED:
			return 0;
		case YELLOW:
			return 1;
		case GREEN:
			return 2;
		case BLUE:
			return 3;
		default:
			return -1;
		}
	}
	
	public java.awt.Color toColor() {
		switch(this) {
		case RED:
			return java.awt.Color.RED;
		case YELLOW:
			return java.awt.Color.YELLOW;
		case GREEN:
			return java.awt.Color.GREEN;
		case BLUE:
			return java.awt.Color.BLUE;
		case HINT:
			return java.awt.Color.DARK_GRAY;
		default:
			return java.awt.Color.WHITE;
		}
	}
	
	public static Color fromInt(int i) {
		switch(i) {
		case 0:
			return RED;
		case 1:
			return YELLOW;
		case 2:
			return GREEN;
		case 3:
			return BLUE;
		default:
			return NONE;
		}
	}
	
	public static Color fromString(String color) {
		color = color.toUpperCase();
		if(RED.toString().equals(color)) {
			return RED;
		}
		if(YELLOW.toString().equals(color)) {
			return YELLOW;
		}
		if(GREEN.toString().equals(color)) {
			return GREEN;
		}
		if(BLUE.toString().equals(color)) {
			return BLUE;
		}
		return NONE;
	}

}
