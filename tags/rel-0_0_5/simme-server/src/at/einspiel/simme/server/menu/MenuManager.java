// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: MenuManager.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.9 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import at.einspiel.simme.server.ManagedUser;
import at.einspiel.util.XMLUtils;

/**
 * Manages the menu, and access to this menu. Here is an example for the XML
 * file.
 * 
 * <pre>
 * &lt;menus [default="default-id"]&gt;
 *   &lt;list title="List Title" id="list-id" list="true"&gt;
 *      &lt;child id="entry1-id"&gt;Entry 1&lt;/child&gt;
 *      &lt;child id="entry2-id"&gt;Entry 2&lt;/child&gt;
 *   &lt;/list&gt;
 *
 *   &lt;text title="Entry 1 Title" id="entry1-id" msg="Entry 1 message"/&gt;
 *
 *   &lt;text title="Entry 2 Title" id="entry2-id"&gt;
 *      &lt;msg&gt;
 *        Entry 2 message
 *      &lt;/msg&gt;
 *   &lt;/text&gt;
 * 
 *   ...
 * &lt;/menus&gt;
 * </pre>
 * 
 * The attribute <code>default</code> is optional. It declares the default
 * menu, i.e. the first menu to be shown, for the <code>MenuManager</code>.
 * If omitted, {@linkplain IMenu#DEFAULT_ID}is taken.
 * 
 * @author kariem
 */
public class MenuManager {

	private static final String ATTR_DEFAULT_MENU = "default";

	private static Map menuManagers = new HashMap(4);
	private static DocumentBuilder builder;

