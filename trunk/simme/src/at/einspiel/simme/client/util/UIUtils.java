// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: UIUtils.java
//                  $Date: 2004/09/21 16:20:39 $
//              $Revision$
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.util;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.Displayable;

import at.einspiel.simme.client.messages.Messages;

/**
 * Several methods for UI manipulation and creation.
 * 
 * @author kariem
 */
public class UIUtils {

	/** Quits the application. */
	public static final Command CMD_QUIT = new Command(Messages.getString("cmd.quit"), Command.EXIT, 0);
	/** Cancel command to leave from current menu or exit from simme online. */
	public static final Command CMD_CANCEL = new Command(Messages.getString("cmd.cancel"), Command.CANCEL, 1);
	/** Goes back. */
	public static final Command CMD_BACK = new Command(Messages.getString("cmd.back"), Command.BACK, 3);
	/** Chooses current selection. */
	public static final Command CMD_CHOOSE = new Command(Messages.getString("cmd.choose"), Command.OK, 4);
	/** Command that continues to the next screen. */
	public static final Command CMD_CONTINUE = new Command(Messages.getString("cmd.continue"), Command.SCREEN, 5);
	/** Command that continues to the next screen. */
	public static final Command CMD_OK = new Command(Messages.getString("cmd.ok"), Command.OK, 7);

	/** Private constructor for utility class. */
	private UIUtils() {
		// no implementation
	}

	/**
	 * Creates a new uneditable text component with the specified title and
	 * text.
	 * 
	 * @param title
	 *            the component's title.
	 * @param text
	 *            the component's text.
	 * @return a newly created text component which is not editable.
	 */
	public static Displayable uneditableTextComponent(String title, String text) {
		final Item textItem = new StringItem(null, text);
		return new Form(title, new Item[]{textItem});
	}

}