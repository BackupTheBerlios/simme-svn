// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractUserMenu.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

import at.einspiel.simme.server.ManagedUser;

/**
 * This class is used to create menus, that can be configured for specific
 * users. A call to the method {@linkplain #clone()}, will result in a real
 * clone.
 * 
 * @author kariem
 */
abstract class AbstractUserMenu extends AbstractMenu {

    ManagedUser mu;
    
    /** @see AbstractMenu#AbstractMenu(String, String) */
    public AbstractUserMenu(String title, String id) {
        super(title, id);
    }

    /** @see AbstractMenu#AbstractMenu(Element) */
    public AbstractUserMenu(Element e) {
        super(e);
    }
    
    /** @see at.einspiel.simme.server.menu.AbstractMenu#setUser(ManagedUser) */
    public void setUser(ManagedUser mu) {
        this.mu = mu;
    }
    
    /**
     * Returns a real clone (new object) of this menu.
     * 
     * @see at.einspiel.simme.server.menu.AbstractMenu#clone()
     */
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
