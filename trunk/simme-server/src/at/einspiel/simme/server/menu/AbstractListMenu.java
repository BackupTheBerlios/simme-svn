// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractListMenu.java
//                  $Date: 2004/08/25 15:43:11 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import org.w3c.dom.Element;

import at.einspiel.util.XMLUtils;

/**
 * Class that allows for simple creation of menus with several children.
 * 
 * @author kariem
 */
abstract class AbstractListMenu extends AbstractMenu implements IMenu {

	/** @see AbstractMenu#AbstractMenu(Element) */
	public AbstractListMenu(Element e) {
		super(e);
	}

	/** @see AbstractMenu#AbstractMenu(String, String) */
	public AbstractListMenu(String title, String id) {
		super(title, id);
	}

	/**
	 * Single item of a list menu.
	 */
	protected static class MenuItem {
		String name;
		String itemId;

		
		MenuItem(String name, String itemId) {
			this.name = name;
			this.itemId = itemId;
		}

		MenuItem(Element e) {
			this(XMLUtils.getText(e), e.getAttribute("id"));
		}

		MenuItem(String name) {
			this(name, null);
		}
		
		/**
		 * Appends the menu item with the name as content to the given buffer.
		 * @param buf the buffer to append the information to.
		 * @param childName the name of the child.
		 */
		static void appendXmlTo(StringBuffer buf, String childName) {
			buf.append("<child>");
			buf.append(childName);
			buf.append("</child>");
		}
		
		/**
		 * Appends xml information to the given buffer.
		 * @param buf
		 *            the buffer to append the information to.
		 */
		void appendXmlTo(StringBuffer buf) {
			appendXmlTo(buf, this.name);
		}
	}
}