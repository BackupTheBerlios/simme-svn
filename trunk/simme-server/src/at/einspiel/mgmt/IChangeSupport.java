// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IChangeSupport.java
//                  $Date: 2003/12/30 10:18:25 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.mgmt;

/**
 * An interface for classes that support publication of their state changes.
 * 
 * @author kariem
 */
public interface IChangeSupport {

   /**
    * Adds a state listener to this object.
    * @param listener the listener.
    */
   public void addStateListener(StateListener listener);

   /**
    * Removes a state listener.
    * @param listener listener to be removed.
    */
   public void removeStateListener(StateListener listener);
   
}
