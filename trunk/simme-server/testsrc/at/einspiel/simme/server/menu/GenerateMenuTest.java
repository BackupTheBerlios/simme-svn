// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: GenerateMenuTest.java
//                  $Date: 2004/02/21 23:04:20 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

/**
 * Class to test generate menu.
 * @author kariem
 */
public class GenerateMenuTest extends AbstractMenuTestCase {

   private static final String TEST_FILE_NAME = TextMenuTest.class.getResource(
         "test-generatemenu.xml").toString();

   /**
    * Creates a new instance of <code>ListMenuTest</code>.
    * @throws Exception if an error occurred while loading the test xml file.
    */
   public GenerateMenuTest() throws Exception {
      super(TEST_FILE_NAME, GenerateMenu.TAG_NAME);
      // TODO implement test
      System.err.println("TODO this test has to be implemented");
   }

   /** @see at.einspiel.simme.server.menu.AbstractMenuTestCase#createMenu(org.w3c.dom.Element) */
   protected IMenu createMenu(Element e) {
      return new GenerateMenu(e);
   }
}