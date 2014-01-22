package rolit.server.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ServerGUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8658198253413521709L;
	
	private JLabel labeladdress = new JLabel("Server IP address");
	private JTextField textaddress = new JTextField();
	
	private JLabel labelport = new JLabel("Port number");
	private JTextField textport = new JTextField();
	
	private JButton toggleServer = new JButton();
	private JTextField log = new JTextField();

	public ServerGUI(String InetAdrress, String portNumber) {
		textaddress.setText(InetAdrress);
		textport.setText(portNumber);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		toggleServer.setText("Start Server");
		
		log.setText("Heel veel text");
		
		gbc.fill = GridBagConstraints.BOTH;
	
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		add(labeladdress, gbc);
		
		gbc.gridx = 1;
		add(textaddress, gbc);
		
		gbc.gridx = 3;
		gbc.gridwidth = 2;
		gbc.gridheight = 2;
		add(toggleServer, gbc);
		
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(labelport, gbc);
		
		gbc.gridx = 1;
		add(textport, gbc);
		
		
		
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 4;
		gbc.gridheight = 2;
		//gbc.fill = GridBagConstraints.BOTH;
		
		log.setSize(400, 300);
		add(log, gbc);
		
		
		setVisible(true);
		setSize(500, 500);
		
		
	}
}
