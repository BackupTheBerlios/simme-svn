// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: LoginRequest.java
//                  $Date: 2003/12/30 23:05:29 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;


/**
 * A request that is sent to transmit information for the login process.
 *
 * @author kariem
 */
public class LoginRequest extends Request {
   /**
    * Creates a new message with the given properties.
    *
    * @param nick the nick name to login with.
    * @param pwd password corresponding to the nick name.
    * @param clientmodel client model.
    * @param version version number.
    */
   public LoginRequest(String nick, String pwd, String clientmodel, String version) {
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
   public LoginRequest(String nick, String pwd, String version) {
      this(nick, pwd, "", version);
   }

}
