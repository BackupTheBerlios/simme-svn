// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: MenuManagerTest.java
//                  $Date: 2004/02/23 10:55:39 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

/**
 * Class to test menu manager.
 * @author kariem
 */
public class MenuManagerTest extends TestCase {

    /**
     * Tests the static method to build a menu manager. It should throw an
     * exception, if the XML file could not be found or could not be parsed, or
     * does not contain any menu data.
     */
    public final void testGetMenuManagerSimple() {
        // test with empty URL
        URL url;
        try {
            url = new URL("http://127.0.0.1/null.xml");
        } catch (MalformedURLException e) {
            throw new AssertionError("Should not be thrown");
        }

        Exception shouldBeThrown = null;
        MenuManager mm = null;
        try {
            mm = MenuManager.getMenuManager(url);
        } catch (MenuCreateException e1) {
            shouldBeThrown = e1;
        }
        assertNotNull(shouldBeThrown);
        assertNull(mm);


        // test with invalid (unparseable) data
        url = MenuManagerTest.class.getResource("test-menumanager-incorrect.xml");
        shouldBeThrown = null;
        mm = null;
        try {
            mm = MenuManager.getMenuManager(url);
        } catch (MenuCreateException e1) {
            shouldBeThrown = e1;
        }
        assertNotNull(shouldBeThrown);
        assertNull(mm);

        // test with no data (empty document)
        url = MenuManagerTest.class.getResource("test-menumanager-incorrect2.xml");
        shouldBeThrown = null;
        mm = null;
        try {
            mm = MenuManager.getMenuManager(url);
        } catch (MenuCreateException e1) {
            shouldBeThrown = e1;
        }
        assertNotNull(shouldBeThrown);
        assertNull(mm);

        // test with some text messages
        url = MenuManagerTest.class.getResource("test-menumanager-textmessages.xml");
        mm = null;
        try {
            mm = MenuManager.getMenuManager(url);
        } catch (MenuCreateException e1) {
            throw new AssertionError("Should not be thrown");
        }
        assertNotNull(mm);
    }

    /**
     * Tests simple navigation.
     */
    public final void testSimpleMenuNavigation() {
       URL xmlUrl = MenuManagerTest.class.getResource("test-menumanager-simplenavigation.xml");
       MenuManager mm = null;
       try {
         mm = MenuManager.getMenuManager(xmlUrl);
      } catch (MenuCreateException e) {
         throw new AssertionError("Should not be thrown");
      }

      // get the "default" menu
      IMenu menu = mm.getMenu();
      assertEquals("The default menu should have \"0\" as id", menu.getId(), IMenu.DEFAULT_ID);

      IMenu newMenu = menu;
      for (int i = 0; i < 20; i++) {
         // try random selections

         // retrieve a random element from the menu's options
         String selection = getRandomElement(newMenu.getOptions());
         // select the new menu
         newMenu = mm.getMenu(menu.getIdFor(selection));
         //System.out.println(newMenu.getTitle() + "\t-\t" + newMenu.getId());
      }
    }

   /**
    * Returns a random element from a string array.
    * @param strings the string array.
    * @return a random element.
    */
   private String getRandomElement(String[] strings) {
      if (strings.length == 1) {
         return strings[0];
      }
      int pos = (int) (Math.random() * strings.length);
      return strings[pos];
   }
}