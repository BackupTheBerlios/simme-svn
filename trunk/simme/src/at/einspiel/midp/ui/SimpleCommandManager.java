// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SimpleCommandManager.java
//                  $Date: 2004/09/22 18:25:42 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.midp.ui;

import java.util.Hashtable;

import javax.microedition.lcdui.*;

/**
 * This class can map one command to one action.
 * @author kariem
 */
public class SimpleCommandManager implements ICommandManager, CommandListener {

	private Displayable d;
	/** Maps commands to actions: cmd => action */
	final protected Hashtable m;

	/**
	 * Creates a new instance of <code>SimpleCommandManager</code>.
	 * @param d
	 *            the displayable
	 */
	public SimpleCommandManager(Displayable d) {
		this(d, null);
	}

	/**
	 * Creates a new instance of <code>SimpleCommandManager</code>.
	 * @param d
	 *            the displayable
	 * @param listSelectAction
	 *            the action which will be used for list selection. This
	 *            parameter may be <code>null</code>.
	 */
	public SimpleCommandManager(Displayable d, Action listSelectAction) {
		setDisplayProvider(d);
		m = new Hashtable(2);
		if (listSelectAction != null) {
			addCommand(List.SELECT_COMMAND, listSelectAction);
		}
	}

	/**
	 * Sets the single display provider for this command manager. The command
	 * manager will be the command listener for the display and will receive
	 * calls to {@link CommandListener#commandAction(Command, Displayable)}.
	 * @see ICommandManager#setDisplayProvider(Displayable)
	 */
	public void setDisplayProvider(Displayable d) {
		this.d = d;
		d.setCommandListener(this);
	}

	/**
	 * Associates the command with the action. One command can only be
	 * associated with a single action. The action will be executed with a call
	 * to {@link #commandAction(Command, Displayable)}. Please note, that the
	 * action's execution should return immediately according to the
	 * specification.
	 * @see ICommandManager#addCommand(Command, Action)
	 */
	public void addCommand(Command c, Action a) {
		m.put(c, a);
		d.addCommand(c);
	}
	
	/** @see ICommandManager#addActionCommand(ActionCommand) */
	public void addActionCommand(ActionCommand ac) {
		addCommand(ac.command, ac.action);
	}

	/**
	 * Executes the action associated with <code>cmd</code>.
	 * @see CommandListener#commandAction(Command, Displayable)
	 * @throws NullPointerException
	 *             if no action is associated with the given command.
	 */
	public void commandAction(Command cmd, Displayable disp) throws NullPointerException {
		Action a = (Action) m.get(cmd);
		a.execute(disp);
	}
}