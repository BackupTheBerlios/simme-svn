package test.tests;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author kariem
 */
public class SimmeTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(GameTest.class);
		return suite;
	} 
}
