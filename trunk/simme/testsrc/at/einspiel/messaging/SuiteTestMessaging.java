// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SuiteTestMessaging.java
//                  $Date: 2003/12/30 23:05:29 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for this package.
 * @author kariem
 */
public class SuiteTestMessaging {

   private SuiteTestMessaging() {
      // private constructor for test suite.
   }
   
   /**
    * Returns the test suite for the package at.einspiel.messaging.
    * @return the test suite.
    */
   public static Test suite() {
      TestSuite suite = new TestSuite("Test for package at.einspiel.messaging");
      //$JUnit-BEGIN$
      suite.addTestSuite(SendableUITest.class);
      suite.addTestSuite(NetTest.class);
      //$JUnit-END$
      return suite;
   }
}
