package test.tests;

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
      suite.addTestSuite(GameTest.class);
      suite.addTestSuite(NetTest.class);

      return suite;
   }
}
