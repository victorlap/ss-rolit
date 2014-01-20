package rolit.client.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;

import rolit.Board;
import rolit.Color;

public class ClientGUI extends JFrame implements Observer, ActionListener {
	
	/**
	 * Auto generated Serial Version UID
	 */
	private static final long serialVersionUID = -6726007787663909268L;
	
	private Board model;
	
	JButton[] fields = new JButton[Board.DIM * Board.DIM];

	// -------- CONSTRUCTORS --------
	public ClientGUI(Board model) {
		this.model = model;
		model.addObserver(this);
		init();
		update(model, null);
		
	}
	
	// --------- METHODS -------
	
	private void init() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		setSize(600, 300);
		
		for(int i = 0; i < fields.length; i++) {
			fields[i] = new JButton();
			fields[i].setText(Color.NONE.toString());
			fields[i].addActionListener(this);
			fields[i].setActionCommand(new Integer(i).toString());
			gbc.gridx = (i % Board.DIM);
			gbc.gridy = (i / Board.DIM);
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.fill = GridBagConstraints.BOTH;
			add(fields[i], gbc);
		}
		
		setVisible(true);
	}

	@Override
	public void update(Observable model, Object arg) {
		Board board = (Board) model;
		System.out.println("UPDATE CLIENTGUI");
		for(int i = 0; i < Board.DIM * Board.DIM; i++) {
			fields[i].setText(board.getField(i).toString());
			
			if(board.getField(i) == Color.NONE) {
				fields[i].setEnabled(true);
			} else {
				fields[i].setEnabled(false);
			}
		}
		//repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int field = Integer.parseInt(e.getActionCommand());
		if(model.checkMove(field, Color.RED)){
			model.doMove(field, Color.RED);
		}
		
	}

}
