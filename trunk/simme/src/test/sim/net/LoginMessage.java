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
   public LoginMessage(String nick, String pwd, String clientmodel, String version) throws IOException {
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

   /**
    * Creates an XML string with the given parameters.
    * 
    * @param nick the nick name to login with.
    * @param pwd password corresponding to the nick name.
    * @param clientmodel client model.
    * @param version version number.
    * 
    * @return a String in the following form:
    *     <pre>&lt;login
    *            user="nick"
    *            pwd="pwd"
    *            client="clientmodel"
    *            version="version"
    *          /&gt;
    *     </pre>
    */
   public static String makeLoginMessage(String nick, String pwd, String clientmodel, String version) {
      StringBuffer buf = new StringBuffer();
      buf.append("<login user=\"");
      buf.append(nick);
      buf.append("\" pwd=\"");
      buf.append(pwd);
      buf.append("\" client=\"");
      buf.append(clientmodel);
      buf.append("\" version=\"");
      buf.append(version);
      buf.append("\" />");

      return buf.toString();
   }
}
