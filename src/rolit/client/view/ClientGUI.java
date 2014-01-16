package rolit.client.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;

import rolit.Board;

public class ClientGUI extends JFrame implements Observer {
	
	/**
	 * Auto generated Serial Version UID
	 */
	private static final long serialVersionUID = -6726007787663909268L;
	
	JButton[] fields = new JButton[Board.DIM * Board.DIM];

	// -------- CONSTRUCTORS --------
	public ClientGUI() {
		init();
	}
	
	// --------- METHODS -------
	
	private void init() {
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
	
		//setSize(300,200);
		
		for(int i = 0; i < fields.length; i++) {
			fields[i] = new JButton();
			c.add(fields[i]);
		}
		
		setVisible(true);
	}

	@Override
	public void update(Observable model, Object arg) {
		Board board = (Board) model;
		fields[board.getlastChangedField()].setText(
				board.getField(
						board.getlastChangedField()).toString());
	}
	
	/**
	 * Test method;
	 */
	public static void main(String[] args) {
		new ClientGUI();
	}
}
