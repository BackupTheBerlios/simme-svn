// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SpecialMenu.java
//                  $Date: 2004/06/07 09:26:45 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

/**
 * Is used to deliver specialited menu information. That includes means to start
 * a game, or set a user into waiting state.
 * 
 * @author kariem
 */
class SpecialMenu extends AbstractUserMenu implements IMenu {

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