	static {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private final Map menus;
	private String defaultMenu;

	private URL menuURL;

	/**
	 * Creates a new instance of <code>MenuManager</code> from a given url
	 * pointing to an XML document.
	 * @param menuURL
	 *            the url of an xml document.
	 * @throws MenuCreateException
	 *             if the file could not be found, parsed or contained no menu
	 *             information.
	 */
	private MenuManager(URL menuURL) throws MenuCreateException {
		this.menuURL = menuURL;
		menus = new HashMap();
		parse();
		if (menus.isEmpty()) {
			throw new MenuCreateException("The url " + menuURL + " does not "
					+ "contain menu information.");
		}
	}

	/**
	 * Reloads the menu manager from its url.
	 * @throws MenuCreateException
	 *             if the file could not be found, parsed or contained no menu
	 *             information.
	 */
	public void reload() throws MenuCreateException {
		menus.clear();
		parse();
		if (menus.isEmpty()) {
			throw new MenuCreateException("The url " + menuURL + " does not "
					+ "contain menu information.");
		}
	}

	/**
	 * Parses the current url and loads the menus.
	 * @throws MenuCreateException
	 *             if the file could not be found, or parsed.
	 */
	private void parse() throws MenuCreateException {
		synchronized (builder) {
			try {
				Document document = builder.parse(menuURL.toString());
				Element root = document.getDocumentElement();
				// set the default menu
				String defMenu = root.getAttribute(ATTR_DEFAULT_MENU);
				if (defMenu.equals("")) {
					defMenu = IMenu.DEFAULT_ID;
				}
				this.defaultMenu = defMenu;
				// parse the rest
				parse(root);
			} catch (SAXException e1) {
				throw new MenuCreateException(e1);
			} catch (IOException e1) {
				throw new MenuCreateException(e1);
			}
		}
	}

	/**
	 * Creates a new menu manager for the given URL.
	 * @param url
	 *            the url pointing to an xml file.
	 * @return the corresponding menu manager.
	 * @throws MenuCreateException
	 *             if the MenuManager could not be created, or does not contain
	 *             any menu data.
	 */
	public static MenuManager getMenuManager(URL url) throws MenuCreateException {
		MenuManager mgr = (MenuManager) menuManagers.get(url);
		if (mgr == null) {
			mgr = new MenuManager(url);
			menuManagers.put(url, mgr);
		}
		return mgr;
	}

	private void parse(Element element) {
		List children = XMLUtils.getDirectChildren(element);
		for (Iterator i = children.iterator(); i.hasNext();) {
			final Element poMenu = (Element) i.next();
			final IMenu menu;
			final String name = poMenu.getNodeName();
			if (name.equals("list")) {
				menu = new ListMenu(poMenu);
			} else if (name.equals("text")) {
				menu = new TextMenu(poMenu);
			} else if (name.equals("generate")) {
				menu = new GenerateMenu(poMenu);
			} else { // if name equals special
				menu = new SpecialMenu(poMenu);
			}
			menus.put(menu.getId(), menu);
		}
	}

	/**
	 * Returns the default menu.
	 * @return the default menu for this manager.
	 */
	public IMenu getMenu() {
		return getMenu(null);
	}

	/**
	 * Returns the menu identified by <code>id</code>.
	 * @param id
	 *            the id for the menu. If <code>id</code> is <code>null</code>,
	 *            then the default menu will be returned. The default menu is
	 *            equal to {@linkplain IMenu#DEFAULT_ID}, unless otherwise
	 *            specified.
	 * 
	 * @return the menu associated with <code>id</code>, or <code>null</code>,
	 *         if no such menu exists.
	 * 
	 * @see #getMenu(String, ManagedUser)
	 */
	public IMenu getMenu(String id) {
		if (id == null) {
			id = defaultMenu;
		}
		return (IMenu) menus.get(id);
	}

	/**
	 * Returns the menu identified by <code>id</code> for the specified user.
	 * 
	 * @param id
	 *            the id for the menu. If <code>id</code> is <code>null</code>,
	 *            then the default menu will be returned. The default menu is
	 *            equal to {@linkplain IMenu#DEFAULT_ID}, unless otherwise
	 *            specified.
	 * @param u
	 *            the user. If this parameter is <code>null</code>, the
	 *            method's result is the same as a call to
	 *            {@linkplain #getMenu(String)}.
	 * 
	 * @return the menu associated with <code>id</code>, or <code>null</code>,
	 *         if no such menu exists.
	 * 
	 * @see #getMenu(String)
	 */
	public IMenu getMenu(String id, ManagedUser u) {
		IMenu currentMenu = getMenu(id);
		if (u != null && currentMenu != null) {
			// clone menu and set specific user
			IMenu tmpMenu = currentMenu.copy();
			tmpMenu.setUser(u);
			return tmpMenu;
		}
		return currentMenu;
	}

	/**
	 * Returns the menu that results on a selection of the menu identified by
	 * the id <code>currentMenu</code> at the <code>selectedPosition</code>
	 * selected by user <code>u</code>.
	 * 
	 * @param menuId
	 *            the id of the current menu.
	 * @param selectedPosition
	 *            the selected position within this menu.
	 * @param u
	 *            the user that selected the menu at
	 *            <code>selectedPosition</code>.
	 * @return a corresponding menu.
	 */
	public IMenu getMenu(String menuId, int selectedPosition, ManagedUser u) {
		IMenu menu = getMenu(menuId);
		String[] options = menu.getOptions();
		if (selectedPosition >= options.length) {
			selectedPosition = 0;
			// TODO think about error loggin
			// System.err.println("menu: selected position out of bounds
			// [current=" + menuId + ",selected=" + selectedPosition + "]");
		}
		String selection = menu.getOptions()[selectedPosition];
		return getMenu(menu.getIdFor(selection), u);
	}

	/**
	 * Returns the menu that results on a selection of the menu identified by
	 * the id <code>currentMenu</code> at the <code>selectedPosition</code>.
	 * @param currentMenu
	 *            the id of the current menu.
	 * @param selectedPosition
	 *            the selected position within this menu.
	 * @return a corresponding menu.
	 */
	public IMenu getMenu(byte currentMenu, int selectedPosition) {
		return getMenu(Byte.toString(currentMenu), selectedPosition, null);
	}
}