// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ChangeSupport.java
//                  $Date: 2004/09/02 10:20:38 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.mgmt;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Changesupport that implements {@link at.einspiel.mgmt.IChangeSupport}. This
 * class may be used by classes implementing the same interface.
 * 
 * @author kariem
 */
public class ChangeSupport implements IChangeSupport, Serializable {

	// the state listeners
	private Set listeners;

	/** @see IChangeSupport#addStateListener(StateListener) */
	public void addStateListener(StateListener listener) {
		if (listeners == null) {
			listeners = new HashSet();
		}
		listeners.add(listener);
	}

	/** @see IChangeSupport#removeStateListener(StateListener) */
	public void removeStateListener(StateListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	/**
	 * Informs all state listeners of the state event.
	 * @param evt
	 *            the event ot be sent.
	 */
	public void fireStateEvent(StateEvent evt) {
		if (listeners != null) {
			for (Iterator i = listeners.iterator(); i.hasNext();) {
				// propagate event to state listeners
				((StateListener) i.next()).updateState(evt);
			}
		}
	}
}