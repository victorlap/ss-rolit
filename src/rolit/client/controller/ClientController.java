package rolit.client.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import rolit.Board;
import rolit.Game;
import rolit.Player;
import rolit.client.view.ConnectGUI;
import rolit.client.view.GameGUI;
import rolit.client.view.LobbyGUI;

public class ClientController implements Observer, ActionListener {

	private Board board;
	private Game game;
	private Player player;
	
	private ConnectGUI connectGUI;
	private GameGUI gameGUI;
	private LobbyGUI lobbyGUI;
	
	private int lastHint = -1;


	public ClientController() {
		connectGUI = new ConnectGUI(this);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void doMove(int field, Color color) {
		game.doMove(field, color);
		update(board, null);
	}

	public void addMessage(String string) {
		// TODO Auto-generated method stub
		
	}

	public void deadConnection() {
		addMessage("Connection to the server failed.");
		connectGUI.setVisible(true);
		
		if(gameGUI != null) {
			gameGUI.setVisible(false);
		} 
		if(lobbyGUI != null) {
			lobbyGUI.setVisible(false);
		}
	}

	public void connectionEstablished() {
		connectGUI.setVisible(false);
		
		if(lobbyGUI == null) {
			lobbyGUI = new LobbyGUI(this);
		} else {
			lobbyGUI.setVisible(true);
		}
	}
	
	public void startGame(String[] players) {
		if(gameGUI != null) {
			gameGUI = new GameGUI(this);
		} else {
			gameGUI.setVisible(true);
		}
		lobbyGUI.setVisible(false);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(gameGUI.hint)) {
			if (lastHint != -1) {
				gameGUI.fields[lastHint].setBackground(Color.WHITE);
			}
			int hint = board.getHint(gameGUI.current);
			gameGUI.fields[hint].setBackground(Color.DARK_GRAY);
			lastHint = hint;
			//view.repaint();
		} else {
			int field = Integer.parseInt(e.getActionCommand());
			if(board.checkMove(field, gameGUI.current)){
				board.doMove(field, gameGUI.current);
				gameGUI.current = gameGUI.current.next();
				gameGUI.jtext.setText(gameGUI.current.toString());
				gameGUI.scores.setText(gameGUI.current.toString() +"'s score: " + board.countFields(gameGUI.current));
				lastHint = -1;
			} else {
				gameGUI.jtext.setText("Not a valid move, " + gameGUI.current.toString());
			}
		}		
	}

	@Override
	public void update(Observable o, Object arg) {
		Board board = (Board) o;
		for(int i = 0; i < Board.DIM * Board.DIM; i++) {
			gameGUI.setField(i, board.getField(i));
		}		
	}
}