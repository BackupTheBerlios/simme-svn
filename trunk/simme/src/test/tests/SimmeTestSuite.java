package test.tests;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author kariem
 */
public class SimmeTestSuite {

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public static Test suite() {
      TestSuite suite = new TestSuite();
      suite.addTestSuite(GameTest.class);

      return suite;
   }
}
