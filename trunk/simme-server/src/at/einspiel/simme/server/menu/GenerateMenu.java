// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: GenerateMenu.java
//                  $Date: 2004/02/21 23:04:20 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

/**
 * A menu that generates its contents.
 * @author kariem
 */
class GenerateMenu extends AbstractMenu implements IMenu {

   static final String TAG_NAME = "generate";

   /**
    * Creates a new instance of <code>GenerateMenu</code>.
    * @param e
    */
   GenerateMenu(Element e) {
      super(e);
      // TODO Auto-generated constructor stub (kariem)
   }

   /** @see at.einspiel.simme.server.menu.IMenu#getXml() */
   public String getXml() {
      // TODO Auto-generated method stub (kariem)
      return null;
   }

}
