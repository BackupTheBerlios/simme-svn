// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: XMLUtils.java
//                  $Date: 2004/09/13 15:12:35 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import at.einspiel.messaging.ISimpleInfo;
import at.einspiel.simme.nanoxml.XMLElement;

/**
 * Provides methods for faster XML parsing.
 * @author kariem
 */
public class XMLUtils {

	private XMLUtils() {
		// private constructor for utility class
	}

	/**
	 * Returns the child elements of a specified <code>Element</code>.
	 * 
	 * @param parentElement
	 *            the element which will be searched.
	 * 
	 * @return a <code>List</code> of DOM elements, that are direct children
	 *         of the given parent node.
	 */
	public static List getDirectChildren(Element parentElement) {
		List directChildren = new ArrayList();
		Node child = parentElement.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				directChildren.add(child);
			}
			child = child.getNextSibling();
		}
		return directChildren;
	}

	/**
	 * Returns the text contained in the given element.
	 * @param e
	 *            the element.
	 * @return the text in the first textnode within the element, or
	 *         <code>null</code>, if the element does not contain any text
	 *         nodes.
	 */
	public static String getText(Element e) {
		Node child = e.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.TEXT_NODE) {
				// return the text of the text node
				return child.getNodeValue();
			}
		}
		return null;
	}

	/**
	 * Adds an attribute to the given string buffer object. Effectively this
	 * method appends a string in the following format to the buffer, prepended
	 * by a single space (" "):
	 * 
	 * <code>$paramName="$paramValue"</code>
	 * 
	 * @param paramName
	 *            the attribute's name.
	 * @param paramValue
	 *            the attribute's value.
	 * @param buf
	 *            the buffer.
	 */
	public static void addAttrToBuf(String paramName, String paramValue, StringBuffer buf) {
		// single space
		buf.append(" ");
		// name="value"
		buf.append(paramName);
		buf.append("=\"");
		buf.append(paramValue);
		buf.append("\"");
	}

	/**
	 * Creates the start of an xml message, which can be sent to the client. The
	 * format is the following:
	 * 
	 * <pre>&lt;$elementName title="$title" id="$id" [list="true"]</pre>
	 * 
	 * Note that the opening tag is not closed by <code>&gt;</code>. The
	 * attribute <i>list </i> will only be added to the string if the parameter
	 * <code>list</code> is <code>true</code>.
	 * 
	 * @param title
	 *            The message's title.
	 * @param id
	 *            The message id.
	 * @param elementName
	 *            The element name.
	 * @param list
	 *            whether the component should be rendered as list.
	 * @return the first tag of an xml message without the closing
	 *         <code>></code>.
	 */
	public static String createXmlStart(String title, String id, String elementName,
			boolean list) {
		StringBuffer xml = new StringBuffer("<");
		xml.append(elementName);

		addAttrToBuf(ISimpleInfo.ATTR_TITLE, title, xml);
		addAttrToBuf(ISimpleInfo.ATTR_ID, id, xml);
		// obey to client's limitation and use XMLElement implementation instead
		// of Boolean.toString(bool).
		if (list) {
			addAttrToBuf(ISimpleInfo.ATTR_LIST, XMLElement.TRUE, xml);
		}

		return xml.toString();
	}
}