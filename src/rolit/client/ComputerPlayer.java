package rolit.client;

import java.util.ArrayList;
import java.util.List;

import rolit.Board;
import rolit.Color;
import rolit.Player;

public class ComputerPlayer extends Player {
	
	public ComputerPlayer(Color color) {
		setColor(color);
	}
	
	@Override
	public String getName() {
		return "ComputerPlayer";
	}

	public int determineMove(Board b) {
		
		Color c = getColor();
		
		List<Integer> validMoves = new ArrayList<Integer>();
		boolean[] tempFlipFields = b.getFlippableFields(c);
		
		for (int i = 0; i < tempFlipFields.length; i++) {
			if (tempFlipFields[i]) {
				validMoves.add(i);
			}
		}
		
		if(validMoves.isEmpty()) {
			for (int i = 0; i < Board.DIM * Board.DIM; i++) {
				if(b.isEmptyField(i) && b.isBordering(i)) {
					validMoves.add(i);
				}
			}
		}
		
		return validMoves.get((int) (Math.random() * validMoves.size()));
	}

}
