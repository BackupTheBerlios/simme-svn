// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractMenu.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.messaging.ParsedObject;


abstract class AbstractMenu implements IMenu {
   String title;
   String id;
   
   AbstractMenu(String title, String id) {
      this.title = title;
      this.id = id;
   }
   
   AbstractMenu(ParsedObject po) {
      this(po.getAttribute("title"), po.getId());
   }
   
   /** @see at.einspiel.simme.server.menu.IMenu#getId() */
   public String getId() {
      return id;
   }
   
   /** @see at.einspiel.simme.server.menu.IMenu#getTitle() */
   public String getTitle() {
      return title;
   }
   
   String createXMLStart(String elementName, boolean list) {
      StringBuffer xml = new StringBuffer("<");
      xml.append(elementName);
      xml.append(" title=\"");
      xml.append(title);
      xml.append("\" id=\"");
      xml.append(id);
      xml.append("\" list=\"");
      xml.append(Boolean.toString(list));
      xml.append("\"");
      return xml.toString();
   }
}