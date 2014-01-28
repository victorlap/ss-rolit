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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rolit.Color;
import rolit.client.controller.ClientController;

public class ConnectGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -108959824947488464L;

	public JButton bConnect;
	public JButton bSetReady;
	private JTextField tfPort;
	private JTextField tfName;
	private JTextField tfHost;
	private JTextField tfPass;
	private JComboBox<String> cbColor;
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
	
	public String getPass() {
		return tfPass.getText();
	}
	
	public JComboBox<String> getColor() {
		return cbColor;
	}

	/** builds the GUI. */
	public void buildGUI() {
		setSize(400, 190);
		setLocation(140,140);

		// Panel p1 - Listen

		JPanel p1 = new JPanel(new FlowLayout());
		JPanel pp = new JPanel(new GridLayout(5,2));
		JPanel p2 = new JPanel(new GridLayout(2,1));

		JLabel lbAddress = new JLabel("Address: ");
		tfHost = new JTextField(getHostAddress(), 12);

		JLabel lbPort = new JLabel("Port:");
		tfPort = new JTextField("1337", 5);

		JLabel lbName = new JLabel("Name:");
		tfName = new JTextField("player_test1", 5);
		
		JLabel lbPass = new JLabel("Password:");
		tfPass = new JTextField("test1", 5);
		
		JLabel lbColor = new JLabel("Color:");
		cbColor = new JComboBox<String>();
		cbColor.setEnabled(false);


		pp.add(lbAddress);
		pp.add(tfHost);
		
		pp.add(lbPort);
		pp.add(tfPort);
		
		pp.add(lbName);
		pp.add(tfName);
		
		pp.add(lbPass);
		pp.add(tfPass);
		
		pp.add(lbColor);
		pp.add(cbColor);

		bConnect = new JButton("Connect");
		bConnect.addActionListener(controller);
		p2.add(bConnect);
		
		bSetReady = new JButton("Ready");
		bSetReady.addActionListener(controller);
		bSetReady.setEnabled(false);
		p2.add(bSetReady);

		p1.add(pp, BorderLayout.WEST);
		p1.add(p2, BorderLayout.EAST);

		Container cc = getContentPane();
		cc.setLayout(new FlowLayout());
		cc.add(p1);

	}

	public void alert(String message) {
		JOptionPane.showMessageDialog(this, message, "Alert", JOptionPane.ERROR_MESSAGE);
	}
	
	public void setColorPane(int[] colors) {
		for(int i = 0; i < colors.length; i++) {
			cbColor.addItem(Color.fromInt(i).toString());
		}
		cbColor.setEnabled(true);
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
