// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractUserMenu.java
//                  $Date: 2004/06/07 09:26:45 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

import at.einspiel.base.User;

/**
 * This class is used to create menus, that can be configured for specific
 * users. A call to the method {@linkplain #clone()}, will result in a real
 * clone.
 * 
 * @author kariem
 */
public abstract class AbstractUserMenu extends AbstractMenu {

    User u;
    
    /** @see AbstractMenu#AbstractMenu(String, String) */
    public AbstractUserMenu(String title, String id) {
        super(title, id);
    }

    /** @see AbstractMenu#AbstractMenu(Element) */
    public AbstractUserMenu(Element e) {
        super(e);
    }
    
    /** @see at.einspiel.simme.server.menu.AbstractMenu#setUser(at.einspiel.base.User) */
    public void setUser(User u) {
        this.u = u;
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
