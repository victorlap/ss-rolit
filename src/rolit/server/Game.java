package rolit.server;

import java.util.ArrayList;
import java.util.Collection;

import rolit.Board;
import rolit.Color;
import rolit.Player;


/**
 * Maintains game object i.e. let players make a move and assigning the current player's turn.
 * 
 * @author Victor Lap and Yuri van Midden
 * @version 1.0.0
 */

public class Game {
	
	// -------- VARIABLES ------
	
	/**
	 * Minimum number of players
	 */
	public static final int MINIMUMPLAYERS = 2;

	/**
	 * The Board
	 */
	private Board board;
	
	private boolean isRunning;

	/**
	 * The array of players
	 */
	private Collection<Player> players;

	/**
	 * Saves array index of current player
	 */
	private int currentPlayer;

	// --------- CONSTRUCTORS ------

	/**
	 * Creates a new Game from the <code>players</code> array.
	 */
	public Game() {
		board = new Board();
		players = new ArrayList<Player>();
	}

	//----------- METHODS ---------
	
	/**
	 * Pick a random player from the array of players
	 * @return <code>int</code> that identifies an element from the <code>players</code> array
	 */
	private int randomPlayer() {
		return (int) Math.random() * players.size();
	}
	
	/**
	 * Start the game i.e. let players make a move and pass the current turn to the next player.
	 */
	public void start() {
		isRunning = true;
		currentPlayer = randomPlayer();
		while (!board.gameOver()) {
            //players[currentPlayer].makeMove(board);
            currentPlayer = (currentPlayer + 1) % players.size();
        }
	}
	
	public int getAmountOfPlayer() {
		return players.size();
	}

	public void doMove(int field, Color color) {
		// TODO Auto-generated method stub
		
	}
	
	public void addPlayer(Player newPlayer) {
		players.add(newPlayer);
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public Collection<Player> getPlayers() {
		return players;
	}
	
	public String freeColorString() {
		ArrayList<Color> colors = new ArrayList<Color>();
		if(players.size() <= 2) {
			colors.add(Color.RED);
			colors.add(Color.YELLOW);
		}
		if(players.size() > 2 && players.size() <= 4) {
			colors.add(Color.GREEN);
		}
		if(players.size() == 4) {
			colors.add(Color.BLUE);
		}
		String result = "";
		for(Color c : colors) {
			boolean canAdd = true;
			for(Player player : players) {
				if(player.getColor() == c) {
					canAdd = false;
				}
			}
			if(canAdd) {
				result = result + " "+ c.toInt();
			}
		}
		return result;
	}



}
