// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: StateEvent.java
//                  $Date: 2003/12/30 10:18:25 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.mgmt;

/**
 * @author kariem
 */
public class StateEvent {

   private Object source;
   private Object oldVal;
   private Object newVal;

   /**
    * Creates a new instance of <code>UpdateStateEvent</code>.
    * @param source the source.
    * @param oldVal the old value.
    * @param newVal the new value.
    */
   public StateEvent(Object source, Object oldVal, Object newVal) {
      this.source = source;
      this.oldVal = oldVal;
      this.newVal = newVal;
   }

   /**
    * @return Returns the newVal.
    */
   public Object getNewVal() {
      return newVal;
   }

   /**
    * @param newVal The newVal to set.
    */
   public void setNewVal(Object newVal) {
      this.newVal = newVal;
   }

   /**
    * @return Returns the oldVal.
    */
   public Object getOldVal() {
      return oldVal;
   }

   /**
    * @param oldVal The oldVal to set.
    */
   public void setOldVal(Object oldVal) {
      this.oldVal = oldVal;
   }

   /**
    * @return Returns the source.
    */
   public Object getSource() {
      return source;
   }

   /**
    * @param source The source to set.
    */
   public void setSource(Object source) {
      this.source = source;
   }
}
