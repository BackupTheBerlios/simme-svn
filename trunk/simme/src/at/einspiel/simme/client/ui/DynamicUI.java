// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: DynamicUI.java
//                  $Date: 2004/08/25 15:36:00 $
//              $Revision: 1.8 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.ui;

import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.lcdui.*;

import at.einspiel.messaging.ISimpleInfo;
import at.einspiel.messaging.Request;
import at.einspiel.messaging.SendableUI;
import at.einspiel.simme.client.Game;
import at.einspiel.simme.client.Sim;
import at.einspiel.simme.client.net.NetworkGame;

/**
 * This class is intended to create dynamically a user interface from the
 * information found in an xml string. For this purpose it uses {@link
 * SendableUI}.
 * 
 * @author jorge
 */
public class DynamicUI implements CommandListener {
	private static final String COMM_PATH = "dynamicState";

	final SendableUI ui;
	boolean updateNecessary;
	String url;
	Displayable disp;

	/**
	 * Creates a new instance of this object with list entries built from the
	 * data found in the given xml string.
	 * 
	 * @param xmlString
	 *            information to build the user interface.
	 */
	public DynamicUI(String xmlString) {
		System.out.println("xmlString=" + xmlString);
		ui = new SendableUI(xmlString);

		getDisplayable();

		disp.addCommand(new Command("Exit", Command.EXIT, 0));
		disp.setCommandListener(this);
	}

	/**
	 * Creates a new dynamic user interface.
	 * 
	 * @param title
	 *            the title.
	 * @param message
	 *            the message to display.
	 * @param url
	 *            the address to connect to.
	 */
	public DynamicUI(String title, String message, String url) {
		System.out.println("message=" + message);
		ui = new SendableUI(title, message);

		this.url = url;
		getDisplayable();

		disp.addCommand(new Command("Abort", Command.CANCEL, 0));
		disp.setCommandListener(this);
	}

	private void connect() {
		if (updateNecessary) {
			ConnectorThread connector = new ConnectorThread();
			connector.start();
		}
	}

	/** @see CommandListener#commandAction(Command, Displayable) */
	public void commandAction(Command cmd, Displayable displayable) {
		Display d = Sim.getDisplay();

		if (cmd.getCommandType() == Command.EXIT) {
			d.setCurrent(Sim.getMainScreen());
		} else {
			Request r = handleCommand();
			if (r == null) {
				return;
			}
			r.sendRequest(COMM_PATH);

			String response = null;

			try {
				response = new String(r.getResponse());
			} catch (IOException e) {
				e.printStackTrace();
			}

			// load response into UI
			ui.initialize(response);

			// show new UI
			d.setCurrent(getDisplayable());
		}
	}

	/**
	 * A request that is generated, if a certain command has been executed.
	 * 
	 * @return a request.
	 */
	private Request handleCommand() {
		if (ui.isList()) {
			// find selected index
			int selected = ((List) disp).getSelectedIndex();

			// send index to server
			Request r = new Request();
			r.setParam("selected", Integer.toString(selected));

			// attach id if possible
			r.setParam("id", Integer.toString(ui.getId()));

			return r;
		}
		return null;
	}

	/**
	 * Returns the current displayable object of the dynamic user interface.
	 * Prior to this call the user interface usually should be initialized by a
	 * call to {@link SendableUI#initialize(String)}.
	 * 
	 * @return a displayable.
	 */
	protected Displayable getDisplayable() {
		disp = makeDisplayable(ui);
		connect();
		return disp;
	}

	Displayable makeDisplayable(SendableUI sui) {
		Displayable d;
		ISimpleInfo infoObject = sui.getInfoObject();
		String title = infoObject.getTitle();
		if (infoObject.isList()) {
			d = makeList(infoObject);
			updateNecessary = false;
		} else if (infoObject.isXml()) {
			// build game with xml information
			Game g = new NetworkGame(infoObject.getXmlInfo(), Sim.getNick(), url);
			// TODO Georg start game, create Zeichenblatt, ...
			d = new Zeichenblatt(false);
			((Zeichenblatt) d).setGame(g);
			updateNecessary = false;
		} else {
			d = new Form(title);
			((Form) d).append(new StringItem("Status:", infoObject.getText()));
			updateNecessary = true;
		}
		return d;
	}

	private Displayable makeList(ISimpleInfo infoObject) {
		List l = new List(infoObject.getTitle(), List.IMPLICIT);

		Enumeration enum = infoObject.getListElements().elements();
		while (enum.hasMoreElements()) {
			String name = (String) enum.nextElement();

			if (name != null) {
				appendToList(l, name, null);
			}
		}
		return l;
	}

	private void appendToList(List l, String name, Image img) {
		l.append(name, img);
	}

	/** Establishes a connection and updates the user interface. */
	class ConnectorThread extends Thread {

		/** @see java.lang.Thread#run() */
		public void run() {
			// send the request
			Request r = new Request();
			r.sendRequest(url);

			try {
				String response = new String(r.getResponse());
				ui.initialize(response);
			} catch (IOException e) {
				// error occured
				ui.initialize("connection broken: " + e.getMessage());
			}
		}
	}
}