// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: ConnectionScreen.java
//                  $Date: $
//              $Revision$
// ----------------------------------------------------------------------------
package at.einspiel.midp.ui;

import java.io.IOException;

import javax.microedition.lcdui.*;

import at.einspiel.messaging.Request;
import at.einspiel.simme.client.Sim;
import at.einspiel.simme.client.util.UIUtils;

/**
 * A simple Screen that is used to show network activity.
 * 
 * @author kariem
 */
public class ConnectionScreen extends Form implements CommandListener {

	private static final int MAX_VAL = 10;

	private final Gauge itProgress;
	private Request request;
	private Animator anim;

	private Displayable fallBack;

	/**
	 * Creates a new instance of <code>ConnectionScreen</code>.
	 * @param title
	 *            the title of this screen.
	 */
	public ConnectionScreen(String title) {
		this(title, null);
	}

	/**
	 * Creates a new instance of <code>ConnectionScreen</code>.
	 * @param title
	 * @param fallBack
	 */
	public ConnectionScreen(String title, Displayable fallBack) {
		super(title);
		this.fallBack = fallBack;
		// for MIDP 2.0
		//itProgress = new Gauge("Connecting", false, Gauge.INDEFINITE,
		// Gauge.CONTINUOUS_RUNNING);
		itProgress = new Gauge("Connecting", false, MAX_VAL - 1, 0);
		append(itProgress);
		
		// add cancel command
		addCommand(UIUtils.CMD_CANCEL);
	}

	/**
	 * Sets the description of the progress bar.
	 * @param description
	 *            the new description.
	 */
	public void setDescription(String description) {
		itProgress.setLabel(description);
	}

	/**
	 * Sets the request for this connection screen.
	 * @param request
	 *            the request used to connect to the server.
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/**
	 * Sends the request.
	 * @param path
	 *            the path to send the request to.
	 */
	public void go(String path) {
		anim = new Animator();
		anim.start();
		request.sendRequest(path);
	}

	/**
	 * Returns the request's response. This method blocks until the response is
	 * received or the default timeout is reached.
	 * 
	 * @return the response.
	 * 
	 * @throws IOException
	 *             if an error occured.
	 */
	public byte[] getResponse() throws IOException {
		try {
			return request.getResponse();
		} catch (IOException e) {
			throw e;
		} finally {
			// finally block to ensure that animator will be released
			cleanUp();
		}
	}

	private void cancel() {
		request.cancel();
		cleanUp();
	}

	/** Used to clean up */
	private void cleanUp() {
		if (anim != null) {
			anim.end();
			anim = null;
		}
		if (fallBack != null) {
			Sim.setDisplay(fallBack);
		}
	}

	/** @see CommandListener#commandAction(Command, Displayable) */
	public void commandAction(Command arg0, Displayable arg1) {
		if (arg0 == UIUtils.CMD_CANCEL) {
			cancel();
		}
	}

	private class Animator extends Thread {
		private boolean isRunning = true;

		/** @see java.lang.Thread#run() */
		public void run() {
			// set the display correctly
			Sim.setDisplay(ConnectionScreen.this);

			
			int pos = 1;
			while (isRunning) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// ignore, not important
				}
				pos++;
				pos = pos % MAX_VAL;
				itProgress.setValue(pos);
			}
		}

		/** Ends the animator. */
		public void end() {
			isRunning = false;
		}

	}
}