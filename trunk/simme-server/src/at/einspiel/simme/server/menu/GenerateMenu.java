// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: GenerateMenu.java
//                  $Date: 2004/06/07 09:26:45 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

/**
 * A menu that generates its contents from the database.
 * @author kariem
 */
class GenerateMenu extends AbstractMenu implements IMenu {

   static final String TAG_NAME = "generate";
   
   static final String USERS_ALL = "all";
   static final String USERS_WAITING = "waiting";
   static final String USERS_PLAYING = "online";
   

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
