// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractMenuTestCase.java
//                  $Date: 2004/04/03 23:39:13 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;

import at.einspiel.util.AbstractXMLTestCase;
import at.einspiel.util.XMLUtils;

/**
 * Abstract menu test case to be subclassed by tests for menus.
 * @author kariem
 */
public abstract class AbstractMenuTestCase extends AbstractXMLTestCase {

   private final Element[] savedMenuElements;
   private final Element[] savedResultElements;

   Element[] elementsMenu;
   Element[] elementsResult;

   AbstractMenuTestCase(String filename, String tagname) throws Exception {
      if (tagname == null) {
         tagname = "*";
      }
      Element root = parse(filename).getDocumentElement();
      List listMenus = new ArrayList();
      List listResults = new ArrayList();
      List children = XMLUtils.getDirectChildren(root);
      for (Iterator i = children.iterator(); i.hasNext(); ) {
         Element e = (Element) i.next();
         if (e.getNodeName().equals("menus")) {
            addElements(((Element) e.getChildNodes())
                  .getElementsByTagName(tagname), listMenus);
         } else if (e.getNodeName().equals("results")) {
            addElements(((Element) e.getChildNodes())
                  .getElementsByTagName(tagname), listResults);
         }
      }

      // listMenus and listResults have all elements
      savedMenuElements = (Element[]) listMenus.toArray(new Element[listMenus
            .size()]);
      savedResultElements = (Element[]) listResults
            .toArray(new Element[listResults.size()]);
   }
   
   AbstractMenuTestCase(String filename) throws Exception {
      this(filename, null);
   }

   /**
    * Tests the constructor.
    * @throws Exception error while doing xml equality check.
    */
   public void testConstructor() throws Exception {
      // test basics: title, id
      for (int i = 0; i < elementsMenu.length; i++) {
         Element current = elementsMenu[i];
         IMenu menu = createMenu(current);
         assertTrue(menu.getTitle() == current.getAttribute("title"));
         assertTrue(menu.getId() == current.getAttribute("id"));
      }

      // compare generated xml from menu to expected results
      for (int i = 0; i < elementsMenu.length; i++) {
         Element current = elementsMenu[i];
         IMenu menu = createMenu(current);
         Element result = elementsResult[i];
         assertXMLEqual(result, menu.getXml(), System.out);
      }
   }
   
   /**
    * Creates the subclass-specific menu from an element.
    * @param e the element.
    * @return the menu.
    */
   protected abstract IMenu createMenu(Element e);

   /** @see junit.framework.TestCase#setUp() */
   protected void setUp() throws Exception {
      elementsMenu = (Element[]) savedMenuElements.clone();
      elementsResult = (Element[]) savedResultElements.clone();
   }
}
