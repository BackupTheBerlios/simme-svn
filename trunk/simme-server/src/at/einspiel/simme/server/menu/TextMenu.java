// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: TextMenu.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.messaging.ParsedObject;


class TextMenu extends AbstractMenu {

   String message;
   String xml;
   
   TextMenu(ParsedObject po) {
      super(po);
      this.message = po.getAttribute("msg");
      
      // build xml representation
      StringBuffer xmlBuf = new StringBuffer(createXMLStart("text", false));
      xmlBuf.append(" msg=\"");
      xmlBuf.append(message);
      xmlBuf.append("\"/>");   
      xml = xmlBuf.toString();
   }

   /** @see at.einspiel.simme.server.menu.IMenu#getXml() */
   public String getXml() {
      return xml;
   }
}