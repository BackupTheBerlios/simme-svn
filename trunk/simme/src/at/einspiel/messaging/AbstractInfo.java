// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: AbstractInfo.java
//                  $Date: 2004/08/25 15:34:24 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.util.Vector;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * @author kariem
 */
public abstract class AbstractInfo implements ISimpleInfo {

	private String title;
	private byte id;

	/**
	 * Creates a new instance of <code>AbstractInfo</code>.
	 * @param title
	 *            the title
	 * @param id
	 *            the id
	 */
	public AbstractInfo(String title, byte id) {
		this.title = title;
		this.id = id;
	}

	/**
	 * Creates a new instance of <code>AbstractInfo</code>.
	 * @param xml
	 *            an xml element.
	 */
	public AbstractInfo(XMLElement xml) {
		this(xml.getAttribute("title", "Auswahl"), (byte) xml.getAttributeInt("id", 0));
	}

	/**
	 * Creates a new instance of <code>AbstractInfo</code>.
	 * @param title
	 *            the title;
	 */
	public AbstractInfo(String title) {
		this(title, (byte) 0);
	}

	/** @see at.einspiel.messaging.ISimpleInfo#getTitle() */
	public String getTitle() {
		return title;
	}

	/** @see at.einspiel.messaging.ISimpleInfo#getId() */
	public byte getId() {
		return id;
	}

	/** @see at.einspiel.messaging.ISimpleInfo#getXmlString() */
	public String getXmlString() {
		XMLElement xml = new XMLElement();
		xml.setName("sendable");
		xml.setAttribute("title", title);
		xml.setAttribute("id", Integer.toString(id));

		// append additional XML information to xml object
		addXmlInfo(xml);
		return xml.toString();
	}

	/**
	 * Adds xml information to the given element. This method has to be
	 * implemented by subclasses, in order to save information only available in
	 * these subclasses.
	 * @param xml
	 *            the element to which additional information should be added.
	 */
	protected abstract void addXmlInfo(XMLElement xml);

	//
	// default implementations
	//

	/**
	 * Always returns <code>false</code>.
	 * @see at.einspiel.messaging.ISimpleInfo#isList()
	 */
	public boolean isList() {
		return false;
	}

	/** @see at.einspiel.messaging.ISimpleInfo#isXml() */
	public boolean isXml() {
		return false;
	}

	/**
	 * Always returns <code>null</code>.
	 * @see at.einspiel.messaging.ISimpleInfo#getListElements()
	 */
	public Vector getListElements() {
		return null;
	}

	/**
	 * Always returns <code>null</code>.
	 * @see at.einspiel.messaging.ISimpleInfo#getText()
	 */
	public String getText() {
		return null;
	}

	/**
	 * Always returns <code>null</code>.
	 * @see at.einspiel.messaging.ISimpleInfo#getXmlInfo()
	 */
	public XMLElement getXmlInfo() {
		return null;
	}
}