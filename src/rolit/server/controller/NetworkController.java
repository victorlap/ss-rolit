package rolit.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import rolit.Color;
import rolit.Player;
import rolit.server.Game;


public class NetworkController extends Thread {
	
	public static final String EXTENSIONS 			= "Extensions";
	public static final String LEADEVERYTHING		= "LeadEverything";
	public static final String MOVE					= "Move";
	public static final String LEADALL				= "LeadAll";
	public static final String COLOUR			 	= "Colour";
	public static final String COLOURDENY			= "ColourDeny";
	public static final String GAMESTART			= "GameStart";
	public static final String TURN					= "Turn";
	public static final String MOVEDENY				= "MoveDeny";
	public static final String NOTIFYMOVE			= "NotifyMove";
	public static final String GAMEEND				= "GameEnd";
	public static final String LOSSPLAYER			= "LossPlayer";
	public static final String LEADRETURN			= "LeadReturn";
	public static final String NOTIFYNEWPLAYER		= "NotifyNewPlayer";
	public static final String EXTENSIONSRES 		= "ExtensionsRes";
	public static final String EXTENSIONSCONFIRM 	= "ExtensionsConfirm";
	public static final String COLOURREQ 			= "ColourReq";
	public static final String JOINREQ 				= "JoinReq";
	public static final String JOINCONFIRM 			= "JoinConfirm";
	public static final String JOINDENY 			= "JoinDeny";
	public static final String ENCODE 				= "Encode";
	public static final String SIGNATURE 			= "Signature";
	public static final String DELIM 				= " ";
	
	
	private int port;
	private ServerSocket ss;
	private Collection<ClientHandlerController> threads;
	private Collection<Game> games;
	private ServerController controller;

        /** Constructs a new Server object */
	public NetworkController(int portArg, ServerController controller) {
		this.port = portArg;
		this.threads = new ArrayList<ClientHandlerController>();
		this.controller = controller;
		this.games = new ArrayList<Game>();
	}

	/**
	 * Listens to a port of this Server if there are any Clients that 
         * would like to connect. For every new socket connection a new
         * ClientHandler thread is started that takes care of the further
         * communication with the Client. 
	 */
	public void run() {
		try {
			ss = new ServerSocket(port);
			
			while(true) {

				Socket newSocket = ss.accept();
				ClientHandlerController newHandler = new ClientHandlerController(this, newSocket, controller);
				controller.addMessage("New Connection from: "+ ss.getInetAddress());
				addHandler(newHandler);
				newHandler.start();
				/** Start the communication */
				newHandler.sendMessage(EXTENSIONS);
			}	
		} catch (IOException e) {
			controller.addMessage("Server couldn't start because the port is not available");
		}
	}
	
	public boolean isUsernameInUse(String username) {
		for(ClientHandlerController handler : threads) {
			if(handler.getPlayer().getName().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInGame(Player player) {
		for(Game game : games) {
			for(Player inGamePlayer : game.getPlayers()) {
				if(player.getName().equals(inGamePlayer.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Sends a message using the collection of connected ClientHandlers
         * to all connected Clients.
	 * @param msg message that is sent
	 */
	public void broadcast(String msg) {
		if(msg != null) {
			for(ClientHandlerController handler : threads) {
				handler.sendMessage(msg);
			}
			controller.addMessage("[BROADCAST] "+ msg);
		}
	}
	
	/** Send message to specific client */
	public void broadcast(String msg, ClientHandlerController chc) {
		if(msg != null && chc != null) {
			chc.sendMessage(msg);
			controller.addMessage("[BROADCAST to "+ chc.getSocket().getInetAddress() +"] "+ msg);
		}
	}

	/**
	 * Add a ClientHandler to the collection of ClientHandlers.
	 * @param handler ClientHandler that will be added
	 */
	public void addHandler(ClientHandlerController handler) throws IOException {
		threads.add(handler);
	}

	/**
	 * Remove a ClientHandler from the collection of ClientHanlders. 
	 * @param handler ClientHandler that will be removed
	 */
	public void removeHandler(ClientHandlerController handler) {
		threads.remove(handler);
	}
	
	public Game getFreeGame() {
		for(Game game : games) {
			if(!game.isRunning()) {
				return game;
			}
		}
		Game newGame = new Game();
		games.add(newGame);
		return newGame;
	}
	
	/** Get game by player */
	public Game getGame(Player byPlayer) {
		for(Game game: games) {
			for(Player player : game.getPlayers()) {
				if(player == byPlayer) {
					return game;
				}
			}
		}
		/** Hij zit in geen enkele game */
		return null;
	}
	
	public void execute(String command, ClientHandlerController sender) {
		Scanner in = new Scanner(command);
		String cmd = in.next();
		
		if(cmd.equals(EXTENSIONSRES)) {
			broadcast(EXTENSIONSCONFIRM + DELIM + "1", sender);
		}
		if(cmd.equals(JOINREQ)) {
			/*if(network.isUsernameInUse(username)) {
				network.broadcast(NetworkController.JOINDENY + NetworkController.DELIM + "0");
				shutdown();
			} else {
				this.username = in.next();
				
				byte[] nonce = new byte[16];
				Random rand;
				try {
					rand = SecureRandom.getInstance("SHA1PRNG");
					rand.nextBytes (nonce);
					this.nonce = nonce;
					network.broadcast(NetworkController.ENCODE + NetworkController.DELIM + new String(nonce));
				} catch (NoSuchAlgorithmException e) {
					controller.addMessage("Algorithm Exception");
				}
			}*/
			if(in.hasNext()) {
				String next = in.next();
				if(!isUsernameInUse(next)) {
					sender.getPlayer().setName(next);
					controller.addMessage("Connection "+ sender.getSocket().getInetAddress() + " has identified itself as: "+ sender.getPlayer().getName());
					broadcast(JOINCONFIRM);
					System.out.println("0");
					Game game = getFreeGame();
					System.out.println("1");
					game.addPlayer(sender.getPlayer());
					System.out.println("2");
					broadcast(COLOURREQ + game.freeColorString());
					System.out.println("3");
				}
				else {
					broadcast(JOINDENY + DELIM + "0", sender);
				}
			}			
		}
		if(cmd.equals(COLOUR) && in.hasNextInt()) {
			Game game = getGame(sender.getPlayer());
			if(game != null) {
				Color color = Color.fromInt(in.nextInt());
				if(color != Color.NONE && !game.isColorInUse(color))  {
					sender.getPlayer().setColor(color);
					broadcast(NOTIFYNEWPLAYER + DELIM + game.getPlayerString());
				} else { // Color is already taken
					broadcast(COLOURDENY, sender);
				}
			}
			
			// Hij zit niet in een game, of hij heeft geen kleur meegegeven, dus we doen niks
		}
		/*if(cmd.equals(NetworkController.SIGNATURE)) {
			AuthenticationController ac = new AuthenticationController(controller, this, username);
			ac.start();
		}*/
		in.close();
	}
}
