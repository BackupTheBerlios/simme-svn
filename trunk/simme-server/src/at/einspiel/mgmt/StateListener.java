// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: StateListener.java
//                  $Date: 2003/12/30 10:18:25 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.mgmt;

/**
 * Listens to state changes.
 * @author kariem
 */
public interface StateListener {
   /**
    * Updates with an event.
    * @param event the event.
    */
   void updateState(StateEvent event);
   
}
