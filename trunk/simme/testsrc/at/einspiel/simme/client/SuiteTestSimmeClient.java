// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SuiteTestSimmeClient.java
//                  $Date: 2003/12/30 23:05:29 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client;

import junit.framework.Test;
import junit.framework.TestSuite;
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
    * @return the test suite.
    */
   public static Test suite() {
      TestSuite suite = new TestSuite("Test for all SimME client");
      //$JUnit-BEGIN$
      suite.addTest(SuiteTestMessaging.suite());
      suite.addTest(SuiteTestNet.suite());
      suite.addTestSuite(MoveTest.class);
      suite.addTestSuite(GameTest.class);
      //$JUnit-END$
      return suite;
   }
}
