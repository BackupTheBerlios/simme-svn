package at.einspiel.messaging;

/**
 * Small interface for a message.
 * 
 * @author kariem
 */
public interface Message {

    /** Message does not have an id. */
    byte NO_ID = 0;
    /** Login was successful. */
    byte LOGIN_OK = 1; 
    /** Login failed. */
    byte LOGIN_FAILED = 2; 

    /**
     * String representation of this message. Ususally this is the form to be
     * sent throught serialization
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
