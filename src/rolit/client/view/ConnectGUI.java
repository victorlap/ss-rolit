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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import rolit.client.controller.ClientController;

public class ConnectGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -108959824947488464L;

	public JButton bConnect;
	private JTextField tfPort;
	private JTextField tfName;
	private JTextField tfHost;
	private JPasswordField tfPass;
	private ClientController controller;

	/** Constructs a ServerGUI object. 
	 * @param clientController 
	 **/
	public ConnectGUI(ClientController controller) {
		super("RolitClient Connect");
		this.controller = controller;

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

	public String getHost() {
		return tfHost.getText();
	}

	public String getPort() {
		return tfPort.getText();
	}

	public String getName() {
		return tfName.getText();
	}

	/** builds the GUI. */
	public void buildGUI() {
		setSize(400, 150);

		// Panel p1 - Listen

		JPanel p1 = new JPanel(new FlowLayout());
		JPanel pp = new JPanel(new GridLayout(4,2));

		JLabel lbAddress = new JLabel("Address: ");
		tfHost = new JTextField(getHostAddress(), 12);

		JLabel lbPort = new JLabel("Port:");
		tfPort = new JTextField("1337", 5);

		JLabel lbName = new JLabel("Name:");
		tfName = new JTextField("", 5);
		
		JLabel lbPass = new JLabel("Password:");
		tfPass = new JPasswordField("", 5);

		pp.add(lbAddress);
		pp.add(tfHost);
		
		pp.add(lbPort);
		pp.add(tfPort);
		
		pp.add(lbName);
		pp.add(tfName);
		
		pp.add(lbPass);
		pp.add(tfPass);

		bConnect = new JButton("Connect");
		bConnect.addActionListener(controller);

		p1.add(pp, BorderLayout.WEST);
		p1.add(bConnect, BorderLayout.EAST);

		Container cc = getContentPane();
		cc.setLayout(new FlowLayout());
		cc.add(p1);

	}

	public void alert(String message) {
		JOptionPane.showMessageDialog(this, message, "Alert", JOptionPane.ERROR_MESSAGE);
	}

	/** returns the Internetadress of this computer */
	private String getHostAddress() {
		try {
			InetAddress iaddr = InetAddress.getLocalHost();
			return iaddr.getHostAddress();
		} catch (UnknownHostException e) {
			return "localhost";
		}
	}
}
