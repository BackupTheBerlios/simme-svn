//----------------------------------------------------------------------------
//[Simme]
//    Java Source File: GameModeForm.java
//               $Date: 2004/09/22 18:25:42 $
//           $Revision$
//----------------------------------------------------------------------------
package at.einspiel.simme.client.ui;

import java.io.IOException;

import javax.microedition.lcdui.*;

import at.einspiel.logging.Logger;
import at.einspiel.messaging.LoginMessage;
import at.einspiel.messaging.LoginRequest;
import at.einspiel.midp.ui.Action;
import at.einspiel.midp.ui.ConnectionScreen;
import at.einspiel.midp.ui.ICommandManager;
import at.einspiel.simme.client.GameRandomAI;
import at.einspiel.simme.client.GameUndoable;
import at.einspiel.simme.client.Sim;
import at.einspiel.simme.client.messages.Messages;
import at.einspiel.simme.client.util.PersonalPrefs;
import at.einspiel.simme.client.util.PrefsException;
import at.einspiel.simme.client.util.UIUtils;

/**
 * <p>
 * Used to choose the game mode. The following choices are presented to the
 * user:
 * <ul>
 * <li>Internet Game</li>
 * <li>Human vs Human (playing on a single device)</li>
 * <li>Human vs Computer</li>
 * </ul>
 * </p>
 * 
 * <p>
 * In order to play over the internet, the client has to be configured with a
 * user defined nick name and password. This only has to be done once.
 * </p>
 * 
 * @author Jorge
 */
public class GameModeForm extends List implements CommandListener {

	private static final Command CMD_CANCEL = new Command(Messages.getString("cmd.quit"),
			Command.BACK, 1);

	private static final String[] CHOICES = {"Internet Spiel", "vs Human", "vs Computer"};

	/**
	 * Creates a new Form where the user can choose between several types of
	 * games.
	 */
	public GameModeForm() {
		super("Game Mode", List.IMPLICIT, CHOICES, null);
		addCommand(UIUtils.CMD_BACK);
		setCommandListener(this);
	}

	/** @see CommandListener#commandAction(Command, Displayable) */
	public void commandAction(Command cmd, Displayable disp) {
		Display d = Sim.getDisplay();

		if (cmd.getCommandType() == Command.BACK) {
			d.setCurrent(Sim.getMainScreen());
		} else {
			GameBoard board = null;
			switch (getSelectedIndex()) {
				case 0 :
					// internet game

					try {
						String[] personalInfo = PersonalPrefs.getPlayerInfo();
						connect(personalInfo);
						/*
						 * ConnectionAlert connectionAlert = new
						 * ConnectionAlert(personalInfo);
						 * d.setCurrent(connectionAlert);
						 * connectionAlert.startConnection(d);
						 */
						break;

					} catch (PrefsException pex) {
						// preferences could not be retrieved.
						Alert error = new Alert("Fehler", pex.getMessage(), null,
								AlertType.ERROR);
						error.setTimeout(Alert.FOREVER);
						d.setCurrent(error, this);
						return;
					}

				case 1 :
					// local game (2 players)
					board = new GameBoard(new GameUndoable());
					break;

				case 2 :
					// local game (vs. computer)
					board = new GameBoard(new GameRandomAI());

			}
			if (board != null) {
				d.setCurrent(board);
				ICommandManager cmdMgr = board.getCommandManager();
				cmdMgr.addCommand(CMD_CANCEL, new Action() {
					/** @see Action#execute(Displayable) */
					public void execute(Displayable display) {
						Sim.setDisplay(GameModeForm.this);
					}
				});
			}
		}
	}

	/**
	 * Connects to the server with the given data as login information.
	 * @param loginData
	 *            the login information.
	 */
	private void connect(final String[] loginData) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					// construct login message
					String version = Sim.getProperty("MIDlet-Version");
					Logger.debug(getClass(), "constructing message (ver. " + version + ")");

					LoginRequest loginMsg = null;

					try {
						loginMsg = new LoginRequest(loginData[0], loginData[1], loginData[3],
								version);
					} catch (NullPointerException npe) {
						npe.printStackTrace();
					}

					ConnectionScreen cs = new ConnectionScreen("SimME Online",
							GameModeForm.this);
					cs.setDescription("Verbinde mit SimME Online");
					cs.setRequest(loginMsg);
					cs.go(Sim.getProperty("simme.page.login"));

					// get response
					Logger.debug(getClass(), "retrieving response");
					String response = new String(cs.getResponse());

					// use response to build result
					Logger.debug(getClass(), "building login message with response: "
							+ response);
					LoginMessage result = new LoginMessage(response);

					Logger.debug(getClass(), "login result: " + result);

					handleResult(loginData[0], result);
				} catch (IOException ioex) {
					handleError(ioex);
				}
			}
		};
		new Thread(r).start();
	}

	/**
	 * Handles the result of the LoginMessage.
	 * @param nick
	 *            the nick name.
	 * @param result
	 *            the login result.
	 */
	private void handleResult(String nick, LoginMessage result) {
		if (result.isSuccess()) {
			final String url = result.getUrl();
			// save nickname and url somewhere in order to be
			// easily accessible without using prefs
			Sim.setNick(nick);

			DynamicUI dUI = new DynamicUI("SimME online", result.getMessage(), url);

			dUI.updateDisplay();

		} else {
			// no success => show cause
			AlertType type = result.isSuccess() ? AlertType.INFO : AlertType.ERROR;
			final String errorMessage = result.getMessage();
			Alert loginAlert = new Alert("Fehler beim Einloggen", errorMessage, null, type);
			loginAlert.setTimeout(Alert.FOREVER);
			Logger.warn("login error: " + errorMessage);
			Display d = Sim.getDisplay();
			d.setCurrent(loginAlert, this);
		}
	}

	/**
	 * Handles a possible error.
	 * @param err
	 *            the exception that caused this error.
	 */
	private void handleError(IOException err) {
		Logger.warn("connection error");

		String errorMsg = err.getMessage();

		// if errorMsg doesn't contain any information, show
		// some standard text
		if ((errorMsg == null) || (errorMsg.length() == 0)) {
			errorMsg = "No connection could be established";
		}

		Alert errorAlert = new Alert("Error", errorMsg, null, AlertType.ERROR);
		errorAlert.setTimeout(Alert.FOREVER);
		Display d = Sim.getDisplay();
		d.setCurrent(errorAlert, this);
	}
}