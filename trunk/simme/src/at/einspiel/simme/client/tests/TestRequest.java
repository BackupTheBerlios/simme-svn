package at.einspiel.simme.client.tests;

import at.einspiel.simme.client.net.Request;
import at.einspiel.simme.client.tests.*;
import tests.*;

import java.io.IOException;

import java.net.URL;

import javax.microedition.io.HttpConnection;

/**
 * Uses a different connection mechanism which is the only method overriden
 * in this class.
 *
 * @author kariem
 */
public class TestRequest extends Request {

   /** @see Request#Request() */
   public TestRequest() {
      super();
   }

   /**
    * Overriden for testing purposes
    *
    * @see Request#getHttpConnection(String)
    */
   public HttpConnection getHttpConnection(String url) throws IOException {
      URL destination = new URL(url);

      return new ConnectionMaker(destination.openConnection());
   }
}
