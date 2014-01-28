package rolit.client.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import rolit.Board;
import rolit.Color;
import rolit.client.controller.ClientController;

public class GameGUI extends JFrame implements WindowListener {
	
	/**
	 * Auto generated Serial Version UID
	 */
	private static final long serialVersionUID = -6726007787663909268L;
	
	private ClientController controller;
	public Color current = Color.RED;
	
	public JButton[] fields = new JButton[Board.DIM * Board.DIM];
	public JButton hint = new JButton();
	
	public JLabel jtext = new JLabel();
	public JLabel scores = new JLabel();
	public JButton ddos = new JButton();

	// -------- CONSTRUCTORS --------
	public GameGUI(ClientController controller) {
		
		super("RolitClient Game");
		this.controller = controller;
		
		buildView();
		
		setVisible(true);
		
		addWindowListener(this);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void buildView() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		setSize(600, 300);
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		//gbc.gridx = Board.DIM;
		gbc.gridy = Board.DIM;
		gbc.gridwidth = (Board.DIM -4) / 2;
		
		jtext.setText(current.toString());
		add(jtext, gbc);
		
		scores.setText("");
		add(scores, gbc);
		
		gbc.gridx = Board.DIM-4;
		gbc.gridwidth = 2;
		ddos.setText("Attack!");
		
		add(ddos, gbc);
		
		gbc.gridx = Board.DIM-2;
		hint.setText("Get Hint");
		
		add(hint, gbc);
		
		for(int i = 0; i < fields.length; i++) {
			fields[i] = new JButton();
			//fields[i].setText(new Integer(i).toString());
			fields[i].setActionCommand(new Integer(i).toString());
			fields[i].addActionListener(controller);
			gbc.gridx = (i % Board.DIM);
			gbc.gridy = (i / Board.DIM);
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			add(fields[i], gbc);
		}
		
		ddos.addActionListener(controller);
		hint.addActionListener(controller);
		
	}
	
	public void setField(int field, Color color) {
		fields[field].setBackground(color.toColor());
		if(color == Color.NONE) {
			fields[field].setEnabled(true);
		} else {
			fields[field].setEnabled(false);
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		controller.stopGame();
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override  
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	
}
