// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SpecialMenuTest.java
//                  $Date: 2004/09/13 15:10:28 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;


/**
 * Class to test special menu.
 * @author kariem
 */
public class SpecialMenuTest extends AbstractMenuTestCase {

   private static final String TEST_FILE_NAME = TextMenuTest.class.getResource(
   "test-specialmenu.xml").toString();

   /**
    * Creates a new instance of <code>SpecialMenuTest</code>.
    * @throws Exception if an error occurred while loading the test xml file.
    */
   public SpecialMenuTest() throws Exception {
      super(TEST_FILE_NAME, SpecialMenu.TAG_NAME);
      // TODO implement test
      System.err.println("TODO this test has to be implemented");
   }

   /** @see at.einspiel.simme.server.menu.AbstractMenuTestCase#createMenu(org.w3c.dom.Element) */
   protected IMenu createMenu(Element e) {
      return new SpecialMenu(e);
   }


}
