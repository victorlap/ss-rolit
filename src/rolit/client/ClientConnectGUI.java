package rolit.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rolit.GUI;

/**
 * ServerGui. A GUI for the Server.
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class ClientConnectGUI extends JFrame implements ActionListener, GUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = -108959824947488464L;
	
	private JButton bConnect;
	private JTextField tfPort;
	private JTextField tfName;
	private JTextField tfAddress;
	private Client client;

	/** Constructs a ServerGUI object. */
	public ClientConnectGUI() {
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
		bConnect.addActionListener(this);
		
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

	/**
	 * listener for the "Start Listening" button
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		if (src == bConnect) {
			startListening();
		}
	}

	/**
	 * Construct a Server-object, which is waiting for clients. The port field and button should be disabled
	 */
	private void startListening() {
		int port = 0;
		int max = 0;

		try {
			port = Integer.parseInt(tfPort.getText());
		} catch (NumberFormatException e) {
			addMessage("ERROR: not a valid portnumber!");
			return;
		}
		
		String name = tfName.getText();
		String hoststr = tfAddress.getText();
		InetAddress host;
		try {
			host = InetAddress.getByName(hoststr);
			
			tfPort.setEditable(false);
			bConnect.setEnabled(false);
			tfName.setEditable(false);
			tfAddress.setEditable(false);

			try {
				client = new Client(name, host, port, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			client.start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addMessage("User " + name + " joined on port " + port + "...");
		client.sendMessage(name + "\n");
	}

	/** add a message to the textarea  */
	public void addMessage(String msg) {
		//taMessages.append(msg + "\n");
	}
	
	public void addMessage(String msg, String clientName) {
		//taMessages.append("<"+clientName+"> " + msg + "\n");
	}

	/** Start a ServerGUI application */
	public static void main(String[] args) {
		ClientConnectGUI gui = new ClientConnectGUI();
	}

}
