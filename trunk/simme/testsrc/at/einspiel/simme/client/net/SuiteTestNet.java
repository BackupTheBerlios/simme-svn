// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SuiteTestNet.java
//                  $Date: 2004/06/07 09:27:25 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for this package.
 * @author kariem
 */
public class SuiteTestNet {

   private SuiteTestNet() {
      // private constructor for test suite.
   }
   
   /**
    * Returns the test suite for the at.einspiel.simme.client.net package.
    * @return the test suite.
    */
   public static Test suite() {
      TestSuite suite = new TestSuite("Test for package at.einspiel.simme.client.net");
      //$JUnit-BEGIN$
      suite.addTestSuite(MoveMessageTest.class);
      //$JUnit-END$
      return suite;
   }
}
