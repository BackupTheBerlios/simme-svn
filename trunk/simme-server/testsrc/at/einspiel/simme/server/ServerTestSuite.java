package at.einspiel.simme.server;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testing SimME server module.
 * 
 * @author kariem
 */
public class ServerTestSuite {
	private ServerTestSuite() {
		// private constructor for test class
	}

	/**
	 * Runs tests for SimME server
	 * 
	 * @return The generated test
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Server");
		suite.addTestSuite(SessionManagerTest.class);
		suite.addTestSuite(DatabaseTest.class);
		suite.addTestSuite(ServerGameTest.class);
		suite.addTestSuite(ManagedGameTest.class);
		return suite;
	}
}
