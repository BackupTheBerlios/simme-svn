package at.einspiel.simme.server.base;

/**
 * Is thrown if nick/password combination is not correct. 
 * 
 * @author kariem
 */
public class WrongUserException extends UserException {

   /**
    * Simple constructor.
    */
   public WrongUserException() {
      super();
   }

   /**
    * Constructor with message.
    * 
    * @param message the message for this exception.
    */
   public WrongUserException(String message) {
      super(message);
   }

   /**
    * Constructor with message and cause.
    * 
    * @param message the message for this exception.
    * @param cause the cause.
    */
   public WrongUserException(String message, Throwable cause) {
      super(message, cause);
   }

}
