// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: ActionCommand.java
//                  $Date: 2004/09/22 18:25:42 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.midp.ui;

import javax.microedition.lcdui.Command;

/**
 * Combines a command with an action in a simple class.
 * @author kariem
 */
public class ActionCommand {

	/** The action. **/
	protected Action action;
	/** The command. **/
	protected Command command;

	/**
	 * Creates a new instance of <code>ActionCommand</code>.
	 * @param action the action.
	 * @param command the command.
	 */
	public ActionCommand(Action action, Command command) {
		super();
		this.action = action;
		this.command = command;
	}

	/**
	 * @return Returns the action.
	 */
	public Action getAction() {
		return action;
	}
	/**
	 * @param action The action to set.
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return command;
	}
	/**
	 * @param command The command to set.
	 */
	public void setCommand(Command command) {
		this.command = command;
	}
}
