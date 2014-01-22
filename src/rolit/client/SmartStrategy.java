package rolit.client;

import java.util.Random;

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
		
		Random random = new Random();
		
		for (int i = 0; i < qualityFields.length; i++){
			if(qualityFields[i] == bestQual && random.nextBoolean()) {
				bestMove = i;
			}
			if (qualityFields[i] > bestQual) {
				bestQual = qualityFields[i];
				bestMove = i;
			}
			//System.out.println(bestMove +" - "+ bestQual);
		}
		if(bestQual == 0) {
			RandomStrategy rs = new RandomStrategy();
			return rs.determineMove(b, c);
		}
		
		return bestMove;
	}

}
