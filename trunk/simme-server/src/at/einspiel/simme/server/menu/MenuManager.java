// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: MenuManager.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.net.URL;
import java.util.*;

import at.einspiel.messaging.ParsedObject;

/**
 * Manages the menu, and access to this menu.
 * @author kariem
 */
public class MenuManager {

   private static Map menuManagers = new HashMap(4);
   
   
   private Map menus;
   
   /**
    * Creates a new menu manager for the given URL.
    * @param url the url pointing to an xml file.
    * @return the corresponding menu manager.
    */
   public static MenuManager getMenuManager(URL url) {
      MenuManager mgr = (MenuManager) menuManagers.get(url);
      if (mgr == null) {
         mgr = new MenuManager(url);
         menuManagers.put(url, mgr);
      }
      return mgr;
   }

   
   private MenuManager(URL menu) {
      menus = new HashMap();
      try {
         ParsedObject po = ParsedObject.loadFromURI(menu.toString());
         parse(po);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void parse(ParsedObject po) {
      List children = po.getChildren();
      for (Iterator i = children.iterator(); i.hasNext(); ) {
         final ParsedObject poMenu = (ParsedObject) i.next();
         final IMenu menu;
         final String name = poMenu.getName();
         if (name.equals("menu")) {
            menu = new ListMenu(poMenu);
         } else if (name.equals("text")) {
            menu = new TextMenu(poMenu);
         } else if (name.equals("generate")) {
            menu = null; // TODO implement GeneratedMenu
         } else { // if name.equals special
            menu = null; // TODO implement SpecialMenu
         }
         menus.put(menu.getId(), menu);
      }
   }
}
