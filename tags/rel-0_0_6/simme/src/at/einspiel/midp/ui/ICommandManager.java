// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: ICommandManager.java
//                  $Date: 2004/09/22 18:25:42 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.midp.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * An extensible CommandListener interface. This interface can be used to attach
 * different actions to commands.
 * @author kariem
 */
public interface ICommandManager extends CommandListener {

	/**
	 * Sets the display to which the commands will be added. The display's
	 * command listener will be set to this object.
	 * @param d
	 *            the display which shows the commands, after they have been
	 *            added.
	 * @see #addCommand(Command, Action)
	 */
	void setDisplayProvider(Displayable d);

	/**
	 * Adds a command to this command manager. The command will be attached to
	 * the displayable set in {@link #setDisplayProvider(Displayable)}. If
	 * <code>c</code> is selected, the command manager searches the associated
	 * action and calls {@link Action#execute()}.
	 * @param c
	 *            the command to be attached.
	 * @param a
	 *            the associated action. This action will be associated with
	 *            <code>c</code>.
	 */
	void addCommand(Command c, Action a);
	
	/**
	 * Adds an action command to this command manager.
	 * @param ac the action commadn to be added.
	 */
	void addActionCommand(ActionCommand ac);
}