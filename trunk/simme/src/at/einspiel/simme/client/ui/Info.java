//----------------------------------------------------------------------------
//[Simme]
//    Java Source File: Info.java
//               $Date: 2004/06/07 09:27:25 $
//           $Revision$
//----------------------------------------------------------------------------
package at.einspiel.simme.client.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

import at.einspiel.simme.client.messages.Messages;

/**
 * Shows information on this game.
 * 
 * @author jorge
 */
public class Info {

	private static final String HOWTO = Messages.getString("info.text"); //$NON-NLS-1$

	private Info() {
		// private constructor for utility class
	}

	/**
	 * Shows the information
	 * 
	 * @param display
	 *            The display, where the information is to be shown.
	 */
	public static void showInfo(Display display) {
		Alert alert = new Alert(Messages.getString("info.title"), HOWTO, null, AlertType.INFO); //$NON-NLS-1$
		alert.setTimeout(Alert.FOREVER);

		display.setCurrent(alert);
	}
}
