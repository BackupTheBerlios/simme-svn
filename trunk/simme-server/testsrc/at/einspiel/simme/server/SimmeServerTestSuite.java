// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SimmeServerTestSuite.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import at.einspiel.db.*;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testing SimME server module.
 * 
 * @author kariem
 */
public class SimmeServerTestSuite {
	private SimmeServerTestSuite() {
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
		suite.addTestSuite(ServerGameTest.class);
		suite.addTestSuite(ManagedGameTest.class);
		suite.addTestSuite(UserManagerTest.class);
		suite.addTestSuite(SessionManagerTest.class);
		return suite;
	}
}
