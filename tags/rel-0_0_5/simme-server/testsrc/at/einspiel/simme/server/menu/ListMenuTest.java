// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ListMenuTest.java
//                  $Date: 2004/02/21 23:04:20 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

/**
 * Class to test list menu.
 * @author kariem
 */
public class ListMenuTest extends AbstractMenuTestCase {

   private static final String TEST_FILE_NAME = TextMenuTest.class.getResource(
         "test-listmenu.xml").toString();

   /**
    * Creates a new instance of <code>ListMenuTest</code>.
    * @throws Exception if an error occurred while loading the test xml file.
    */
   public ListMenuTest() throws Exception {
      super(TEST_FILE_NAME, ListMenu.TAG_NAME);
   }

   /** @see AbstractMenuTestCase#createMenu(Element) */
   protected IMenu createMenu(Element e) {
      return new ListMenu(e);
   }
}
