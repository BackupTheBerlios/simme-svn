// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: InfoList.java
//                  $Date: 2004/08/25 15:34:24 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.util.Enumeration;
import java.util.Vector;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * @author kariem
 */
public class InfoList extends AbstractInfo implements ISimpleInfo {

	/** enumeration of list elements */
	private static final Vector listElements = new Vector(0);

	/**
	 * Creates a new instance of <code>InfoList</code>.
	 * @param xml
	 */
	public InfoList(XMLElement xml) {
		super(xml);
		// listElements.removeAllElements(); // TODO remove line after check
		// add children to vector
		Enumeration enum = xml.enumerateChildren();
		while (enum.hasMoreElements()) {
			XMLElement element = (XMLElement) enum.nextElement();
			String childName = element.getContent();
			if (childName != null) {
				listElements.addElement(childName);
			}
		}
	}
	
	/** @see at.einspiel.messaging.ISimpleInfo#getListElements() */
	public Vector getListElements() {
		return listElements;
	}

	/** @see at.einspiel.messaging.ISimpleInfo#isList() */
	public boolean isList() {
		return true;
	}


	/** @see at.einspiel.messaging.AbstractInfo#addXmlInfo(at.einspiel.simme.nanoxml.XMLElement) */
	protected void addXmlInfo(XMLElement xml) {
		if (!listElements.isEmpty()) {
			xml.setAttribute("list", XMLElement.TRUE);
			Enumeration enum = listElements.elements();
			while (enum.hasMoreElements()) {
				String el = (String) enum.nextElement();
				XMLElement child = new XMLElement();
				child.setContent(el);
				xml.addChild(child);
			}
		}
	}
}