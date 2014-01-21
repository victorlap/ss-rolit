package rolit.client.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import rolit.Board;
import rolit.Color;
import rolit.client.controller.ClientController;

public class ClientGUI extends JFrame implements Observer, GUI {
	
	/**
	 * Auto generated Serial Version UID
	 */
	private static final long serialVersionUID = -6726007787663909268L;
	
	private ClientController controller;
	public Color current = Color.RED;
	
	public JButton[] fields = new JButton[Board.DIM * Board.DIM];
	
	public JTextField jtext = new JTextField();

	// -------- CONSTRUCTORS --------
	public ClientGUI() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		setSize(600, 300);
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = Board.DIM;
		//gbc.gridy = Board.DIM +1;
		jtext.setText(current.toString());
		add(jtext, gbc);
		
		for(int i = 0; i < fields.length; i++) {
			fields[i] = new JButton();
			//fields[i].setText(Color.NONE.toString());
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
	
	// --------- METHODS -------

	@Override
	public void update(Observable model, Object arg) {
		Board board = (Board) model;
		//System.out.println("UPDATE CLIENTGUI");
		for(int i = 0; i < Board.DIM * Board.DIM; i++) {
			//fields[i].setText(board.getField(i).toString());
			fields[i].setBackground(board.getField(i).toColor());
			if(board.getField(i) == Color.NONE) {
				fields[i].setEnabled(true);
			} else {
				fields[i].setEnabled(false);
			}
		}
		//repaint();
	}
	
	public void addController(ClientController c) {
		this.controller = c;
		for(int i=0; i < Board.DIM * Board.DIM; i++) {
			fields[i].addActionListener(controller);
		}
	}

	
}
