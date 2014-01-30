package rolit.server;

import java.util.ArrayList;
import java.util.Collection;

import rolit.Board;
import rolit.Color;
import rolit.Player;
import rolit.server.controller.NetworkController;


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
	
	/**
	 * NetworkController
	 */
	private NetworkController controller;
	
	/** 
	 * isRunning
	 */
	private boolean isRunning;

	/**
	 * The array of players
	 */
	private ArrayList<Player> players;
	
	/**
	 * Count of the move
	 */
	private int count = 0;

	/**
	 * Saves array index of current player
	 */
	private int currentPlayer;

	// --------- CONSTRUCTORS ------

	/**
	 * Creates a new Game from the <code>players</code> array.
	 */
	public Game(NetworkController networkController) {
		board = new Board();
		players = new ArrayList<Player>();
		controller = networkController;
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
		board.init();
		isRunning = true;
		currentPlayer = randomPlayer();
		nextTurn();
	}
	
	/**
	 * Returns the amount of players currently in the game
	 * @return int amount of players
	 */
	public int getAmountOfPlayer() {
		return players.size();
	}
	
	/**
	 * If the previous turn is correct, this method is executed
	 */
	public void nextTurn() {
		controller.broadcast(NetworkController.TURN + NetworkController.DELIM + count + NetworkController.DELIM + currentPlayer);
		count++;
		
	}
	
	/**
	 * returns the current turn
	 * @return int
	 */
	public int getTurn() {
		return count;
	}
	/**
	 * Passes the turn to another player
	 */
	public void nextPlayer() {
		currentPlayer = (currentPlayer + 1) % players.size();
		System.out.println(currentPlayer);
		while(players.get(currentPlayer).getColor() == Color.NONE) {
			currentPlayer = (currentPlayer + 1) % players.size();
		}
	}

	/**
	 * Executes the move if the move is correct, then executes nextTurn() and nextPlayer()
	 * @param col
	 * @param row
	 * @param color
	 * @throws NullPointerException if the player tries to do a move before it's turn
	 */
	public void doMove(int col, int row, Color color) throws NullPointerException {
		System.out.println(color.toString() + currentPlayer);
		if(currentPlayer != color.toInt()) {
			throw new NullPointerException();
		}
		board.doMove(col, row , color);
		nextPlayer();
		nextTurn();
	}
	
	/**
	 * Adds a player to the game
	 * @param newPlayer
	 */
	public void addPlayer(Player newPlayer) {
		players.add(newPlayer);
	}
	
	/**
	 * Returns if the game is already started
	 * @return bool
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Get all the players who are currently in the game
	 * @return ArrayList<Player>
	 */
	public Collection<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Returns a string with the colors that can be chosen according to the AMULET protocol
	 * @return
	 */
	public String freeColorString() {
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(Color.RED);
		colors.add(Color.YELLOW);
		colors.add(Color.GREEN);
		colors.add(Color.BLUE);
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

	/**
	 * Checks if the color is in use
	 * @param color
	 * @return
	 */
	public boolean isColorInUse(Color color) {
		for(Player player : players) {
			if(player.getColor() == color) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the player for its color
	 * @param c color
	 * @return
	 */
	public Player getPlayerByColor(Color c) {
		for(Player player : players) {
			if(player.getColor() == c) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Returns a string representative of the Players who are in the game, according to the AMULET protocol
	 * @return
	 */
	public String getPlayerString() {
		String result;
		if(getPlayerByColor(Color.RED) != null) {
			result = getPlayerByColor(Color.RED).getName() + " ";
		} else {
			result = "EMPTY ";
		}
		if(getPlayerByColor(Color.YELLOW) != null) {
			result = result + getPlayerByColor(Color.YELLOW).getName() + " ";
		} else {
			result = result + "EMPTY ";
		}
		if(getPlayerByColor(Color.GREEN) != null) {
			result = result + getPlayerByColor(Color.GREEN).getName() + " ";
		} else {
			result = result + "EMPTY ";
		}
		if(getPlayerByColor(Color.BLUE) != null) {
			result = result + getPlayerByColor(Color.BLUE).getName() + " ";
		} else {
			result = result + "EMPTY ";
		}
		return result;
	}

	/**
	 * removes a player from the game
	 * @param player
	 */
	public void removePlayer(Player player) {
		players.remove(player);
		
	}

	/**
	 * Checks if the game is ready to start
	 * @return
	 */
	public boolean readyToStart() {
		if(players.size() < 2) {
			return false;
		}
		for(Player player : players) {
			if(!player.isReady()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		String result = "";
		for(Player player : players) {
			result = result + player + "\n";
		}
		return result;
	}

	/**
	 * Checks if the game has ended
	 * @return
	 */
	public boolean gameOver() {
		System.out.println(board);
		return board.isFull();
		
	}


}
