// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: InfoText.java
//                  $Date: 2004/08/25 15:34:24 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import at.einspiel.logging.Logger;
import at.einspiel.simme.nanoxml.XMLElement;
import at.einspiel.simme.nanoxml.XMLParseException;

/**
 * The information object for simple user interfaces.
 * 
 * @author kariem
 */
public class InfoText extends AbstractInfo implements ISimpleInfo {

	/** for simple uis: only text */
	private String text;

	/**
	 * Creates a new instance of <code>InfoText</code> with the
	 * information from an xml object.
	 * 
	 * @param xml
	 *            contains the text message either in the attribute or the tag
	 *            named <code>msg</code>:
	 * 
	 * <pre>
	 *   &lt;text msg="{text}"/&gt;
	 *   &lt;text&gt;
	 *     &lt;msg&gt;{text}&lt;/msg&gt;
	 *   &lt;/text&gt;
	 *  </pre>
	 *  
	 */
	public InfoText(XMLElement xml) {
		super(xml);
		// text sent => retrieve msg attribute or element
		text = xml.getAttribute("msg");
		if (text == null || text.length() == 0) {
			if (xml.countChildren() > 0) {
				text = ((XMLElement) xml.getChildren().elementAt(0)).getContent();
			}
			// fall-back instead of showing nothing
			text = "Warte...";
		}
	}

	/**
	 * Creates a simple xml text object.
	 * @param title
	 *            the title
	 * @param message
	 *            the message
	 */
	public InfoText(String title, String message) {
		super(title);
		this.text = message;
	}

	/**
	 * Creates a new instance of <code>InfoText</code>.
	 * 
	 * @param xmlString
	 *            the full xml string that was supplied
	 * @param xex
	 *            the exception that was thrown while trying to parse
	 *            <code>xmlString</code>
	 */
	public InfoText(String xmlString, XMLParseException xex) {
		this("Information", xmlString);
		Logger.debug("InfoText created with errors: " + xex.getMessage());
	}

	/** @see at.einspiel.messaging.ISimpleInfo#getText() */
	public String getText() {
		return text;
	}

	/** @see at.einspiel.messaging.AbstractInfo#addXmlInfo(at.einspiel.simme.nanoxml.XMLElement) */
	protected void addXmlInfo(XMLElement xml) {
		xml.setAttribute("msg", text);
	}

}