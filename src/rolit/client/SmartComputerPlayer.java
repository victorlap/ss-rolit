package rolit.client;

import java.util.Random;

import rolit.Board;
import rolit.Color;

public class SmartComputerPlayer extends ComputerPlayer {

	public SmartComputerPlayer(Color color) {
		super(color);
	}

	@Override
	public String getName() {
		return "SmartComputerPlayer";
	}

	@Override
	public int determineMove(Board b) {
		
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			
		}
		
		Color c = getColor();

		int[] qualityFields = b.getQuality(c);
		int bestMove = 0;
		int bestQual = 0;

		Random random = new Random();

		for (int i = 0; i < qualityFields.length; i++){
			if(qualityFields[i] == bestQual && random.nextBoolean()) {
				bestMove = i;
			}
			if (qualityFields[i] > bestQual) {
				if (i != 1 && i!=8 && i!=9 &&
						i != 6 && i!=14 && i!=15 &&
						i != 48 && i!=49 && i!=57 &&
						i != 54 && i!=55 && i!=62) {
					bestQual = qualityFields[i];
					bestMove = i;
				}
				
			}
			if((i == 0 || i == 7 || i == 63 || i == 56) && b.isBordering(i) && b.isEmptyField(i) && b.checkMove(i, c)) {
				return i;
			}
				
			//System.out.println(bestMove +" - "+ bestQual);
		}
		if(bestQual == 0) {
			return super.determineMove(b);
		}

		return bestMove;
	}
	

}
