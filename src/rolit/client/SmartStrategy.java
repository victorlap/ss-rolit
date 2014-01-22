package rolit.client;

import rolit.Board;
import rolit.Color;

public class SmartStrategy implements Strategy {

	@Override
	public String getName() {
		return "Smart";
	}

	@Override
	public int determineMove(Board b, Color c) {
		
		int[] qualityFields = b.getQuality(c);
		int bestMove = 0;
		int bestQual = 0;
		
		for (int i = 0; i < qualityFields.length; i++){
			if (qualityFields[i] > bestQual) {
				bestQual = qualityFields[i];
				bestMove = i;
			}
		}
		
		return bestMove;
	}

}
