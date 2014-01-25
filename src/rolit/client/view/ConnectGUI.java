package rolit.client.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rolit.client.controller.ClientController;

/**
 * ServerGui. A GUI for the Server.
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class ConnectGUI extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -108959824947488464L;
	
	private JButton bConnect;
	private JTextField tfPort;
	private JTextField tfName;
	private JTextField tfAddress;
	//private NetworkController client;

	/** Constructs a ServerGUI object. 
	 * @param clientController 
	 **/
	public ConnectGUI(ClientController clientController) {
		super("ClientGUI");

		buildGUI();
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
			}
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	/** builds the GUI. */
	public void buildGUI() {
		setSize(400, 400);

		// Panel p1 - Listen

		JPanel p1 = new JPanel(new FlowLayout());
		JPanel pp = new JPanel(new GridLayout(3,2));

		JLabel lbAddress = new JLabel("Address: ");
		tfAddress = new JTextField(getHostAddress(), 12);
		
		JLabel lbPort = new JLabel("Port:");
		tfPort = new JTextField("1337", 5);
		
		JLabel lbName = new JLabel("Name:");
		tfName = new JTextField("", 5);

		pp.add(lbAddress);
		pp.add(tfAddress);
		pp.add(lbName);
		pp.add(tfName);
		pp.add(lbPort);
		pp.add(tfPort);

		bConnect = new JButton("Connect");
		//bConnect.addActionListener(this);
		
		p1.add(pp, BorderLayout.WEST);
		p1.add(bConnect, BorderLayout.EAST);

		Container cc = getContentPane();
		cc.setLayout(new FlowLayout());
		cc.add(p1);
		
	}

	/** returns the Internetadress of this computer */
	private String getHostAddress() {
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			return iaddr.getHostAddress();
		} catch (UnknownHostException e) {
			return "?unknown?";
		}
	}
}
