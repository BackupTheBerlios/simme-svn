package test.tests;

import test.sim.net.LoginMessage;

import java.io.IOException;

import java.net.URL;

import javax.microedition.io.HttpConnection;

/**
 * Uses a different connection mechanism which is the only method overriden
 * in this class.
 *
 * @author kariem
 */
public class TestLoginMessage extends LoginMessage {
   
   /** @see LoginMessage#LoginMessage(String, String, String, String) */
   public TestLoginMessage(String nick, String pwd, String clientmodel, String version)
      throws IOException {
      super(nick, pwd, clientmodel, version);
   }

   /** @see LoginMessage#LoginMessage(String, String, String) */
   public TestLoginMessage(String nick, String pwd, String version) throws IOException {
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
