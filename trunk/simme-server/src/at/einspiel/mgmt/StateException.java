package at.einspiel.mgmt;

/**
 * Is thrown if an error occured while changing states.
 * 
 * @author kariem
 */
public class StateException extends Exception {

   /**
    * Simple exception without message.
    */
   public StateException() {
      super();
   }

   /**
    * Simple exception including message.
    * 
    * @param message the message.
    */
   public StateException(String message) {
      super(message);
   }

   /**
    * Exception containing a message and the cause 
    * 
    * @param message the exception's message.
    * @param cause the caus of the exception.
    */
   public StateException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Exception that only contains a cause.
    * 
    * @param cause the exception's cause.
    */
   public StateException(Throwable cause) {
      super(cause);
   }

}
