// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: ISimpleInfo.java
//                  $Date: 2004/09/02 10:17:29 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.util.Vector;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * @author kariem
 */
public interface ISimpleInfo {

	/**
	 * Returns the title.
	 * @return the title.
	 */
	String getTitle();

	/**
	 * Returns the id.
	 * @return the id.
	 */
	byte getId();

	/**
	 * Shows, whether the information object should be rendered as a list.
	 * @return <code>true</code> if it should be rendered as a list,
	 *         <code>false</code> otherwise.
	 */
	boolean isList();

	/**
	 * Shows, wheter this object contains xml meta information.
	 * @return <code>true</code> if it contains xml meta information,
	 *         <code>false</code> otherwise.
	 */
	boolean isXml();

	/**
	 * Returns the list elements.
	 * @return the list elements. Returns <code>null</code> if
	 *         {@linkplain #isList()}returns <code>false</code>.
	 */
	Vector getListElements();

	/**
	 * Returns a text representation of this info object.
	 * @return the text representation of this object.
	 */
	String getText();

	/**
	 * Returns an xml representation.
	 * 
	 * @return the xml information.
	 */
	XMLElement getXmlInfo();

	/**
	 * Returns a string which can be used to create a new InfoObject.
	 * @return a string representing this info object.
	 */
	String getXmlString();

}