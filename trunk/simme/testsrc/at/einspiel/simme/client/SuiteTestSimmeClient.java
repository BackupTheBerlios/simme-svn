// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SuiteTestSimmeClient.java
//                  $Date: 2004/08/12 22:00:07 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client;

import junit.framework.Test;
import junit.framework.TestSuite;
import at.einspiel.logging.SuiteTestLogging;
import at.einspiel.messaging.SuiteTestMessaging;
import at.einspiel.simme.client.net.SuiteTestNet;

/**
 * Class to test simme module.
 * 
 * @author kariem
 */
public class SuiteTestSimmeClient {

	private SuiteTestSimmeClient() {
		// private constructor for test suite.
	}

	/**
	 * Returns the test suite for the SimME client.
	 * 
	 * @return the test suite.
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test all SimME client test cases");
		//$JUnit-BEGIN$
		suite.addTest(SuiteTestMessaging.suite());
		suite.addTest(SuiteTestNet.suite());
		suite.addTest(SuiteTestLogging.suite());
		suite.addTestSuite(MoveTest.class);
		suite.addTestSuite(GameTest.class);
		suite.addTestSuite(GameRandomAITest.class);
		//$JUnit-END$
		return suite;
	}
}