package at.einspiel.messaging;

import java.io.IOException;

import junit.framework.TestCase;
import at.einspiel.messaging.Request;

/**
 * Class used to Test class network methods.
 * 
 * @author kariem
 */
public class NetTest extends TestCase {
	private static final String LOGIN_PAGE = "doLogin";
	
	private static final String SERVER = "http://localhost:8080/simme/";
	//private static final String SERVER =
	// "http://128.131.111.157:8080/simme/";
	private Request req;

	/** @see TestCase#setUp() */
	protected void setUp() {
		req = new TestRequest();
	}

	/** Tests connecting to a server */
	public void testConnection() {
		req.sendRequest(SERVER, ".");
	}

	/** Tests building URLs */
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

	/** Tests sending via GET */
	public void testGetSubmission() {
		req.setParam("user", "username");
		assertEquals("?user=username", req.getParamString(false));

		req.setParam("pwd", "password");
		assertEquals("?user=username&pwd=password", req.getParamString(false));

		req.sendRequest(SERVER, LOGIN_PAGE, false);
	}

	/** Tests sending via POST */
	public void testPostSubmission() {
		req.setParam("user", "username2");
		assertEquals("user=username2", req.getParamString(true));

		req.setParam("pwd", "password");
		assertEquals("user=username2&pwd=password", req.getParamString(true));

		req.sendRequest(SERVER, LOGIN_PAGE);
	}

	/**
	 * Tests sending login information
	 * 
	 * @throws IOException
	 *             if a problem has occured while sending the request.
	 */
	public void testLoginMessage() throws IOException {
		TestLoginRequest login = new TestLoginRequest("loginTest", "password", "eclipse", "1.0");

		/*
		 * PrintWriter sysOut = new PrintWriter(System.out);
		 * login.getXmlElement().write(sysOut); sysOut.flush();
		 */
		login.sendRequest(SERVER, LOGIN_PAGE, true);
		System.out.println(new String(login.getResponse()).trim());
	}
}