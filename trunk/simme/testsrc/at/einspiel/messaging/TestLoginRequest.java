// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: TestLoginRequest.java
//                  $Date: 2003/12/30 23:05:29 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.io.IOException;
import java.net.URL;

import javax.microedition.io.HttpConnection;

import at.einspiel.messaging.LoginRequest;

/**
 * Uses a different connection mechanism which is the only method overriden
 * in this class.
 *
 * @author kariem
 */
public class TestLoginRequest extends LoginRequest {
   
   /** @see LoginRequest#LoginRequest(String, String, String, String) */
   public TestLoginRequest(String nick, String pwd, String clientmodel, String version) {
      super(nick, pwd, clientmodel, version);
   }

   /** @see LoginRequest#LoginRequest(String, String, String) */
   public TestLoginRequest(String nick, String pwd, String version) {
      this(nick, pwd, null, version);
   }

   /**
    * Creates a new <code>HttpConnection</code> from a url.
    *
    * @param url a string representing a URL.
    * @return a new <code>HttpConnection</code> to the <code>url</code>.
    *
    * @throws IOException if an error occured while trying to open a
    *         connection.
    */
   public HttpConnection getHttpConnection(String url) throws IOException {
      URL destination = new URL(url);

      return new ConnectionMaker(destination.openConnection());
   }
}
