// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractMenu.java
//                  $Date: 2004/06/07 09:26:45 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

import at.einspiel.base.User;

/**
 * <p>Abstract implementation of {@link at.einspiel.simme.server.menu.IMenu}.
 * This class provides a simple constructor to set <i>title</i> and <i>id</i>
 * either directly or through an <code>Element</code>.</p>
 * 
 * <p>Additionally it provides a method for subclasses to write the first part
 * of their xml representation.</p>
 * 
 * <p>In order to produce a cloneable object, subclass
 * {@linkplain AbstractUserMenu}, as this class only returns a reference to the
 * current object instead of a clone.</p>
 * 
 * @see at.einspiel.simme.server.menu.AbstractUserMenu
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
        return new String[]{DEFAULT_ID};
    }
    
    /**
     * Menus are by default not user specific. This class returns the default
     * representation, even if a user is supplied as parameter.
     * 
     * @see at.einspiel.simme.server.menu.IMenu#getXml(at.einspiel.base.User)
     */
    public String getXml(User u) {
        return getXml();
    }
    
    /** 
     * As menus are by default not user specific, this method does not do
     * anything. Implementation has to be done in subclasses.
     * 
     * @see at.einspiel.simme.server.menu.IMenu#setUser(at.einspiel.base.User)
     */
    public void setUser(User u) {
        // does not do anything.
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

    /**
     * The clone method is overridden in order to support for user specific
     * menus. By default cloning returns the same instance (<code>this</code>).
     * 
     * @return the same instance. 
     * @see java.lang.Object#clone()
     */
    protected Object clone() throws CloneNotSupportedException {
        return this;
    }
    
    /**
     * Returns a clone of this object.
     * 
     * @see at.einspiel.simme.server.menu.IMenu#copy()
     */
    public IMenu copy() {
        try {
            return (IMenu) clone();
        } catch (CloneNotSupportedException e) {
            return this;
            // throw new RuntimeException(e);
        }
    }
}