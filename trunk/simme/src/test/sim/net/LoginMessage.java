package test.sim.net;

import java.io.IOException;

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
    *
    * @throws IOException if an error occured while creating the XML information
    */
   public LoginMessage(String nick, String pwd, String clientmodel, String version)
      throws IOException {
      //super(makeLoginMessage(nick, pwd, clientmodel, version));
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
    *
    * @throws IOException if an error occured while creating the XML information
    */
   public LoginMessage(String nick, String pwd, String version) throws IOException {
      this(nick, pwd, "", version);
   }

}
