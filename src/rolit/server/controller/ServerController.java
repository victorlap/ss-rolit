package rolit.server.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rolit.server.view.ServerGUI;

public class ServerController implements ActionListener {
	
	NetworkController network;
	ServerGUI serverGUI;
	
	public ServerController() {
		serverGUI = new ServerGUI(this);
	}

	/**
	 * listener for the "Start Listening" button
	 */
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		if (src == serverGUI.bConnect) {
			startListening();
		}
	}
	
	public void addMessage(String msg) {
		System.out.println(msg);
		serverGUI.addMessage(msg);
	}
	
	/**
	 * Construct a Server-object, which is waiting for clients. The port field and button should be disabled
	 */
	private void startListening() {
		int port = 0;

		try {
			port = Integer.parseInt(serverGUI.getPort().getText());
		} catch (NumberFormatException e) {
			addMessage("ERROR: not a valid portnumber!");
			return;
		}

		serverGUI.getPort().setEditable(false);
		serverGUI.getConnect().setEnabled(false);

		addMessage("Started listening on port " + port + "...");
		
		network = new NetworkController(port, this);
		network.start();
	}

}
