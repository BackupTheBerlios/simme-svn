// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: TextMenuTest.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

import at.einspiel.messaging.ISimpleInfo;

/**
 * Class to test text menu.
 * @author kariem
 */
public class TextMenuTest extends AbstractMenuTestCase {

   private static final String TEST_FILE_NAME = TextMenuTest.class.getResource(
      "test-textmenu.xml").toString();

   /**
    * Creates a new instance of <code>TextMenuTest</code>.
    * @throws Exception if an error occurred while loading the test xml file.
    */
   public TextMenuTest() throws Exception {
      super(TEST_FILE_NAME, ISimpleInfo.TAG_TEXT);
   }

   /** @see AbstractMenuTestCase#createMenu(Element) */
   protected IMenu createMenu(Element e) {
      return new TextMenu(e);
   }

   /** Tests the "msg" element. */
   public void testMsgElement() {
      // test third element
      Element current = elementsMenu[2];
      IMenu menu = new TextMenu(current);
      // only double newline characters should separate a paragraph.
      String[] paragraphs = menu.getXml().split("\n\n");
      for (int i = 0; i < paragraphs.length; i++) {
         assertTrue(paragraphs[i].indexOf("  ") == -1); // no double spaces
         assertTrue(paragraphs[i].indexOf('\r') == -1); // no line feeds
         assertTrue(paragraphs[i].indexOf('\t') == -1); // no tabs
         assertTrue(paragraphs[i].indexOf('\n') == -1); // no newlines
      }
   }
}