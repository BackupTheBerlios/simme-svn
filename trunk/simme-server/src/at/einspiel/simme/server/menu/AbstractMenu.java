// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractMenu.java
//                  $Date: 2004/02/21 23:03:13 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

/**
 * <p>Abstract implementation of {@link at.einspiel.simme.server.menu.IMenu}.
 * This class provides a simple constructor to set <i>title</i> and <i>id</i>
 * either directly or through an <code>Element</code>.</p>
 * 
 * <p>Additionally it provides a method for subclasses to write the first part
 * of their xml representation</p>
 * 
 * @author kariem
 */
abstract class AbstractMenu implements IMenu {
   private static final String ATTR_ID = "id";
   private static final String ATTR_TITLE = "title";
   String title;
   String id;

   /**
    * Creates a new instance of <code>AbstractMenu</code>.
    * @param title the title.
    * @param id the id.
    */
   AbstractMenu(String title, String id) {
      this.title = title;
      this.id = id;
   }

   /**
    * Creates a new instance of <code>AbstractMenu</code> from the information
    * found in an element.
    * @param e the xml element.
    */
   AbstractMenu(Element e) {
      this(e.getAttribute(ATTR_TITLE), e.getAttribute(ATTR_ID));
   }

   /** @see at.einspiel.simme.server.menu.IMenu#getId() */
   public String getId() {
      return id;
   }

   /** @see at.einspiel.simme.server.menu.IMenu#getTitle() */
   public String getTitle() {
      return title;
   }
   
   /** @see at.einspiel.simme.server.menu.IMenu#getIdFor(java.lang.String) */
   public String getIdFor(String selection) {
      return DEFAULT_ID;
   }
   
   /** @see at.einspiel.simme.server.menu.IMenu#getOptions() */
   public String[] getOptions() {
      return new String[] { DEFAULT_ID };
   }

   String createXMLStart(String elementName, boolean list) {
      StringBuffer xml = new StringBuffer("<");
      xml.append(elementName);
      xml.append(" ");
      // title attribute
      xml.append(ATTR_TITLE);
      xml.append("=\"");
      xml.append(title);
      xml.append("\" ");
      // id attribute
      xml.append(ATTR_ID);
      xml.append("=\"");
      xml.append(id);
      xml.append("\" list=\"");
      xml.append(Boolean.toString(list));
      xml.append("\"");
      return xml.toString();
   }
}