// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: Action.java
//                  $Date: 2004/09/22 18:25:42 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.midp.ui;

import javax.microedition.lcdui.Displayable;

/**
 * Simple action class.
 */
public abstract class Action {

	/**
	 * Executes this action. The default implementation calls
	 * {@linkplain #execute(Displayable)}.
	 */
	public void execute() {
		execute(null);
	}

	/**
	 * Executes this action with an additional displayable parameter. This
	 * method may be used by implementations of
	 * {@link javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, Displayable)}.
	 * @param d
	 *            the display.
	 */
	public abstract void execute(Displayable d);
}