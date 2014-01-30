package rolit.server.view;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import rolit.server.controller.ServerController;

public class ServerGUI extends JFrame {
	
	/**
	 * Auto generated serialVersionUID
	 */
	private static final long serialVersionUID = 3449105720772236156L;
	public JButton bConnect;
	private JTextField tfPort;
	private JTextArea taMessages;
	private ServerController controller;

	/** Constructs a ServerGUI object. */
	public ServerGUI(ServerController controller) {
		super("ServerGUI");
		
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

	/** builds the GUI. */
	public void buildGUI() {
		setSize(600, 400);

		// Panel p1 - Listen

		JPanel p1 = new JPanel(new FlowLayout());
		JPanel pp = new JPanel(new GridLayout(2, 2));

		JLabel lbAddress = new JLabel("Address: ");
		JTextField tfAddress = new JTextField(getHostAddress(), 12);
		tfAddress.setEditable(false);

		JLabel lbPort = new JLabel("Port:");
		tfPort = new JTextField("1337", 5);

		pp.add(lbAddress);
		pp.add(tfAddress);
		pp.add(lbPort);
		pp.add(tfPort);

		bConnect = new JButton("Start Listening");
		bConnect.addActionListener(controller);

		p1.add(pp, BorderLayout.WEST);
		p1.add(bConnect, BorderLayout.EAST);

		// Panel p2 - Messages

		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		
		JLabel lbMessages = new JLabel("Messages:");
		taMessages = new JTextArea("", 15, 50);
		taMessages.setEditable(false);
		DefaultCaret caret = (DefaultCaret)taMessages.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane p3 = new JScrollPane(taMessages);
		//p3.setLayout(new ScrollPaneLayout());
		
		
//		p3.add(taMessages);
//		p3.setSize(500,500);
		p2.add(lbMessages);
		p2.add(p3, BorderLayout.SOUTH);

		Container cc = getContentPane();
		cc.setLayout(new FlowLayout());
		cc.add(p1);
		cc.add(p2);
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
	
	public JTextField getPort() {
		return tfPort;
	}
	
	public JButton getConnect() {
		return bConnect;
	}

	/** add a message to the textarea  */
	public void addMessage(String msg) {
		taMessages.append(msg + "\n");
	}
}
