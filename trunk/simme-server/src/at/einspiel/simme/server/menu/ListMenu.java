// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ListMenu.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.einspiel.messaging.ParsedObject;


class ListMenu extends AbstractMenu{
   List elements;
   String xml;
   
   ListMenu(ParsedObject po) {
      super(po);
      
      // parse "child" elements
      List children = po.getChildren();
      elements = new ArrayList(children.size());
      for (Iterator i = children.iterator(); i.hasNext(); ) {
         ParsedObject child = (ParsedObject) i.next();
         if (child.getName().equals("child")) {
            elements.add(new MenuItem(child));
         }
      }
      
      StringBuffer xmlBuf = new StringBuffer(createXMLStart("menu", true));
      xmlBuf.append(">");
      for (Iterator i = elements.iterator(); i.hasNext(); ) {
         xmlBuf.append(((MenuItem) i.next()).getXml());
      }
      xmlBuf.append("</menu>");
      
      xml = xmlBuf.toString();
   }

   /**@see at.einspiel.simme.server.menu.IMenu#getXml() */
   public String getXml() {
      return xml;
   }
   
   class MenuItem {
      String name;
      byte itemId;
      MenuItem(ParsedObject po) {
         name = po.getAttribute("name");
         itemId = Byte.parseByte(po.getId());
      }
      String getXml() {
         return "<child name=\"" + name + "\"/>";
      }
   }
   
}