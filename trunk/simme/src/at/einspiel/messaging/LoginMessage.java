// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: LoginMessage.java
//                  $Date: 2004/09/13 15:26:53 $
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import at.einspiel.simme.nanoxml.XMLElement;
import at.einspiel.simme.nanoxml.XMLParseException;

/**
 * The result of a login process. This message is returned after logging in with
 * the server. The success of a login process may be retrieved via
 * {@link #isSuccess()}. The corresponding status or information message is set
 * in the field <code>info</code>, and may be accessed via {@link #getInfo()}.
 * 
 * @author kariem
 */
public class LoginMessage implements IMessage {
	/** The element name of the string representation */
	public static final String ELEMENT_NAME = "loginresult";

	/**
	 * <code>true</code> if the login has succeeded, <code>false</code>
	 * otherwise.
	 */
	private boolean success;

	/** login message */
	private String info;

	/** response url */
	private String url;

	/** Login was successful. */
	byte LOGIN_OK = 1;
	/** Login failed. */
	byte LOGIN_FAILED = 2;

	/**
	 * Creates a new <code>LoginResult</code> from the data in the xml string.
	 * If <code>xmlString</code> does not contain valid data, the login result
	 * is set to not succeeded and the message contains a general error message.
	 * 
	 * @param xmlString
	 *            the string containing data in XML format.
	 */
	public LoginMessage(String xmlString) {
		XMLElement element = new XMLElement();

		try {
			element.parseString(xmlString.trim());
			byte id = (byte) element.getAttributeInt("success", LOGIN_FAILED);
			setSuccess(id == LOGIN_OK); // set to true, if login was ok
			setInfo(element.getAttribute("info"));
			setUrl(element.getAttribute("url"));
		} catch (XMLParseException xex) {
			setSuccess(false);
			setInfo("Reply had errors.");
		}
	}
	
	/**
	 * Creates a new <code>LoginResult</code> from the given properties.
	 * 
	 * @param succeeded
	 *            <code>true</code> if the login process succeeded, otherwise
	 *            <code>false</code>.
	 * @param url
	 *            the new url to load.
	 * @param msg
	 *            the message with additional information.
	 */
	public LoginMessage(boolean succeeded, String msg, String url) {
		setSuccess(succeeded);
		setInfo(msg);
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
		if (success) {
			element.setAttribute("success", Integer.toString(LOGIN_OK));
			// only write URL if success == true
			element.setAttribute("url", url);
		} else {
			element.setAttribute("success", Integer.toString(LOGIN_FAILED));
		}
		element.setAttribute("info", info);

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
	public String getInfo() {
		return info;
	}

	/**
	 * Shows success or failure of the login process.
	 * 
	 * @return <code>true</code> if the process succeeded, otherwise
	 *         <code>false</code>.
	 */
	public boolean isSuccess() {
		return success;
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
	 * Sets the information string.
	 * 
	 * @param string
	 *            the message to set.
	 */
	public void setInfo(String string) {
		info = string;
	}

	/**
	 * Sets the succeeded value.
	 * 
	 * @param b
	 *            succeeded or failed.
	 */
	public void setSuccess(boolean b) {
		success = b;
	}

	/**
	 * Sets the url.
	 * 
	 * @param string
	 *            the url to set.
	 */
	public void setUrl(String string) {
		url = string;
	}

	//
	// missing IMessage implementation
	//

	/** @see IMessage#getMessage() */
	public String getMessage() {
		return getInfo();
	}

	/** @see IMessage#getId() */
	public byte getId() {
		if (success) {
			return LOGIN_OK;
		}
		return LOGIN_FAILED;
	}
}