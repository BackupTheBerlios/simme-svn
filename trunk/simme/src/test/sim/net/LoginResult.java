package test.sim.net;

import nanoxml.XMLElement;
import nanoxml.XMLParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * The result of a login process.
 *
 * @author kariem
 */
public class LoginResult {
    /** The element name of the string representation */
    public static final String ELEMENT_NAME = "loginresult";

    /** login succeeded */
    private boolean succeed;

    /** login message */
    private String message;

    /** response url */
    private String url;

    /**
     * Creates a new <code>LoginResult</code> from the data in the xml string. If
     * <code>xmlString</code> does not contain valid data, the login result is
     * set to not succeeded and the message contains a general error message.
     *
     * @param xmlString the string containing data in XML format.
     */
    public LoginResult(String xmlString) {
        XMLElement element = new XMLElement();

        try {
            element.parseString(xmlString.trim());
            setSucceed(element.getAttributeBoolean("succeed"));
            setMessage((String) element.getAttribute("msg"));
            setUrl((String) element.getAttribute("url"));
        } catch (XMLParseException xex) {
            setSucceed(false);
            setMessage("Reply had errors.");
        }
    }

    /**
     * Creates a new <code>LoginResult</code> from the given properties.
     *
     * @param succeeded <code>true</code> if the login process succeeded,
     *         otherwise <code>false</code>.
     * @param url the new url to load.
     * @param msg the message with additional information.
     */
    public LoginResult(boolean succeeded, String msg, String url) {
        setSucceed(succeeded);
        setMessage(msg);
        setUrl(url);
    }

    /**
     * @return an XML representation of this result or <code>null</code> if a
     *         problem occured while generating XML.
     *
     * @see Object#toString()
     */
    public String toString() {
        XMLElement element = new XMLElement();
        element.setName(ELEMENT_NAME);
        if (succeed) {
            element.setAttribute("succeed", "1");
            // only write URL if succeed == true
            element.setAttribute("url", url);
        } else {
            element.setAttribute("succeed", "0");
        }
        element.setAttribute("msg", message);

        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(bas);

        try {
            element.write(writer);
            writer.flush();
        } catch (IOException e) {
            return null;
        }

        return bas.toString();
    }

    /**
     * Returns the message.
     *
     * @return the message contained in this <code>LoginResult</code>.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Shows success or failure of the login process.
     *
     * @return <code>true</code> if the process succeeded, otherwise
     *         <code>false</code>.
     */
    public boolean isSucceed() {
        return succeed;
    }

    /**
     * Returns the url.
     *
     * @return the url which will be used after a successful login.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the message.
     * @param string the message to set.
     */
    public void setMessage(String string) {
        message = string;
    }

    /**
     * Sets the succeeded value.
     *
     * @param b succeeded or failed.
     */
    public void setSucceed(boolean b) {
        succeed = b;
    }

    /**
     * Sets the url.
     * @param string the url to set.
     */
    public void setUrl(String string) {
        url = string;
    }
}
