// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SimulatedPlayer.java
//                  $Date: 2004/09/22 18:22:05 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.einspiel.logging.Logger;
import at.einspiel.messaging.*;
import at.einspiel.midp.ui.IDynamicUI;
import at.einspiel.simme.client.Game;
import at.einspiel.simme.client.GameInfo;
import at.einspiel.simme.client.Move;

/**
 * A class that simulates a player in SimME.
 * 
 * @author kariem
 */
public class SimulatedPlayer implements IDynamicUI {

	String nick;
	String pwd;
	private String commPath;
	private boolean loggedIn;
	private final SendableUI sUI;
	private final Request r;
	private NetworkGame game;

	/**
	 * Creates a new instance of <code>SimulatedPlayer</code>.
	 * @param nick
	 *            the nick name.
	 * @param pwd
	 *            the password
	 */
	public SimulatedPlayer(String nick, String pwd) {
		this.nick = nick;
		this.pwd = pwd;
		this.r = new TestRequest() {
			/** @see at.einspiel.messaging.Request#addUserData() */
			public void addUserData() {
				// instead of retrieving the nick from Sim, set it to "nick"
				setParam(IConstants.PARAM_USER, SimulatedPlayer.this.nick);
			}
		};
		this.sUI = new SendableUI("test", "test") {

			/** @see at.einspiel.messaging.SendableUI#initialize(java.lang.String) */
			public void initialize(String xmlString) {
				Logger.debug(getClass(), "initializing with " + xmlString);
				super.initialize(xmlString);
			}

			/** @see SendableUI#update() */
			public void update() {
				// update with the special test request
				update(r);
			}
		};
		sUI.setDynamicUI(this);

	}

	/**
	 * Connects the player to the network and starts a login.
	 * 
	 * @return the communication path used for further communication.
	 * @throws IOException
	 *             if an error occurs during login.
	 */
	public String login() throws IOException {
		TestLoginRequest loginRequest = new TestLoginRequest(nick, pwd, "testclient");
		loginRequest.sendRequest("Login");
		String response = null;
		try {
			response = new String(loginRequest.getResponse());
		} catch (IOException ioex) {
			ioex.printStackTrace();
			throw ioex;
		}
		LoginMessage result = new LoginMessage(response);
		this.commPath = result.getUrl();
		loggedIn = result.isSuccess();
		if (!loggedIn) {
			throw new IOException("Could not login: " + result.getMessage());
		}
		return commPath;
	}

	/**
	 * Starts a game and returns the game id. If the player has not logged in
	 * yet, {@linkplain #login()}will be called prior to starting the game.
	 * 
	 * @return the game id.
	 * @throws IOException
	 *             if a network error occurs.
	 */
	public NetworkGame startGame() throws IOException {
		if (!loggedIn) {
			login();
		}
		sUI.setCommPath(commPath);
		sUI.update();

		long timeStart = System.currentTimeMillis();
		while (game == null) {
			// see if currentTime - timeStart > 10000;
			long currentTime = System.currentTimeMillis();
			if (currentTime - timeStart > 40000) {
				throw new IOException("Could not start game, timeout reached");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return game;
	}

	/** @see at.einspiel.midp.ui.IDynamicUI#updateDisplay() */
	public void updateDisplay() {
		ISimpleInfo info = sUI.getInfoObject();
		if (info instanceof InfoXml) {
			game = new NetworkGame(info.getXmlInfo(), nick, "default");
		} else if (info instanceof InfoList) {
			// select first element
			r.setParam(IConstants.PARAM_SEL, "0");
			sUI.update(r);
		} else {
			// do nothing, sUI will update automatically
		}
	}

	/**
	 * Plays a game, if a game is currently available for this player. If there
	 * is no game available, an exception is thrown.
	 * @throws Exception
	 *             if no game is available.
	 * @see #startGame()
	 */
	public void play() throws Exception {
		if (game == null) {
			throw new Exception("No game available to play. Start game first.");
		}
		synchronized (game) {
			// start the game as it is done on the client
			// if the player is not the first to make a move, the network game
			// will automatically try to connect to the server.
			game.start();

			GameInfo info = game.getGameInfo();
			String p1Nick = info.getP1Name();
			byte localPlayer = nick.equals(p1Nick) ? Game.PLAYER1 : Game.PLAYER2;

			assert nick.equals(game.getPlayerName(localPlayer)) : "Problem with player names: p1="
					+ info.getP1Name() + ", p2=" + info.getP2Name() + ", player=" + nick;

			while (!game.isGameOver()) {
				makeMoveOrWait(localPlayer);
			}

			game.notifyAll();
		}
	}

	// either makes a move or waits for the opponent's next move
	private void makeMoveOrWait(byte localPlayer) {
		if (game.getPlayersTurn() != localPlayer) {
			// wait for the next move
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}

		// get a view of all edges
		byte[] edges = new byte[Move.MAX_EDGE_INDEX];
		for (byte i = 0; i < edges.length; i++) {
			edges[i] = game.getEdgeOwner(i);
		}
		// filter those which are available
		List available = new ArrayList();
		for (byte i = 0; i < edges.length; i++) {
			if (edges[i] == Game.NEUTRAL) {
				available.add(new Byte(i));
			}
		}

		// according to Ramsey theory there must be at least one remaining
		// element
		assert !available.isEmpty() : "No moves available";

		// shuffle available
		Collections.shuffle(available);
		// select first element
		byte selectedEdge = ((Byte) available.get(0)).byteValue();
		// make the move
		game.selectEdge(selectedEdge);
	}

	/**
	 * Returns the game.
	 * @return the game, if a game has been created. Otherwise this method
	 *         return <code>null</code>.
	 */
	public NetworkGame getGame() {
		return game;
	}

	/**
	 * Sets the game.
	 * @param game
	 *            the game to set.
	 */
	public void setGame(NetworkGame game) {
		this.game = game;
	}

	/**
	 * Returns a runnable that calls {@linkplain #startGame()}. The method is
	 * called from a from within {@linkplain Thread#run()}.
	 * @return a runnable object, that can start a game for this player.
	 * @see #startGame()
	 */
	public Runnable getStartRunnable() {
		return new StartRunner();
	}

	/**
	 * Returns a runnable that calls {@linkplain #play()}. The method is called
	 * from a from within {@linkplain Thread#run()}.
	 * 
	 * @return a runnable object that can simulate playing a game.
	 */
	public Runnable getPlayRunnable() {
		return new PlayRunner();
	}

	class StartRunner implements Runnable {

		/** @see java.lang.Runnable#run() */
		public void run() {
			try {
				SimulatedPlayer.this.startGame();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class PlayRunner implements Runnable {
		/** @see java.lang.Runnable#run() */
		public void run() {
			try {
				SimulatedPlayer.this.play();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}