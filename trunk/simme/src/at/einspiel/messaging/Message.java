// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: Message.java
//                  $Date: 2003/12/30 23:05:29 $
//              $Revision: 1.2 $
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
     * The string that is usually displayed to the user, describing the cause
     * or meta information.
     * 
     * @return a string containing user readable information.
     */
    String getInfo();
}
