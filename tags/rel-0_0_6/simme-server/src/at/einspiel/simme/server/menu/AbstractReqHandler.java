// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: AbstractReqHandler.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

/**
 * An abstract utility implementation for
 * {@linkplain at.einspiel.simme.server.menu.IReqHandler}.
 * @author kariem
 */
abstract class AbstractReqHandler implements IReqHandler {

	/**
	 * The id of the current menu. This will be transferred, in case the client
	 * has to reconnect to the same menu.
	 */
	private String idCurrent;

	/**
	 * The id of the next menu. This may be null, if the id was not set in the
	 * constructor.
	 */
	private String idNext;

	/**
	 * Default constructor for easier subclassing.
	 * @param nextId
	 *            the id of the next menu.
	 */
	AbstractReqHandler(String nextId) {
		this.idNext = nextId;
	}

	/**
	 * Default constructor for easier subclassing.
	 * @param idCurrent
	 *            the id of the current menu, in case, the same menu has to be
	 *            reloaded.
	 * @param idNext
	 *            the id of the next menu.
	 */
	AbstractReqHandler(String idCurrent, String idNext) {
		this.idCurrent = idCurrent;
		this.idNext = idNext;
	}

	/**
	 * Creates a new instance of <code>AbstractReqHandler</code>. No next id
	 * is specified.
	 */
	AbstractReqHandler() {
		this(null);
	}

	/**
	 * Creates a text message, that will be automatically updated by the client.
	 * The id necessary for this text component will be taken from the field
	 * <i>idNext</i>.
	 * @param title
	 *            the message's title.
	 * @param message
	 *            the message itself.
	 * @param time
	 *            the time in seconds to update.
	 * @param reloadNext
	 *            whether to set the next id as id or the current. If set to
	 *            <code>true</code> the next id will be inserted into the
	 *            <code>id</code> field of the text message.
	 * @param longMessage
	 *            whether to use the long message format.
	 * 
	 * @return a string representation of the text message that can be
	 *         interpreted by the client.
	 * 
	 * @see TextXml
	 */
	String createTextMessage(String title, String message, int time, boolean reloadNext,
			boolean longMessage) {
		// choose which id to load
		String id = reloadNext ? idNext : idCurrent;
		// create text message
		TextXml textXml = new TextXml(title, id, message, longMessage, time, null);
		// convert to xml
		return textXml.toString();
	}

	/**
	 * Creates a text message, that will be automatically updated by the client.
	 * The id necessary for this text component will be taken from the field
	 * <i>idNext</i>. The message will be created in the short format.
	 * 
	 * @param title
	 *            the message's title.
	 * @param message
	 *            the message itself.
	 * @param time
	 *            the time in seconds to update.
	 * @param reloadNext
	 *            whether to set the next id as id or the current. If set to
	 *            <code>true</code> the next id will be inserted into the
	 *            <code>id</code> field of the text message.
	 * 
	 * @return a string representation of the text message that can be
	 *         interpreted by the client.
	 * 
	 * @see #createTextMessage(String, String, int, boolean, boolean)
	 */
	String createTextMessage(String title, String message, int time, boolean reloadNext) {
		return createTextMessage(title, message, time, reloadNext, false);
	}
}