// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractMenu.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

import at.einspiel.base.User;
import at.einspiel.messaging.ISimpleInfo;
import at.einspiel.simme.nanoxml.XMLElement;

/**
 * <p>
 * Abstract implementation of {@link at.einspiel.simme.server.menu.IMenu}. This
 * class provides a simple constructor to set <i>title </i> and <i>id </i>
 * either directly or through an <code>Element</code>.
 * </p>
 * <p>
 * Additionally it provides a method for subclasses to write the first part of
 * their xml representation: {@link #createXMLStart(String, boolean)}.
 * </p>
 * <p>
 * In order to produce a cloneable object, subclass
 * {@linkplain AbstractUserMenu}, as this class only returns a reference to the
 * current object instead of a clone.
 * </p>
 * 
 * @see at.einspiel.simme.server.menu.AbstractUserMenu
 * 
 * @author kariem
 */
abstract class AbstractMenu implements IMenu {

	String title;
	String id;

	/**
	 * Creates a new instance of <code>AbstractMenu</code>.
	 * @param title
	 *            the title.
	 * @param id
	 *            the id.
	 */
	AbstractMenu(String title, String id) {
		this.title = title;
		this.id = id;
	}

	/**
	 * Creates a new instance of <code>AbstractMenu</code> from the
	 * information found in an element.
	 * @param e
	 *            the xml element.
	 */
	AbstractMenu(Element e) {
		this(e.getAttribute(ISimpleInfo.ATTR_TITLE), e.getAttribute(ISimpleInfo.ATTR_ID));
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
	 * Menus do not answer to any meta information by default. This
	 * implementation returns the default implementation, even if a value is
	 * supplied as parameter.
	 * 
	 * @see at.einspiel.simme.server.menu.IMenu#getXml(java.lang.String)
	 */
	public String getXml(String meta) {
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

	/**
	 * Creates the start of an xml representation. To receive a well-formed xml
	 * string, either <code>/&gt;</code> or <code>&lt;/$elementName&gt;</code>
	 * must be appended to this method's return value.
	 * @param elementName
	 *            the tag name of the returned xml snippet.
	 * @param list
	 *            whether this string is used to show a list.
	 * @return the start of the xml representation for this object. This will
	 *         look like
	 *         <code>&lt;$elementName title="$title" id="$id" [list="true"]</code>.
	 *         Note that the opening tag is not closed by <code>&gt;</code>.
	 *         The attribute <i>list </i> will only be added to the string if
	 *         the parameter <code>list</code> is <code>true</code>.
	 */
	protected String createXMLStart(String elementName, boolean list) {
		return createXmlStart(title, id, elementName, list);
	}

	static String createXmlStart(String title, String id, String elementName, boolean list) {
		StringBuffer xml = new StringBuffer("<");
		xml.append(elementName);

		addAttrToBuf(ISimpleInfo.ATTR_TITLE, title, xml);
		addAttrToBuf(ISimpleInfo.ATTR_ID, id, xml);
		// obey to client's limitation and use XMLElement implementation instead
		// of Boolean.toString(bool).
		if (list) {
			addAttrToBuf(ISimpleInfo.ATTR_LIST, XMLElement.TRUE, xml);
		}

		return xml.toString();
	}

	/**
	 * Adds an attribute to the given string buffer object. Effectively this
	 * method appends a string in the following format to the buffer, prepended
	 * by a single space (" "):
	 * 
	 * <code>$paramName="$paramValue"</code>
	 * 
	 * @param paramName
	 *            the attribute's name.
	 * @param paramValue
	 *            the attribute's value.
	 * @param buf
	 *            the buffer.
	 */
	protected static void addAttrToBuf(String paramName, String paramValue, StringBuffer buf) {
		// single space
		buf.append(" ");
		// name="value"
		buf.append(paramName);
		buf.append("=\"");
		buf.append(paramValue);
		buf.append("\"");
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
	 * Returns a clone of this object. This implementation does not return a
	 * clone, but instead the same object.
	 * 
	 * @see at.einspiel.simme.server.menu.IMenu#copy()
	 */
	public IMenu copy() {
		try {
			return (IMenu) clone();
		} catch (CloneNotSupportedException e) {
			return this;
		}
	}
}