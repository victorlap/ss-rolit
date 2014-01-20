package rolit;

public enum Color {

	NONE,
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

}
