// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ListMenu.java
//                  $Date: 2004/02/21 23:03:13 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A simple list showing static entries.
 * @author kariem
 */
class ListMenu extends AbstractMenu implements IMenu {
   
   static final String TAG_NAME = "list";

   List elements;
   String xml;

   /**
    * Creates a new instance of <code>ListMenu</code>.
    * @param e the element.
    */
   ListMenu(Element e) {
      super(e);

      // parse "child" elements
      NodeList children = e.getElementsByTagName("child");
      elements = new ArrayList(children.getLength());
      for (int i = 0; i < children.getLength(); i++) {
         elements.add(new MenuItem((Element) children.item(i)));

      }

      StringBuffer xmlBuf = new StringBuffer(createXMLStart(TAG_NAME, true));
      xmlBuf.append(">");
      for (Iterator i = elements.iterator(); i.hasNext(); ) {
         ((MenuItem) i.next()).appendXmlTo(xmlBuf);
      }
      xmlBuf.append("</");
      xmlBuf.append(TAG_NAME);
      xmlBuf.append(">");

      xml = xmlBuf.toString();
   }

   /**@see at.einspiel.simme.server.menu.IMenu#getXml() */
   public String getXml() {
      return xml;
   }

   /** @see at.einspiel.simme.server.menu.IMenu#getIdFor(java.lang.String) */
   public String getIdFor(String selection) {
      // search through all menu items and find corresponding id
      for (Iterator i = elements.iterator(); i.hasNext();) {
         MenuItem item = (MenuItem) i.next();
         if (item.itemId.equals(selection)) {
            // the correct id is the same as the selection for this IMenu
            return selection;
         }
      }
      
      // if nothing is found, return default implementation
      return super.getIdFor(selection);
   }
   
   /** @see at.einspiel.simme.server.menu.IMenu#getOptions() */
   public String[] getOptions() {
      String[] options = new String[elements.size()];
      // add all possible item ids
      int pos = 0;
      for (Iterator i = elements.iterator(); i.hasNext();) {
         MenuItem item = (MenuItem) i.next();
         options[pos++] = item.itemId;
      }
      
      return options;
   }
   
   
   class MenuItem {
      String name;
      String itemId;

      MenuItem(Element e) {
         name = e.getAttribute("name");
         itemId = e.getAttribute("id");
      }

      /**
       * Appends xml information to the given buffer.
       * @param buf the buffer to append the information to.
       */
      void appendXmlTo(StringBuffer buf) {
         buf.append("<child name=\"");
         buf.append(name);
         buf.append("\"/>");
      }
   }

}