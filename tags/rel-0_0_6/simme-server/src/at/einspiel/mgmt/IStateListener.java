// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IStateListener.java
//                  $Date: 2004/09/07 13:29:29 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.mgmt;

/**
 * Listens to state changes.
 * @author kariem
 */
public interface IStateListener {
   /**
    * Updates with an event.
    * @param event the event.
    */
   void updateState(StateEvent event);
   
}
