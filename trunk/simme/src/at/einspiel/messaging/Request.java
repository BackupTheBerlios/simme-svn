//----------------------------------------------------------------------------
//[Simme]
//    Java Source File: Request.java
//               $Date: 2004/09/16 08:30:07 $
//           $Revision: 1.9 $
//----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import at.einspiel.simme.client.Sim;

/**
 * Represents a request sent to the server.
 * 
 * @author kariem
 */
public class Request {

	/** This request's default timeout in milliseconds */
	public static final int DEFAULT_TIMEOUT = 1000;

	/**
	 * The base url to where a <code>Request</code> will be sent. This should
	 * be one of
	 * <ul>
	 * <li><code>http://localhost:8080/simme/</code> (debug)</li>
	 * <li><code>http://www.einspiel.at/simme/</code></li>
	 * </ul>
	 * The field is set by the MIDlet property <code>simme.server.base</code>.
	 */
	public static final String URL_BASE = Sim.getProperty("simme.server.base");

	/** The timeout used for this request */
	protected final int timeout;
	private Hashtable params;
	private IOException occurredException;
	private byte[] response;
	ConnectionThread sender;

	private boolean cancelled;

	/**
	 * Creates a new <code>Request</code>. The text has to be set manually.
	 */
	public Request() {
		this(null, DEFAULT_TIMEOUT);
	}

	/**
	 * Creates a new <code>Request</code> with the given text.
	 * 
	 * @param parameters
	 *            the parameters for this request.
	 * @param timeout
	 *            The timeout in milliseconds.
	 */
	public Request(Hashtable parameters, int timeout) {
		init(parameters);
		this.timeout = timeout;
	}

	private void init(Hashtable parameters) {
		this.params = parameters;
		cancelled = false;

		if (params == null) {
			params = new Hashtable();
		}
	}

	/** Resets this requests parameters */
	public void reset() {
		init(null);
	}

	/**
	 * Creates a new request with the given timeout.
	 * 
	 * @param timeout
	 *            The timeout in milliseconds.
	 */
	public Request(int timeout) {
		this(null, timeout);
	}

	/**
	 * Sets a parameter of this request.
	 * 
	 * @param name
	 *            the name.
	 * @param value
	 *            the value. If value is <code>null</code>, the parameter
	 *            will not be set.
	 */
	public void setParam(String name, String value) {
		if (value != null) {
			params.put(name, value);
		}
	}

	/**
	 * Sends this request to default server, using the specified path as url.
	 * The response is saved in a byte array, which can be accessed via
	 * {@link #getResponse()}.
	 * 
	 * @param path
	 *            The path on the server prepended by ({@link #URL_BASE})
	 *            without the first "/"; e.g. <code>index.html</code>.
	 * @see #sendRequest(String, String)
	 */
	public void sendRequest(String path) {
		sendRequest(URL_BASE, path);
	}

	/**
	 * Sends this request to a server, using the specified path as url. The
	 * response is saved in a byte array, which can be accessed via
	 * {@link #getResponse()}. The default type is post.
	 * 
	 * @param urlBase
	 *            the server address (plus protocol, port, ...).
	 * 
	 * @param path
	 *            The path on the server identified by <code>urlBase</code>.
	 * 
	 * @see #sendRequest(String, String, boolean)
	 */
	public void sendRequest(String urlBase, String path) {
		sendRequest(urlBase, path, false);
	}

	/**
	 * Sends this request to a server, using the specified path as url. The
	 * response is saved in a byte array, which can be accessed via
	 * {@link #getResponse()}.
	 * 
	 * TODO verify that there is only one connector running (reuse thread)
	 * 
	 * @param urlBase
	 *            the server address (plus protocol, port, ...).
	 * 
	 * @param path
	 *            The path on the server identified by <code>urlBase</code>.
	 * 
	 * @param post
	 *            Whether this message should be a POST or a GET.
	 */
	public void sendRequest(String urlBase, String path, boolean post) {
		sender = new ConnectionThread(this, urlBase, path, post);
		sender.start();
	}

	/**
	 * Generates a String from the parameters. <br>
	 * The format is <code>?name=value&amp;name2=value2...</code>
	 * 
	 * @param post
	 *            whether a POST-String or a GET-String should be generated.
	 * @return a String that may be added to a url.
	 */
	public String getParamString(boolean post) {
		if (params == null) {
			return "";
		}

		if (params.size() == 0) {
			return "";
		}

		StringBuffer returnString = new StringBuffer();

		if (!post) {
			returnString.append("?");
		}

		Enumeration keys = params.keys();
		String name = (String) keys.nextElement();
		String value = (String) params.get(name);

		returnString.append(name);
		returnString.append("=");
		returnString.append(value);

		while (keys.hasMoreElements()) {
			name = (String) keys.nextElement();
			value = (String) params.get(name);
			returnString.append("&");
			returnString.append(name);
			returnString.append("=");
			returnString.append(value);
		}

		return returnString.toString();
	}

	/**
	 * Holds the response of this request.
	 * 
	 * @return the response.
	 * @throws IOException
	 *             if an error has occurred while getting the response.
	 */
	public synchronized byte[] getResponse() throws IOException {
		if (sender != null) {
			if (sender.isStarted()) {
				while (sender.isAlive() && !cancelled) {
					try {
						wait(timeout);
					} catch (InterruptedException e) {
						throw new IOException(e.getMessage());
					}
				}

				if (occurredException != null) {
					throw occurredException;
				}

				return response;
			}
		}
		return null;
	}

	/**
	 * Sets the response of this request and unblocks {@link #getResponse()}.
	 * 
	 * @param response
	 *            the response.
	 */
	protected synchronized void setResponse(byte[] response) {
		this.response = response;
		notifyAll();
	}

	/**
	 * Opens a http connection with the specified url.
	 * 
	 * @param url
	 *            The url to open.
	 * 
	 * @return an open http connection.
	 * 
	 * @throws IOException
	 *             If some other kind of I/O error occurs.
	 */
	public HttpConnection getHttpConnection(String url) throws IOException {
		return (HttpConnection) Connector.open(url);
	}

	/**
	 * Returns the current error message.
	 * 
	 * @return the current error, or <code>null</code> if no error has
	 *         occurred.
	 */
	public IOException getOccurredException() {
		return occurredException;
	}

	/**
	 * Sets the current error.
	 * 
	 * @param exception
	 *            the exception that occurred.
	 */
	public void setOccurredException(IOException exception) {
		occurredException = exception;
	}

	/**
	 * Returns the timeout of this request.
	 * 
	 * @return the timeout of this request in milliseconds.
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Adds user data to the request. The data currently only consists of the
	 * user name.
	 */
	public void addUserData() {
		setParam(IConstants.PARAM_USER, Sim.getNick());
	}
	
	public void cancel() {
		cancelled = true;
	}
}