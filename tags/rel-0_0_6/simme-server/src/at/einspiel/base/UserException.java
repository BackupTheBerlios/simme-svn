package at.einspiel.base;

/**
 * General Exception when using users. This class should be subclassed to
 * inform more precisely about occurred errors. 
 * 
 * @author kariem
 */
public class UserException extends Exception {

   User user;
   
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
   
   /**
    * Creates a new instance of <code>UserException</code>.
    * @param message the message for this exception.
    * @param cause the cause.
    * @param u the optional user.
    */
   public UserException(String message, Throwable cause, User u) {
      this(message, cause);
      this.user = u;
   }

   /**
    * @return Returns the user.
    */
   public User getUser() {
      return user;
   }
   /**
    * @param user The user to set.
    */
   public void setUser(User user) {
      this.user = user;
   }
}
