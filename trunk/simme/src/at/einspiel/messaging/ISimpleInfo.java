// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: ISimpleInfo.java
//                  $Date: 2004/09/07 13:23:06 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.util.Vector;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * A class that represents a simple information object, which can be used to
 * create a basic user interface.
 * 
 * @author kariem
 */
public interface ISimpleInfo {

	/** Tag for text elements */
	String TAG_TEXT = "text";

	/** The attribute for the <i>id </i>. */
	String ATTR_ID = "id";
	/** The attribute for the <i>title </i>. */
	String ATTR_TITLE = "title";
	/** Attribute that defines a list. */
	String ATTR_LIST = "list";
	/** Attribute that shows that there is meta-information */
	String ATTR_METAINFO = "metainfo";
	/**
	 * Attribute that indicates the time for the next update. The SendableUI
	 * object will update automatically.
	 */
	String ATTR_UPDATE = "update";
	/**
	 * Optional attribute that only exists together with
	 * {@linkplain #ATTR_UPDATE}. It indicates the URL to update from if
	 * different from the URL prior to the request.
	 */
	String ATTR_UPDATE_PATH = "updateURL";

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
	 * Shows, whether this object contains xml meta information.
	 * @return <code>true</code> if it contains xml meta information,
	 *         <code>false</code> otherwise.
	 */
	boolean isXml();

	/**
	 * Whether this object has associated meta-information.
	 * @return <code>true</code> if there is meta-info associated with this
	 *         object, <code>false</code> otherwise.
	 */
	boolean hasMetaInfo();

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