package at.einspiel.simme.server.messaging;

/**
 * The result of a login process.
 * 
 * @author kariem
 */
public class LoginResult extends Message {

   /** login succeeded */
   public static final int LOGIN_OK = 0;
   /** login failed */
   public static final int LOGIN_FAIL = 1;

   private boolean succeed;

   /**
    * Creates a new <code>LoginResult</code>.
    * 
    * @param user user name.
    * @param pwd user's password.
    */
   public LoginResult(String user, String pwd) {
      // TODO try to create new user and act according to result
   }

   /** @see java.lang.Object#toString() */
   public String toString() {
      return makeMessage();
   }

   /**
    * Creates an XML message from the information of the login process.
    * 
    * @return an XML message
    */
   private String makeMessage() {
      return null;
   }

}
