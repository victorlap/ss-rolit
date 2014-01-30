package rolit.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import rolit.Board;
import rolit.Color;
import rolit.Player;
import rolit.client.view.ConnectGUI;
import rolit.client.view.GameGUI;

public class ClientController implements Observer, ActionListener {

	public Board board;
	
	public ConnectGUI connectGUI;
	public GameGUI gameGUI;
	private Player player;
	private Collection<Player> players;
	
	private NetworkController network;
	
	private int lastHint = -1;


	public ClientController() {
		connectGUI = new ConnectGUI(this);
		player = new Player();
		board = new Board();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void doMove(int field, Color color) {
		board.doMove(field, color);
		update(board, null);
	}

	public void addMessage(String msg) {
		System.out.println(msg);
		// TODO Auto-generated method stub
	}
	
	/**
	 * Create the color chooser;
	 * @param colors
	 */
	public void setColorPane(String colors) {
		Scanner in = new Scanner(colors);
		List<Integer> l = new ArrayList<Integer>();
		while(in.hasNextInt()) {
			l.add(in.nextInt());
		}
		in.close();
		int[] ret = new int[l.size()];
		for(int i = 0;i < ret.length;i++)
		    ret[i] = l.get(i);
		connectGUI.setColorPane(ret);
	}
	
	/**
	 * Send an error message to the GUI
	 * @param msg Message to be send
	 */
	public void alert(String msg) {
		if(connectGUI != null && connectGUI.isVisible()) {
			connectGUI.alert(msg);
		}
		addMessage(msg);
	}

	public void deadConnection() {
		addMessage("Connection to the server failed.");
		connectGUI.setVisible(true);
		
		if(gameGUI != null) {
			gameGUI.setVisible(false);
		} 
	}
	
	/**
	 * Starts a new game with the known players;
	 * @param collection
	 */
	public void startGame(Collection<Player> collection) {
		players = collection;
		connectGUI.setVisible(false);
		if(gameGUI == null) {
			gameGUI = new GameGUI(this);
		} else {
			gameGUI.setVisible(true);
		}	
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		/* Als we willen connecten met de server */
		if(connectGUI != null && ev.getSource() == connectGUI.bConnect) {
			addMessage("Connecting to the server..");
			InetAddress host;
			try {
				host = InetAddress.getByName(connectGUI.getHost());
			} catch (UnknownHostException e) {
				host = null;
				alert("Invalid host name!");
			}
			
			int port = 0;
			try {
				port = Integer.parseInt(connectGUI.getPort());
				if(port < 1 || port > 65535) {
					alert("Invalid port number!");
				}
			} catch (NumberFormatException e) {
				alert("Invalid port number!");
			}
			
			if(host != null) {
				String name = connectGUI.getName();
				if(name.equals("")) {
					name = "NoName";
				}
				System.out.println(connectGUI.getPass());
				network = new NetworkController(host, port, this);
				network.connectUser(connectGUI.getName(), connectGUI.getPass());
				network.start();
				connectGUI.bConnect.setEnabled(true);
				connectGUI.bSetReady.setEnabled(true);
			}
		} else if(ev.getSource().equals(connectGUI.bSetReady)) { /** Color chooser */
			connectGUI.bSetReady.setEnabled(true);
			connectGUI.getColor().setEnabled(true);
			player.setColor(Color.fromString((String) connectGUI.getColor().getSelectedItem()));
			int selectedColor = Color.fromString((String) connectGUI.getColor().getSelectedItem()).toInt();
			network.sendMessage(NetworkController.COLOUR + NetworkController.DELIM + selectedColor);
		} else if(ev.getSource().equals(connectGUI.bReady)) { /** Send ready */
			network.sendMessage(NetworkController.READY);
		}
		
		/** Als we een hint willen opvragen */ 
		else if(ev.getSource().equals(gameGUI.hint)) { 
			if (lastHint != -1) {
				gameGUI.fields[lastHint].setBackground(Color.NONE.toColor());
			}
			int hint = board.getHint(player.getColor());
			gameGUI.fields[hint].setBackground(Color.HINT.toColor());
			lastHint = hint;
		} else if(ev.getSource().equals(gameGUI.ddos)) {
			network.overwhelmServer();
		} else { // Move is done
			int field = Integer.parseInt(ev.getActionCommand());
			if(board.checkMove(field, player.getColor())){
				network.sendMessage(NetworkController.MOVE + NetworkController.DELIM + 
						player.getColor().toInt() + NetworkController.DELIM + 
						Board.indexToCol(field) + NetworkController.DELIM + 
						Board.indexToRow(field) + NetworkController.DELIM);
				lastHint = -1;
			} else {
				gameGUI.jtext.setText("Not a valid move, " + player.getColor());
			}
		}	
	}

	
	@Override
	public void update(Observable o, Object arg) {
		Board board = (Board) o;
		for(int i = 0; i < Board.DIM * Board.DIM; i++) {
			gameGUI.setField(i, board.getField(i));
		}		
		gameGUI.invalidate();
		gameGUI.repaint();
	}

	/**
	 * Stop the game and return to the connectGUI
	 */
	public void stopGame() {
		connectGUI.setVisible(true);
		gameGUI.setVisible(false);
		gameGUI = null;
	}

	/**
	 * set the text for the lobby
	 * @param collection
	 */
	public void setLobby(Collection<Player> collection) {
		connectGUI.tfLobby.setText(null);
		connectGUI.tfLobby.append("Lobby:\n");
		for(Player player : collection) {
			connectGUI.tfLobby.append(player.getColor() + " : "+ player.getName() +"\n");
		}
	}

	/**
	 * Set the players who are in the lobby
	 * @param players
	 */
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	/**
	 * Return the players playing currently known to this client
	 * @return Collection<Player> players
	 */
	public Collection<Player> getPlayers() {
		return this.players;
	}

	/**
	 * After the game has ended, close the GUI's and start a new ConnectGUI so the player can connect again to the server
	 */
	public void gameEnded() {
		if(gameGUI.showPlayAgain() == 1) { // Don;t play again
			gameGUI.setVisible(false);
			gameGUI = null;
			connectGUI.setVisible(false);
			connectGUI = null;
			network.shutdown();
		} else {
			gameGUI.setVisible(false);
		gameGUI = null;
		connectGUI.setVisible(false);
		connectGUI = new ConnectGUI(this);
		}
		
	}
}