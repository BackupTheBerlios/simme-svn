// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: AbstractInfo.java
//                  $Date: 2004/09/21 16:18:52 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.util.Vector;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * Abstract implementation of {@linkplain at.einspiel.messaging.ISimpleInfo}.
 * This class contains simple constructors which initialize the fields
 * <code>id</code> and <code>title</code>. Additionally default
 * implementations for the methods definied in
 * {@linkplain at.einspiel.messaging.ISimpleInfo} are provided.
 * 
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
		this(xml.getAttribute(ATTR_TITLE, "Auswahl"), (byte) xml.getAttributeInt(ATTR_ID, 0));
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

	/** @see at.einspiel.messaging.ISimpleInfo#setDefaultId() */
	public void setDefaultId() {
		id = ISimpleInfo.DEFAULT_ID;
	}
	
	/** @see at.einspiel.messaging.ISimpleInfo#getXmlString() */
	public String getXmlString() {
		XMLElement xml = new XMLElement();
		xml.setName("sendable");
		xml.setAttribute(ISimpleInfo.ATTR_TITLE, title);
		xml.setAttribute(ISimpleInfo.ATTR_ID, Integer.toString(id));

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

	/**
	 * Always returns <code>false</code>.
	 * @see at.einspiel.messaging.ISimpleInfo#hasMetaInfo()
	 */
	public boolean hasMetaInfo() {
		return false;
	}
}