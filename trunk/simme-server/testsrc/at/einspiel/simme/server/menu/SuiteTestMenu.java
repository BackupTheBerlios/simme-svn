// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SuiteTestMenu.java
//                  $Date: 2004/02/21 23:04:20 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for this package.
 * @author kariem
 */
public class SuiteTestMenu {

   /**
    * Runs tests for the package at.einspiel.simme.server.menu
    * 
    * @return The generated test
    */
   public static Test suite() {
      TestSuite suite = new TestSuite("Test for package at.einspiel.simme.server.menu");
      //$JUnit-BEGIN$
      suite.addTestSuite(TextMenuTest.class);
      suite.addTestSuite(ListMenuTest.class);
      suite.addTestSuite(GenerateMenuTest.class);
      suite.addTestSuite(SpecialMenuTest.class);
      suite.addTestSuite(MenuManagerTest.class);
      //$JUnit-END$
      return suite;
   }
}
