// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: Message.java
//                  $Date: 2004/08/25 15:40:01 $
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

/**
 * Small interface for a message.
 * 
 * @author kariem
 */
public interface Message {

	/** Message does not have an id. */
	byte NO_ID = 0;

	/** Attribute name for id. */
	String ID = "id";

	/**
	 * String representation of this message. Usually this is the form to be
	 * sent through serialization
	 * 
	 * @return String representation.
	 */
	String getMessage();

	/**
	 * The id defines the information found in the message. With this id the
	 * information can easily be processed.
	 * 
	 * @return the id of the message.
	 */
	byte getId();

	/**
	 * The string that is usually displayed to the user. This is the content of
	 * the message, but can also contain meta-information.
	 * 
	 * @return a string containing user readable information.
	 */
	String getInfo();
}