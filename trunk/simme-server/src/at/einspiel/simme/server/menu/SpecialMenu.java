// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SpecialMenu.java
//                  $Date: 2004/02/21 23:04:20 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

/**
 * Is used to deliver reserved menu information. That includes means to start
 * a game, or set a user into waiting state.
 * 
 * @author kariem
 */
class SpecialMenu extends AbstractMenu implements IMenu {

   static final String TAG_NAME = "special";
   
   static final String SPECIAL_WAIT = "wait";
   static final String SPECIAL_START = "start";
   static final String SPECIAL_READY = "ready";

   /**
    * Creates a new instance of <code>SpecialMenu</code>.
    * @param e
    */
   SpecialMenu(Element e) {
      super(e);
      // TODO Auto-generated constructor stub (kariem)
   }

   /** @see at.einspiel.simme.server.menu.IMenu#getXml() */
   public String getXml() {
      // TODO Auto-generated method stub (kariem)
      return null;
   }
}
