package at.einspiel.simme.client.ui;

import java.io.IOException;

import javax.microedition.lcdui.*;

import at.einspiel.logging.Logger;
import at.einspiel.messaging.LoginMessage;
import at.einspiel.messaging.LoginRequest;
import at.einspiel.simme.client.Sim;
import at.einspiel.simme.client.util.PersonalPrefs;
import at.einspiel.simme.client.util.PrefsException;

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

	private static final String[] CHOICES = {"Internet Spiel", "vs Human", "vs Computer"};

	/**
	 * Creates a new Form where the user can choose between several types of
	 * games.
	 */
	public GameModeForm() {
		super("Game Mode", List.IMPLICIT, CHOICES, null);
		addCommand(new Command("Zurück", Command.BACK, 0));
		addCommand(new Command("Auswahl", Command.OK, 1));
		setCommandListener(this);
	}

	/** @see CommandListener#commandAction(Command, Displayable) */
	public void commandAction(Command cmd, Displayable disp) {
		Display d = Sim.getDisplay();

		if (cmd.getCommandType() == Command.BACK) {
			d.setCurrent(Sim.getMainScreen());
		} else {
			switch (getSelectedIndex()) {
				case 0 :
					// Internet Spiel

					try {
						String[] personalInfo = PersonalPrefs.getPlayerInfo();
						ConnectionAlert infoAlert = new ConnectionAlert(personalInfo);
						d.setCurrent(infoAlert);
						infoAlert.startConnection(d);
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
					// Lokales Spiel (2 Spieler)
					d.setCurrent(new Zeichenblatt());
					break;

				case 2 :
					// Lokales Spiel (gg. Computer)
					d.setCurrent(new Zeichenblatt(true));
					break;

			}
		}
	}

	private class ConnectionAlert extends Alert {
		String[] loginData;

		/**
		 * Constructs an empty <code>InfoAlert</code> with the given title
		 * 
		 * @param data
		 *            the data for the alert.
		 */
		ConnectionAlert(String[] data) {
			super("Connecting ...");
			setString("Connection to the server is being established.");
			// Alert will be substituted by result message
			setTimeout(Alert.FOREVER);
			loginData = data;

			// TODO remove debug output
			for (int i = 0; i < data.length; i++) {
				if (data[i] != null) {
					Logger.debug(i + ":" + data[i]);
				}
			}
			Logger.debug("connectionalert created");
		}

		/**
		 * Initializes the connection and shows its output.
		 * 
		 * @param d
		 *            the display which is used to show the output.
		 */
		void startConnection(final Display d) {
			// enter new thread, so that the user interface is updated correctly
			Thread t = new Thread() {
				/** @see java.lang.Thread#run() */
				public void run() {
					try {
						Logger.debug("constructing message");

						// construct login message
						String version = Sim.getProperty("MIDlet-Version");
						Logger.debug("version: " + version);

						LoginRequest loginMsg = null;

						try {
							loginMsg = new LoginRequest(loginData[0], loginData[1],
									loginData[3], version);
						} catch (NullPointerException npe) {
							npe.printStackTrace();
						}

						Logger.debug("sending message");
						loginMsg.sendRequest(Sim.getProperty("simme.page.login"));

						// get response
						String response = new String(loginMsg.getResponse());

						// use response to build result
						LoginMessage result = new LoginMessage(response);

						Logger.debug("login result: " + result);

						if (result.isSuccess()) {
							final String url = result.getUrl();
							// save nickname and url somewhere in order to be
							// easily accessible
							Sim.setNick(loginData[0]);

							DynamicUI dUI = new DynamicUI("SimME online", result.getMessage(),
									url);
							d.setCurrent(dUI.getDisplayable());

						} else {
							// no success => show cause
							AlertType type = result.isSuccess()
									? AlertType.INFO
									: AlertType.ERROR;
							final String errorMessage = result.getMessage();
							Alert loginAlert = new Alert("Error while logging in",
									errorMessage, null, type);
							loginAlert.setTimeout(FOREVER);
							Logger.info("login error: " + errorMessage);
							d.setCurrent(loginAlert);
						}
					} catch (IOException ioex) {
						Logger.warn("connection error");

						String errorMsg = ioex.getMessage();

						// if errorMsg doesn't contain any information, show
						// some standard text
						if ((errorMsg == null) || (errorMsg.length() == 0)) {
							errorMsg = "No connection could be established";
						}

						Alert errorAlert = new Alert("Error", errorMsg, null, AlertType.ERROR);
						errorAlert.setTimeout(FOREVER);
						d.setCurrent(errorAlert);
					}
				}
			};

			t.start();
		}
	}
}