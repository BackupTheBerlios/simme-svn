package at.einspiel.simme.server.messaging;

import at.einspiel.simme.client.net.Message;
import at.einspiel.simme.client.net.SendableUI;

/**
 * This class implements the message interface and allows for easy transfer of
 * information to the client/user.
 * 
 * @author kariem
 */
public class SimpleClientMessage extends SendableUI implements Message {

    /**
     * Creates a simple message with the given title and content.
     * 
     * @param title the title.
     * @param msg the message.
     */
    public SimpleClientMessage(String title, String msg) {
        super();
        setTitle(title);
        setText(msg);
    }

    /**
     * Creates a simple message with the given content.
     * 
     * @param msg the message.
     * 
     * @see #SimpleClientMessage(String, String)
     */
    public SimpleClientMessage(String msg) {
        this("Information", msg);
    }

    /** @see Message#getMessage() */
    public String getMessage() {
        return getXmlString();
    }
    
    /** @see Message#getId() */
    public byte getId() {
        return 0;
    }
    
    /** @see Message#getInfo() */
    public String getInfo() {
        return getText();
    }
}
