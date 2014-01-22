package rolit.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rolit.Board;
import rolit.Player;
import rolit.client.view.ClientGUI;

public class ClientController implements ActionListener {

	Board model;
	ClientGUI view;
	Player[] players;
	int lastHint = -1;


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
		if(e.getSource().equals(view.hint)) {
			if (lastHint != -1) {
				view.fields[lastHint].setBackground(Color.WHITE);
			}
			int hint = model.getHint(view.current);
			view.fields[hint].setBackground(Color.DARK_GRAY);
			lastHint = hint;
			//view.repaint();
		} else {
			int field = Integer.parseInt(e.getActionCommand());
			if(model.checkMove(field, view.current)){
				model.doMove(field, view.current);
				view.current = view.current.next();
				view.jtext.setText(view.current.toString());
				view.scores.setText(view.current.toString() +"'s score: " + model.countFields(view.current));
				lastHint = -1;
			} else {
				view.jtext.setText("Not a valid move, " + view.current.toString());
			}
		}		
	}

	public void init() {
		model.init();
	}
}