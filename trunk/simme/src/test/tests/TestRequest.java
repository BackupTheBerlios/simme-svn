package test.tests;

import java.io.IOException;

import java.net.URL;

import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import test.sim.net.Request;

/**
 * Uses a different connection mechanism which is the only method overriden
 * in this class.
 * 
 * @author kariem
 */
public class TestRequest extends Request {

   /** @see Request#Request(Hashtable) */
   public TestRequest(Hashtable parameters) {
      super(parameters);
   }

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
