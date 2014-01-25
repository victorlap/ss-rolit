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

}
