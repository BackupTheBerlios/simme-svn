// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: TextMenu.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import at.einspiel.messaging.ISimpleInfo;

/**
 * A simple text menu containing only a single message. There are two possible
 * formats:
 * 
 * <pre>
 *   &lt;text title="${text}" 
 *         id="${id}" 
 *         msg="${text}"/&gt;
 * </pre>
 * 
 * which is used for short messages and
 * 
 * <pre>
 *   &lt;text title="${text}" id="${id}"&gt;
 *       &lt;msg&gt;${text}&lt;/msg&gt;
 *   &lt;/text&gt;
 * </pre>
 * 
 * for longer messages.
 * 
 * @author kariem
 */
class TextMenu extends AbstractMenu {

	static final String STR_MESSAGE = "msg";
	String xml;

	/**
	 * Creates a new instance of <code>TextMenu</code>.
	 * @param e
	 *            an xml element.
	 */
	TextMenu(Element e) {
		super(e);
		boolean longMessage = false;
		// first get attribute "msg"
		String message = e.getAttribute(STR_MESSAGE);

		if (message == null || message.trim().length() == 0) {
			// no such attribute => retrieve text of msg element
			longMessage = true;
			Element elementMsg = (Element) e.getElementsByTagName(STR_MESSAGE).item(0);
			NodeList children = elementMsg.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node n = children.item(0);
				if (n.getNodeType() == Node.TEXT_NODE) {
					// assign text of text node to message
					message = n.getNodeValue().trim();
					break;
				}
			}

			// tokenize ... retain "\n"
			StringTokenizer strTok = new StringTokenizer(message, " \t\r\f");
			StringBuffer result = new StringBuffer();
			// concatenate
			while (strTok.hasMoreElements()) {
				result.append(strTok.nextToken());
				result.append(' ');
			}
			message = result.toString().trim();
			// replace single newlines with spaces
			message = message.replaceAll("\n ", " ");
			// remaining newlines are converted into double newlines.
			message = message.replaceAll("\n ", "\n\n");
		}

		xml = createTextXml(title, id, message, longMessage);
	}

	/**
	 * Creates a text representation for a text message with title id and
	 * message.
	 * 
	 * @param title
	 *            the title.
	 * @param id
	 *            the menu id.
	 * @param message
	 *            the message.
	 * @param longMessage
	 *            if set to <code>true</code>, this method will put the
	 *            <code>message</code> string into a separate
	 *            <code>&lt;msg&gt;</code> element. Otherwise the message will
	 *            be contained within an attribute.
	 * @return an xml representation of the message.
	 */
	static String createTextXml(String title, String id, String message, boolean longMessage) {
		// build xml representation
		StringBuffer xmlBuf = new StringBuffer(createTextXmlStart(title, id));
		if (!longMessage) {
			// attribute with message
			addAttrToBuf(STR_MESSAGE, message, xmlBuf);
			// close attribute and text element
			xmlBuf.append("/>");
		} else {
			xmlBuf.append(">");

			//element msg
			// open element
			xmlBuf.append("<");
			xmlBuf.append(STR_MESSAGE);
			xmlBuf.append(">");
			// element content
			xmlBuf.append(message);
			// close element
			xmlBuf.append("</");
			xmlBuf.append(STR_MESSAGE);
			xmlBuf.append(">");

			// close text element
			xmlBuf.append("</");
			xmlBuf.append(ISimpleInfo.TAG_TEXT);
			xmlBuf.append(">");
		}
		return xmlBuf.toString();
	}

	/**
	 * Creates the start for the text representation of a text message.
	 * @param title
	 *            the title.
	 * @param id
	 *            the id.
	 * @return an xml representation of a text message. The format is
	 *         <code>&lt;text title="$title" id="$id"</code>. Note that the
	 *         resulting string does not contain the closing <code>&gt;</code>
	 *         of the tag <code>text</code>.
	 */
	static String createTextXmlStart(String title, String id) {
		return createXmlStart(title, id, ISimpleInfo.TAG_TEXT, false);
	}
	
	static String createTextXmlStart(String title, String id, int updateTime, String updatePath) {
		StringBuffer textStart = new StringBuffer(createTextXmlStart(title, id));
		
		if (updateTime > 0) {
			addAttrToBuf(ISimpleInfo.ATTR_UPDATE, Integer.toString(updateTime), textStart);
			addAttrToBuf(ISimpleInfo.ATTR_UPDATE_PATH, updatePath, textStart);
		}
		return textStart.toString();
	}

	/** @see at.einspiel.simme.server.menu.IMenu#getXml() */
	public String getXml() {
		return xml;
	}
}