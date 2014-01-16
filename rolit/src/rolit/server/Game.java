package rolit.server;

import rolit.Board;
import rolit.Player;

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

	/**
	 * The players
	 */
	private Player[] players;

	/**
	 * The current player
	 */
	private int currentPlayer;

	// --------- CONSTRUCTORS ------

	/**
	 * Creates the Game
	 */
	public Game(Player[] players) {
		board = new Board();
		this.players = players;
		currentPlayer = randomPlayer();
	}

	//----------- METHODS ---------
	
	/**
	 * Pick a random player from the array of players
	 * @return <code>int</code> that identifies an element from the <code>players</code> array
	 */
	private int randomPlayer() {
		return (int) Math.random() * players.length;
	}
	
	/**
	 * Start the game i.e. let players make a move and pass the current turn to the next player.
	 */
	public void start() {
		while (!board.gameOver()) {
            players[currentPlayer].makeMove(board);
            currentPlayer = (currentPlayer + 1) % players.length;
        }
	}



}
