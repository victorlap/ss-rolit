package rolit.client;

import rolit.Board;
import rolit.Color;
import rolit.Player;
import rolit.client.RandomStrategy;
import rolit.client.Strategy;

public class ComputerPlayer extends Player {

	public ComputerPlayer(Color theColor, Strategy strategy) {
		super(strategy.getName()+"-"+theColor, theColor);
		this.strategy = strategy;
	}
	
	public ComputerPlayer(Color theColor) {
		super("Naive", theColor);
		this.strategy = new RandomStrategy();
	}

	@Override
	public int determineMove(Board board) {
		return strategy.determineMove(board, getColor());
	}
	
	public Strategy getStrategy() {
		return strategy;
	}
	
	public void updateStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	

}
