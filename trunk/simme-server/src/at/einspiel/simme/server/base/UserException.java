package at.einspiel.simme.server.base;

/**
 * General Exception when using users. This class should be subclassed to
 * inform more precisely about occurred errors. 
 * 
 * @author kariem
 */
public class UserException extends Exception {

   /**
    * Simple constructor
    */
   public UserException() {
      super();
   }

   /**
    * Constructor with string.
    * 
    * @param message the message for this exception
    */
   public UserException(String message) {
      super(message);
   }

   /**
    * Constructor with string and cause.
    * 
    * @param message the message for this exception.
    * @param cause the cause.
    */
   public UserException(String message, Throwable cause) {
      super(message, cause);
   }

}
