// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IMenu.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.simme.server.ManagedUser;

/**
 * Used to model a server-side menu. The contents of menus are sent to the
 * client, via the methods {@link #getXml()}and {@link #getXml(String)}.
 * 
 * @author kariem
 */
public interface IMenu extends Cloneable {

	/** The default id for menus. The current value is <code>0</code>. */
	String DEFAULT_ID = "0";

	/**
	 * The type attribute, which may be used for different instances of the same
	 * class.
	 */
	String ATTR_TYPE = "type";

	/**
	 * The value of this attribute shows the next id to be used. The client
	 * usually receives a component with auto-update capabilities and will be
	 * redirected to the menu with the ID indicated by this attribute.
	 */
	String ATTR_NEXT = "next";

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
	 * Returns an xml representation, which can be sent to the client.
	 * 
	 * @return an xml representation.
	 * @see #getXml(String)
	 */
	String getXml();

	/**
	 * Returns an xml representation that may be distinct from the result of
	 * {@link #getXml()}. The additional parameter is used to generate a
	 * different output. The menu ignores the parameter, if no meta attributes
	 * are set.
	 * 
	 * @param meta
	 *            the meta information.
	 * @return an xml representation.
	 */
	String getXml(String meta);

	/**
	 * Sets the user for this menu. A subsequent call to {@linkplain #getXml()}
	 * can result in a different behaviour compared to the result without a
	 * prior call to this method. Especially, if the menu is designed to be user
	 * specific.
	 * 
	 * @param mu
	 *            the user.
	 */
	void setUser(ManagedUser mu);

	/**
	 * Returns the options for a selection on this menu. The associated id of
	 * the selected menu can be accessed by {@link #getIdFor(String)}with one
	 * of the options as parameter.
	 * 
	 * @return all possible selections on this menu.
	 * @see #getIdFor(String)
	 */
	String[] getOptions();

	/**
	 * Returns the menu id for the corresponding selection. The selection should
	 * be one of the results of a call to {@link #getOptions()}.
	 * 
	 * @param selection
	 *            the user's selection.
	 * @return the id of the menu that correspond's to the user's selection for
	 *         this menu.
	 * @see #getOptions()
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