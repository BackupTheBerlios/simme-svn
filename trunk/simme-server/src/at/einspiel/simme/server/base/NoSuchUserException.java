package at.einspiel.simme.server.base;

/**
 * Is thrown when a user cannot be found.
 *
 * @author kariem
 */
public class NoSuchUserException extends UserException {

   /**
    * Creates a new <code>NoSuchUserException</code> with the nick name
    * of the <code>User</code> that could not be found.
    * 
    * @param nick The nick name of the user that cannot be found.
    */
   public NoSuchUserException(String nick) {
      super(buildNickMissing(nick));
   }

   /**
    * Adds a cause to the constructor {@link #NoSuchUserException(String)}.
    * 
    * @param nick The nick name of the user.
    * @param cause The cause.
    */
   public NoSuchUserException(String nick, Throwable cause) {
      super(buildNickMissing(nick), cause);
   }

   private static String buildNickMissing(String nick) {
      return ("Der Benutzer \"" + nick + "\" konnte nicht gefunden werden.");
   }

}
