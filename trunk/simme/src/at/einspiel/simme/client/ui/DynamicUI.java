// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: DynamicUI.java
//                  $Date: 2004/09/16 08:29:35 $
//              $Revision: 1.12 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.ui;

import java.util.Enumeration;

import javax.microedition.lcdui.*;

import at.einspiel.logging.Logger;
import at.einspiel.messaging.*;
import at.einspiel.simme.client.Game;
import at.einspiel.simme.client.Sim;
import at.einspiel.simme.client.net.NetworkGame;
import at.einspiel.simme.client.util.UIUtils;

/**
 * This class is intended to create dynamically a user interface from the
 * information found in an xml string. For this purpose it uses {@link
 * SendableUI}.
 * 
 * @author jorge
 */
public class DynamicUI implements IDynamicUI, CommandListener {

	final SendableUI sui;
	boolean updateNecessary;
	Displayable disp;

	/**
	 * Creates a new dynamic user interface. A subsequent call to
	 * {@linkplain #updateDisplay()}is necessary.
	 * 
	 * @param title
	 *            the title.
	 * @param message
	 *            the message to display.
	 * @param url
	 *            the address to connect to after displaying the message.
	 */
	public DynamicUI(String title, String message, String url) {
		Logger.debug(getClass(), "message=" + message);

		sui = new SendableUI(title, message);
		sui.setDynamicUI(this);
		sui.setCommPath(url);
	}

	/**
	 * Connects to <code>url</code> if an update is necessary. An update
	 * usually is set to be necessary, if the current displayable only shows a
	 * text message, and there is no possible navigation.
	 */
	private synchronized void connect() {
		Logger.debug(getClass(), "called connect()");
		if (updateNecessary) {
			sui.update();
		}
	}

	/** @see CommandListener#commandAction(Command, Displayable) */
	public void commandAction(Command cmd, Displayable displayable) {
		Logger.debug(getClass(), "command " + cmd.getLabel() + " executed");
		if (cmd == UIUtils.CMD_CANCEL) {
			// exit simme online
			Sim.getDisplay().setCurrent(Sim.getMainScreen());
		} else if (cmd == UIUtils.CMD_CONTINUE) {
			// go to the main screen
			connect();
		} else {
			Request r = handleCommand();
			if (r == null) {
				return;
			}

			// dynamic user interface will be updated automatically
			sui.update(r);
		}
	}

	/**
	 * A request that is generated, if a certain command has been executed.
	 * @return a request.
	 */
	private Request handleCommand() {
		if (sui.isList()) {
			// find selected index
			final List currentList = (List) disp;
			final int selected = currentList.getSelectedIndex();

			Logger.debug(getClass(), "selected: " + selected);

			Request r = new Request();

			// send index to server
			r.setParam(IConstants.PARAM_SEL, Integer.toString(selected));

			if (sui.hasMetaInfo()) {
				r.setParam(IConstants.PARAM_META, currentList.getString(selected));
			}

			return r;
		}
		return null;
	}

	/**
	 * Updates the display.
	 */
	public void updateDisplay() {
		Displayable d = makeDisplayable(sui.getInfoObject());

		if (d instanceof GameBoard) {
			// TODO add reference to this ui
		} else {
			// add command listeners
			// command to exit simme online
			d.addCommand(UIUtils.CMD_CANCEL);
			// command to go to the next page
			if (updateNecessary) {
				d.addCommand(UIUtils.CMD_CONTINUE);
			}
			d.setCommandListener(this);
		}

		// point field disp to temporary display
		this.disp = d;

		Sim.getDisplay().setCurrent(disp);
		Logger.debug(getClass(), "Display set to " + disp);
	}

	/**
	 * Creates a displayable from an ISimpleInfo object.
	 * @param infoObject
	 *            the object holding information to create the displayable.
	 * @return a newly created displayable object.
	 */
	Displayable makeDisplayable(ISimpleInfo infoObject) {
		final Displayable newDisp;
		final String title = infoObject.getTitle();
		if (infoObject.isList()) {
			newDisp = makeList(infoObject);
		} else if (infoObject.isXml()) {
			// build game with xml information
			final Game g = new NetworkGame(infoObject.getXmlInfo(), Sim.getNick(), sui
					.getCommPath());
			newDisp = new GameBoard(false);
			g.setDynamicUI((IDynamicUI) newDisp);
			// start game in own thread, UI will be updated correctly
			Thread t = new StartGameThread((GameBoard) newDisp, g);
			t.start();

			updateNecessary = false;
		} else {
			newDisp = UIUtils.uneditableTextComponent(title, infoObject.getText());
			updateNecessary = true;
		}
		return newDisp;
	}

	/**
	 * Creates a {@linkplain List}from an info object. If info object does not
	 * contain any list elements ({@linkplain ISimpleInfo#getListElements()}
	 * returns an empty vector), this method will return a short textual
	 * information instead of a list.
	 * 
	 * @param infoObject
	 *            the object that contains information to create the list.
	 * @return a list object with all the elements contained in the info object,
	 *         or a textual component if there are no elements.
	 * 
	 * @see UIUtils#uneditableTextComponent(String, String)
	 */
	private Displayable makeList(ISimpleInfo infoObject) {
		List l = new List(infoObject.getTitle(), List.IMPLICIT);

		Enumeration enum = infoObject.getListElements().elements();
		while (enum.hasMoreElements()) {
			String name = (String) enum.nextElement();

			if (name != null) {
				l.append(name, null);
			}
		}
		// if the list does not contain any elements, show a short textual info
		if (l.size() == 0) {
			updateNecessary = true;
			return UIUtils.uneditableTextComponent("Leer",
					"Zur Zeit befinden sich hier noch keine Einträge.");
		}

		// some elements are in the list, so return the rest
		updateNecessary = false;
		return l;
	}

	class StartGameThread extends Thread {

		private final GameBoard zeichenblatt;
		private final Game g;

		/**
		 * Creates a new instance of <code>StartGameThread</code>.
		 * @param zeichenblatt
		 * @param g
		 */
		public StartGameThread(GameBoard zeichenblatt, Game g) {
			this.zeichenblatt = zeichenblatt;
			this.g = g;
		}

		/** @see java.lang.Thread#run() */
		public void run() {
			try {
				sleep(1500);
			} catch (InterruptedException e) {
				Logger.error("Error while trying to sleep thread", e);
			}
			zeichenblatt.setGame(g);
		}

	}
}