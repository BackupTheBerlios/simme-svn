// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: InfoXml.java
//                  $Date: 2004/09/13 15:26:53 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * @author kariem
 */
public class InfoXml extends AbstractInfo {

	/** optional game information */
	private XMLElement xmlElement;

	/**
	 * Creates a new instance of <code>InfoXml</code>.
	 * @param xml
	 *            the xml meta object, that contains information about title and
	 *            id. The first child will contain the internal xml information.
	 */
	public InfoXml(XMLElement xml) {
		super(xml);
		xmlElement = (XMLElement) xml.getChildren().elementAt(0);
	}

	/**
	 * Returns the xml information.
	 * 
	 * @return the xml information.
	 */
	public XMLElement getXmlInfo() {
		return xmlElement;
	}

	/** @see at.einspiel.messaging.AbstractInfo#addXmlInfo(at.einspiel.simme.nanoxml.XMLElement) */
	protected void addXmlInfo(XMLElement xml) {
		xml.addChild(xmlElement);
	}

	/** @see at.einspiel.messaging.ISimpleInfo#isXml() */
	public boolean isXml() {
		return true;
	}
}