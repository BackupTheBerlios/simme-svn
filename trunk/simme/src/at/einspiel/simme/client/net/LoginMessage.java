package at.einspiel.simme.client.net;

/**
 * A message that is sent to transmit information for the login process.
 *
 * @author kariem
 */
public class LoginMessage extends Request {
   /**
    * Creates a new message with the given properties.
    *
    * @param nick the nick name to login with.
    * @param pwd password corresponding to the nick name.
    * @param clientmodel client model.
    * @param version version number.
    */
   public LoginMessage(String nick, String pwd, String clientmodel, String version) {
      super();
      setParam("user", nick);
      setParam("pwd", pwd);
      setParam("model", clientmodel);
      setParam("version", version);
   }

   /**
    * Creates a new message with the given properties.
    *
    * @param nick the nick name to login with.
    * @param pwd password corresponding to the nick name.
    * @param version version number.
    */
   public LoginMessage(String nick, String pwd, String version) {
      this(nick, pwd, "", version);
   }

}
