package rolit.client;

import java.util.ArrayList;
import java.util.List;

import rolit.Board;
import rolit.Color;

public class RandomStrategy implements Strategy {

	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public int determineMove(Board b, Color c) {
		
		List<Integer> validMoves = new ArrayList<Integer>();
		boolean[] tempFlipFields = b.getFlippableFields(c);
		
		for (int i = 0; i < tempFlipFields.length; i++) {
			if (tempFlipFields[i]) {
				validMoves.add(i);
			}
		}
		
		return (int) (Math.random() * validMoves.size());
	}

}
