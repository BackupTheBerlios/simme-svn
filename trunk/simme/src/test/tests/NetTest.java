package test.tests;

import junit.framework.TestCase;

import test.sim.net.Request;

import java.io.IOException;


/**
 * Class used to Test class {@link test.sim.Game}
 *
 * @author kariem
 */
public class NetTest extends TestCase
{
  //private static final String SERVER = "http://localhost:8080/simme/";
  private static final String SERVER = "http://128.131.111.157:8080/simme/";
  private Request req;

  /** @see TestCase#setUp() */
  protected void setUp()
  {
    req = new TestRequest();
  }

  /**
   * Tests connecting to a server
   *
   * @throws IOException if a problem has occured while sending the request.
   */
  public void testConnection() throws IOException
  {
    req.sendRequest(SERVER, ".");
  }

  /**
   * Tests building URLs
   */
  public void testBuildURL()
  {
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
  public void testGetSubmission() throws IOException
  {
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
  public void testPostSubmission() throws IOException
  {
    req.setParam("user", "username2");
    assertEquals("user=username2", req.getParamString(true));

    req.setParam("pwd", "password");
    assertEquals("user=username2&pwd=password", req.getParamString(true));

    req.sendRequest(SERVER, "doLogin.jsp");
  }

  /**
   * Tests sending login information
   *
   * @throws IOException if a problem has occured while sending the request.
   */
  public void testLoginMessage() throws IOException
  {
    TestLoginMessage login = new TestLoginMessage("loginTest", "password",
        "eclipse", "1.0");

    /*
    PrintWriter sysOut = new PrintWriter(System.out);
    login.getXmlElement().write(sysOut);
    sysOut.flush();
    */
    login.sendRequest(SERVER, "doLogin.jsp", false);
    System.out.println(new String(login.getResponse()).trim());
  }
}
