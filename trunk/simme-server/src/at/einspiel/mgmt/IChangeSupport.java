// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IChangeSupport.java
//                  $Date: 2004/09/07 13:29:07 $
//              $Revision: 1.2 $
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
   public void addStateListener(IStateListener listener);

   /**
    * Removes a state listener.
    * @param listener listener to be removed.
    */
   public void removeStateListener(IStateListener listener);
   
}
