// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SimulatedPlayer.java
//                  $Date: 2004/09/13 15:22:00 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import java.io.IOException;

import at.einspiel.logging.Logger;
import at.einspiel.messaging.*;
import at.einspiel.simme.client.ui.IDynamicUI;

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

	/** @see at.einspiel.simme.client.ui.IDynamicUI#updateDisplay() */
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
	 * Returns a runnable that calls {@linkplain #startGame()}from within the
	 * {@linkplain Thread#run()}method.
	 * @return
	 */
	public Runnable getStartRunnable() {
		return new StartRunner();
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
}