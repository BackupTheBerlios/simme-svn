package test.tests;

import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.TestCase;
import test.sim.net.Request;
import test.sim.net.XmlMessage;

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
    * Tests building URLs
    */
   public void testBuildURL() {
      req.setParam("user", "username");
      assertEquals("?user=username", req.getParamString(false));
      assertEquals("user=username", req.getParamString(true));

      req.setParam("pwd", "password");
      assertEquals("?user=username&pwd=password", req.getParamString(false));
      assertEquals("user=username&pwd=password", req.getParamString(true));

      req.setParam("user", "username");
      assertEquals("?user=username&pwd=password", req.getParamString(false));
      assertEquals("user=username&pwd=password", req.getParamString(true));
   }

   /**
    * Tests sending via GET
    * 
    * @throws IOException if a problem has occured while sending the request.
    */
   public void testGetSubmission() throws IOException {
      req.setParam("user", "username");
      assertEquals("?user=username", req.getParamString(false));

      req.setParam("pwd", "password");
      assertEquals("?user=username&pwd=password", req.getParamString(false));
      
      req.sendRequest(SERVER, "doLogin.jsp", false);

   }

   /**
    * Tests sending via POST
    * 
    * @throws IOException if a problem has occured while sending the request.
    */
   public void testPostSubmission() throws IOException {
      req.setParam("user", "username2");
      assertEquals("user=username2", req.getParamString(true));

      req.setParam("pwd", "password");
      assertEquals("user=username2&pwd=password", req.getParamString(true));
      
      req.sendRequest(SERVER, "doLogin.jsp");
   }


   /**
    * Tests parse response into XML and print it
    * 
    * @throws IOException if a problem has occured while sending the request.
    */
   public void testXmlResponse() throws IOException {
      req.setParam("user", "xmltest");
      req.setParam("pwd", "password");
      assertEquals("user=xmltest&pwd=password", req.getParamString(true));
      
      req.sendRequest(SERVER, "doLogin.jsp");
      
      XmlMessage msg = new XmlMessage(req.getResponse());
      msg.write(new PrintWriter(System.out));
      System.out.println();
   }

}
