// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SuiteTestLogging.java
//                  $Date: 2004/08/12 21:56:08 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.logging;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for this package.
 * 
 * @author kariem
 */
public class SuiteTestLogging {

	private SuiteTestLogging() {
		// private constructor for test suite.
	}

	/**
	 * Returns the test suite for the at.einspiel.logging package.
	 * 
	 * @return the test suite.
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for package at.einspiel.logging");
		//$JUnit-BEGIN$
		suite.addTestSuite(LimitedLogTest.class);
		//$JUnit-END$
		return suite;
	}
}