package test.tests;

import java.io.IOException;
import java.net.URL;

import javax.microedition.io.HttpConnection;

import test.sim.net.LoginMessage;

/**
 * Uses a different connection mechanism which is the only method overriden
 * in this class.
 * 
 * @author kariem
 */
public class TestLoginMessage extends LoginMessage {

   /** @see LoginMessage#LoginMessage(String, String, String, String) */
   public TestLoginMessage(String nick, String pwd, String clientmodel, String version) throws IOException {
      super(nick, pwd, clientmodel, version);
      //request = new TestRequest();
   }

   /** @see LoginMessage#LoginMessage(String, String, String) */
   public TestLoginMessage(String nick, String pwd, String version) throws IOException {
      this(nick, pwd, null, version);
   }
   
   
	public HttpConnection getHttpConnection(String url) throws IOException {
		URL destination = new URL(url);
		return new ConnectionMaker(destination.openConnection());
	}

}
