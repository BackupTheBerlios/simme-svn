package test.tests;

import java.io.IOException;

import junit.framework.TestCase;
import test.sim.net.Request;


/**
 * Class used to Test class {@link test.sim.Game}
 *
 * @author kariem
 */
public class NetTest extends TestCase {

   private Request req;
   private static final String SERVER = "http://localhost:8080/simme/";
   

   /** @see TestCase#setUp() */
   protected void setUp() {
      req = new TestRequest();
   }

   /**
    * Tests connecting to a server
    * 
    * @throws IOException if a problem has occured while sending the request.
    */
   public void testConnection() throws IOException {
      req.sendRequest(SERVER, ".");
   }
   
   /**
    * Tests receiving responses
    * 
    * @throws IOException if a problem has occured while sending the request.
    */
   public void testResponse() throws IOException {
      req.sendRequest(SERVER, ".");
   }

}
