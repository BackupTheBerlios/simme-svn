// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SuiteTestSimmeServer.java
//                  $Date: 2004/02/21 23:04:20 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import at.einspiel.db.*;
import at.einspiel.simme.server.menu.SuiteTestMenu;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testing SimME server module.
 * 
 * @author kariem
 */
public class SuiteTestSimmeServer {
	private SuiteTestSimmeServer() {
		// private constructor for test class
	}

	/**
	 * Runs tests for SimME server
	 * 
	 * @return The generated test
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Server");
		suite.addTestSuite(DatabaseTest.class);
      suite.addTest(SuiteTestMenu.suite());
		suite.addTestSuite(ServerGameTest.class);
		suite.addTestSuite(ManagedGameTest.class);
		suite.addTestSuite(UserManagerTest.class);
		suite.addTestSuite(SessionManagerTest.class);
		return suite;
	}
}
