// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: TextXml.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.messaging.ISimpleInfo;
import at.einspiel.util.XMLUtils;

/**
 * Represents a simple message that may be sent to the client. The client may
 * render this object's information as uneditable text component.
 * 
 * @author kariem
 */
public class TextXml {
	String title;
	String id;
	String message;
	boolean longMessage;
	int updateTime;
	String updatePath;

	/**
	 * Creates a new instance of <code>TextXml</code>.
	 * @param title
	 *            The message title.
	 * @param id
	 *            The id.
	 * @param longMessage
	 *            Whether the message is longer. If <code>true</code> the
	 *            string representation of this object holds an additional
	 *            &lt;msg&gt; element. If set to <code>false</code>, the
	 *            message is added as an attribute to the main element.
	 * @param message
	 *            The actual content.
	 * @param updateTime
	 *            the time in seconds to update. If less than 1, then
	 *            <code>updatePath</code> will be ignored on output.
	 * @param updatePath
	 *            the path that points to the new location. This parameter is
	 *            only useful in conjunction with <code>updateTime</code>.
	 *  
	 */
	public TextXml(String title, String id, String message, boolean longMessage,
			int updateTime, String updatePath) {
		this.title = title;
		this.id = id;
		this.message = message;
		this.longMessage = longMessage;
		this.updateTime = updateTime;
		this.updatePath = updatePath;
	}

	/**
	 * Creates a new instance of <code>TextXml</code>.
	 * @param title
	 *            The message title.
	 * @param id
	 *            The id.
	 * @param longMessage
	 *            Whether the message is longer. If <code>true</code> the
	 *            string representation of this object holds an additional
	 *            &lt;msg&gt; element. If set to <code>false</code>, the
	 *            message is added as an attribute to the main element.
	 * @param message
	 *            The actual content.
	 */
	public TextXml(String title, String id, String message, boolean longMessage) {
		this(title, id, message, longMessage, 0, null);
	}

	/**
	 * Creates a new instance of <code>TextXml</code>. The message will be in
	 * short format.
	 * @param title
	 *            The message title.
	 * @param id
	 *            The id.
	 * @param message
	 *            The actual content.
	 * @param updateTime
	 *            the time in seconds to update. If it is less than 1, it will
	 *            be ignored.
	 * @param updatePath
	 *            the path that points to the new location. This parameter is
	 *            only useful in conjunction with <code>updateTime</code>. If
	 *            set to <code>null</code> the current location should be
	 *            selected for update.
	 * 
	 * @see #TextXml(String, String, String, boolean, int, String)
	 */
	public TextXml(String title, String id, String message, int updateTime, String updatePath) {
		this(title, id, message, false, updateTime, updatePath);
	}

	/**
	 * Creates a new instance of <code>TextXml</code>. The message will be in
	 * short format.
	 * @param title
	 *            The message title.
	 * @param id
	 *            The id.
	 * @param message
	 *            The actual content.
	 * @param updateTime
	 *            the time in seconds to update. If it is less than 1, it will
	 *            be ignored.
	 * 
	 * @see #TextXml(String, String, String, boolean, int, String)
	 */
	public TextXml(String title, String id, String message, int updateTime) {
		this(title, id, message, updateTime, null);
	}

	/**
	 * Creates a new instance of <code>TextXml</code>.
	 * @param title
	 *            The message title.
	 * @param id
	 *            The id.
	 * @param message
	 *            The actual content.
	 */
	public TextXml(String title, String id, String message) {
		this(title, id, message, 0, null);
	}

	/**
	 * Returns an XML representation of this object. The format is either
	 * 
	 * <pre>
	 *   &lt;text title="${text}" 
	 *         id="${id}" 
	 *         msg="${text}"/&gt;
	 * </pre>
	 * 
	 * or, for longer messages:
	 * 
	 * <pre>
	 *   &lt;text title="${text}" id="${id}"&gt;
	 *       &lt;msg&gt;${text}&lt;/msg&gt;
	 *   &lt;/text&gt;
	 * </pre>
	 * 
	 * @return an XML representation that can be displayed as text component on
	 *         the client's display.
	 */
	public String toString() {
		StringBuffer xmlBuf = new StringBuffer(createTextXmlStart(title, id, updateTime,
				updatePath));
		if (!longMessage) {
			// attribute with message
			XMLUtils.addAttrToBuf(TextMenu.STR_MESSAGE, message, xmlBuf);
			// close attribute and text element
			xmlBuf.append("/>");
		} else {
			xmlBuf.append(">");

			//element msg
			// open element
			xmlBuf.append("<");
			xmlBuf.append(TextMenu.STR_MESSAGE);
			xmlBuf.append(">");
			// element content
			xmlBuf.append(message);
			// close element
			xmlBuf.append("</");
			xmlBuf.append(TextMenu.STR_MESSAGE);
			xmlBuf.append(">");

			// close text element
			xmlBuf.append("</");
			xmlBuf.append(ISimpleInfo.TAG_TEXT);
			xmlBuf.append(">");
		}
		return xmlBuf.toString();
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
		TextXml xmlText = new TextXml(title, id, message, longMessage);
		return xmlText.toString();
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
		return XMLUtils.createXmlStart(title, id, ISimpleInfo.TAG_TEXT, false);
	}

	static String createTextXmlStart(String title, String id, int updateTime, String updatePath) {
		StringBuffer textStart = new StringBuffer(createTextXmlStart(title, id));

		if (updateTime > 0) {
			XMLUtils.addAttrToBuf(ISimpleInfo.ATTR_UPDATE, Integer.toString(updateTime),
					textStart);
			if (updatePath != null) {
				XMLUtils.addAttrToBuf(ISimpleInfo.ATTR_UPDATE_PATH, updatePath, textStart);
			}
		}
		return textStart.toString();
	}
}