//----------------------------------------------------------------------------
//[Simme]
//    Java Source File: TestRequest.java
//               $Date: 2004/09/13 15:22:00 $
//           $Revision: 1.3 $
//----------------------------------------------------------------------------
package at.einspiel.messaging;

import at.einspiel.messaging.Request;

import java.io.IOException;

import java.net.URL;

import javax.microedition.io.HttpConnection;

/**
 * Uses a different connection mechanism which is the only method overriden
 * in this class.
 *
 * @author kariem
 */
public class TestRequest extends Request implements ITestRequest {

	/** @see Request#Request() */
   public TestRequest() {
      super();
   }
   
	/** @see Request#Request(int) */
	public TestRequest(int timeout) {
		super(timeout);
	}
	
	/** @see at.einspiel.messaging.Request#sendRequest(java.lang.String) */
	public void sendRequest(String path) {
		super.sendRequest(TEST_SERVER, path);
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
