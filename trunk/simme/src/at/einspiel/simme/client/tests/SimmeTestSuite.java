package at.einspiel.simme.client.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testing simme module
 *
 * @author kariem
 */
public class SimmeTestSuite {

    /**
     * Runs tests.
     *
     * @return the Test which is executed.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for SimME client");
        suite.addTestSuite(MoveTest.class);
        suite.addTestSuite(GameTest.class);
        //suite.addTestSuite(NetTest.class);
        suite.addTestSuite(SendableUITest.class);
        return suite;
    }
}
