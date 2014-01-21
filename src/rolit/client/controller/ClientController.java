package rolit.client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rolit.Board;
import rolit.Player;
import rolit.client.view.ClientGUI;

public class ClientController implements ActionListener {
	
	Board model;
	ClientGUI view;
	Player[] players;
	

	public ClientController() {
		// TODO Auto-generated constructor stub
	}

	public void addModel(Board board) {
		model = board;
	}

	public void addView(ClientGUI view) {
		this.view = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int field = Integer.parseInt(e.getActionCommand());
		if(model.checkMove(field, view.current)){
			model.doMove(field, view.current);
			view.current = view.current.next();
			view.jtext.setText(view.current.toString());
		}
		
	}
	
	public void init() {
		model.init();
	}
}