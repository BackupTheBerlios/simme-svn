// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: MoveMessage.java
//                  $Date: 2004/09/13 15:26:53 $
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import at.einspiel.messaging.IMessage;
import at.einspiel.simme.nanoxml.XMLElement;
import at.einspiel.simme.nanoxml.XMLParseException;

/**
 * Holds the information on the move.
 * @author kariem
 */
public class MoveMessage implements IMessage {

	/** The name of the element. */
	protected static final String NAME = "move";

	/** The attribute for the information. */
	protected static final String ATTR_INFO = "info";
	/** The attribute name for the move. */
	protected static final String ATTR_MOVE = "move";

	/** The edge index of this move. */
	protected byte move;
	/** The move id */
	protected byte id;
	/** The information */
	protected String info;

	/**
	 * Creates a new instance of <code>MoveMessage</code>.
	 */
	protected MoveMessage() {
		// default constructor for subclasses
	}

	/**
	 * Creates a new instance of <code>MoveMessage</code>.
	 * 
	 * @param xmlString
	 *            must be of the following format
	 * 
	 * <pre>
	 *   &lt;move move="&lt;edge&gt;"
	 *         info="&lt;text&gt;"
	 *         id="&lt;msg-id&gt;" /&gt;
	 * </pre>
	 * 
	 * <i>move </i> is mandatory, while the other two attributes, <i>info </i>
	 * and <i>id </i> are optional.
	 * 
	 * @throws NumberFormatException
	 *             if the value of the <i>move </i> attribute is not parseable
	 *             as <code>java.lang.Byte</code> or is <code>null</code>.
	 * @throws XMLParseException
	 *             if the xmlString could not be parsed correctly
	 */
	public MoveMessage(String xmlString) throws NumberFormatException, XMLParseException {
		XMLElement xml = new XMLElement();
		xml.parseString(xmlString);
		// set info from response
		info = xml.getAttribute(ATTR_INFO);

		// create move from response if possible
		move = Byte.parseByte(xml.getAttribute(ATTR_MOVE));

		// create id from response if possible
		String idString = xml.getAttribute(ID);
		if (idString != null) {
			id = Byte.parseByte(idString);
		}
	}

	/**
	 * This method does not have any implementation, because this is not
	 * necessary on the client side. The method should be overridden by
	 * subclasses.
	 * @see at.einspiel.messaging.IMessage#getMessage()
	 */
	public String getMessage() {
		// must be overloaded
		// it should be possible to pass the result of this method to the
		// constructor
		return null;
	}

	/**
	 * Returns the edge.
	 * @return the edge.
	 */
	public byte getMove() {
		return move;
	}

	/** @see at.einspiel.messaging.IMessage#getId() */
	public byte getId() {
		return id;
	}

	/** @see at.einspiel.messaging.IMessage#getInfo() */
	public String getInfo() {
		return info;
	}
}