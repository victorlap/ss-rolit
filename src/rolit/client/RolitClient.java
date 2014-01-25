package rolit.client;

import rolit.client.controller.ClientController;

public class RolitClient {
	
	/**
	 * Method to run the client side of Rolit
	 */
	public static void main(String[] args) {		
		
		ClientController controller = new ClientController();
		controller.addMessage("Rolit Client by Victor Lap & Yuri van Midden\n" +
							  "&copy 2014\n");
				
	}

}
