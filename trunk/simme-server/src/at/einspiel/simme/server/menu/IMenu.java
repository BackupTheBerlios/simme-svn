// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IMenu.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.base.User;

/**
 * Used to model a server-side menu. The contents of menus are sent to the
 * client, via the methods {@link #getXml()} and {@link #getXml(String)}.
 * 
 * @author kariem
 */
public interface IMenu extends Cloneable {

	/** The default id for menus. The current value is <code>0</code>. */
	String DEFAULT_ID = "0";

	/**
	 * The type attribute, which may be used for different instances of the same
	 * class.
	 * @see GenerateMenu
	 * @see SpecialMenu
	 */
	String ATTR_TYPE = "type";

	/**
	 * Returns the title.
	 * 
	 * @return the title.
	 */
	String getTitle();

	/**
	 * Returns the id.
	 * 
	 * @return the id.
	 */
	String getId();

	/**
	 * Returns an xml representation. This representation is sent to the client.
	 * 
	 * @return an xml representation.
	 * @see #getXml(User)
	 */
	String getXml();

	/**
	 * Returns an xml representation with the additional meta parameter.
	 * @param meta
	 *            the meta information.
	 * @return an xml representation.
	 */
	String getXml(String meta);

	/**
	 * Sets the user for this menu. A subsequent call to {@linkplain #getXml()}
	 * should then return the same result as a call to
	 * {@linkplain #getXml(User)}, if the user is the same.
	 * 
	 * @param u
	 *            the user.
	 */
	void setUser(User u);

	/**
	 * Returns the options for a selection on this menu.
	 * 
	 * @return the possible selections on this menu.
	 */
	String[] getOptions();

	/**
	 * Returns the id for the corresponding selection
	 * 
	 * @param selection
	 *            the user's selection.
	 * @return the id of the menu that correspond's to the user's selection for
	 *         this menu.
	 */
	String getIdFor(String selection);

	/**
	 * This is the public interface for {@linkplain Cloneable}.
	 * 
	 * @return a copy of the current object, or the same object, if it is not
	 *         cloneable.
	 * 
	 * @see Cloneable
	 */
	IMenu copy();

}