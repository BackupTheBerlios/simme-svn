// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SimmeServerTestSuite.java
//                  $Date: 2003/12/30 10:18:25 $
//              $Revision: 1.2 $
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
		suite.addTestSuite(SessionManagerTest.class);
		suite.addTestSuite(DatabaseTest.class);
		suite.addTestSuite(ServerGameTest.class);
		suite.addTestSuite(ManagedGameTest.class);
		return suite;
	}
}
