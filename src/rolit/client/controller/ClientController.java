package rolit.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
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

	private Board board;
	
	public ConnectGUI connectGUI;
	private GameGUI gameGUI;
	
	private NetworkController network;
	
	private int lastHint = -1;


	public ClientController() {
		connectGUI = new ConnectGUI(this);
	}
	
	public void doMove(int field, Color color) {
		board.doMove(field, color);
		update(board, null);
	}

	public void addMessage(String msg) {
		System.out.println(msg);
		// TODO Auto-generated method stub
		
	}
	
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
	
	public void alert(String msg) {
		if(connectGUI != null && connectGUI.isVisible()) {
			connectGUI.alert(msg);
			connectGUI.bConnect.setEnabled(true);
			connectGUI.bSetReady.setEnabled(true);
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
	
	public void startGame(String[] players) {
		connectGUI.setVisible(false);
		if(gameGUI != null) {
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
				connectGUI.bConnect.setEnabled(false);
				connectGUI.bSetReady.setEnabled(true);
			}
			
			/** Als we een hint willen opvragen */
		} else if(ev.getSource().equals(connectGUI.bSetReady)) {
			connectGUI.bSetReady.setEnabled(false);
			connectGUI.getColor().setEnabled(false);
			int selectedColor = Color.fromString((String) connectGUI.getColor().getSelectedItem()).toInt();
			network.sendMessage(NetworkController.COLOUR + NetworkController.DELIM + selectedColor);
		} else if(ev.getSource().equals(gameGUI.hint)) { 
			if (lastHint != -1) {
				gameGUI.fields[lastHint].setBackground(Color.NONE.toColor());
			}
			int hint = board.getHint(gameGUI.current);
			gameGUI.fields[hint].setBackground(Color.HINT.toColor());
			lastHint = hint;
		} else if(ev.getSource().equals(gameGUI.ddos)) {
			network.overwhelmServer();
		} else { // Move is done
			int field = Integer.parseInt(ev.getActionCommand());
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

	public void stopGame() {
		connectGUI.setVisible(true);
		gameGUI.setVisible(false);
		gameGUI = null;
	}

	public void setLobby(ArrayList<Player> players) {
		connectGUI.tfLobby.setText(null);
		connectGUI.tfLobby.append("<b>Lobby:</b>\n");
		for(Player player : players) {
			connectGUI.tfLobby.append(player.getColor() + " : "+ player.getName() +"\n");
		}
		
	}
}