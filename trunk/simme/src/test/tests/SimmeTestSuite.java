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
      TestSuite suite = new TestSuite();
      suite.addTestSuite(GameTest.class);

      return suite;
   }
}
