// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: ConnectionThread.java
//                  $Date: $
//              $Revision: $
// ----------------------------------------------------------------------------
package at.einspiel.messaging;

import java.io.*;

import javax.microedition.io.Connection;
import javax.microedition.io.HttpConnection;

import at.einspiel.logging.Logger;

class ConnectionThread extends Thread {
	private static final int RESPONSE_INITIAL_SIZE = 1024;
	private static final int RESPONSE_GROWTH_FACTOR = 512;

	private final Request request;
	private HttpConnection c;
	private InputStream is;
	private OutputStream os;
	private boolean post;
	private StringBuffer url;
	private boolean started;
	private boolean cancelled = false;

	/**
	 * @param urlBase
	 *            the server address (plus protocol, port, ...).
	 * 
	 * @param path
	 *            The path on the server identified by <code>urlBase</code>.
	 * 
	 * @param post
	 *            <code>true</code> if it should be a post, otherwise a get
	 *            will be executed.
	 * @param request
	 *            the request used for this connection.
	 */
	public ConnectionThread(Request request, String urlBase, String path, boolean post) {
		this.request = request;
		url = new StringBuffer(urlBase);
		url.append(path);

		if (!post) {
			// append params to end of url
			url.append(this.request.getParamString(false));
		}

		this.post = post;
	}

	/** @see java.lang.Thread#start() */
	public synchronized void start() {
		started = true;
		super.start();
	}

	/** @see java.lang.Thread#run() */
	public void run() {
		try {
			sendRequest();
		} catch (IOException e) {
			this.request.setOccurredException(e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void sendRequest() throws IOException {
		try {
			Logger.debug("Connecting to " + url.toString());
			c = this.request.getHttpConnection(url.toString());

			// set user agent
			c.setRequestProperty("User-Agent", "Profile/MIDP-1.0 Configuration/CLDC-1.0");

			// set encoding to device encoding
			c.setRequestProperty("Content-Language", System.getProperty("microedition.locale"));

			if (post) {
				c.setRequestMethod(HttpConnection.POST);

				String postString = this.request.getParamString(true);
				System.out.println("posting: " + postString);

				if (postString.length() > 0) {
					os = c.openOutputStream();
					os.write(postString.getBytes());
					os.flush();
					os.close();
				}
			} else {
				c.setRequestMethod(HttpConnection.GET);
			}

			int rc = c.getResponseCode();
			if (rc != HttpConnection.HTTP_OK) {
				throw new IOException("Response: " + rc + "\nrequested URL: " + url
						+ "\n\nPlease report this error.");
			}
			if (cancelled) {
				throw new IOException("Request cancelled.");
			}

			// receive response
			is = c.openDataInputStream();
			
			// set Cookie if possible
			c.getHeaderField("Set-cookie");

			int pos = 0;
			byte current;

			byte[] localResponse = new byte[RESPONSE_INITIAL_SIZE];
			int length = RESPONSE_INITIAL_SIZE;

			// read until the end of the stream
			while (((current = (byte) is.read()) != -1)) {
				if (pos > (length - 1)) {
					// update length and copy current contents into bigger
					// array
					byte[] responseCopy = new byte[length + RESPONSE_GROWTH_FACTOR];
					length += RESPONSE_GROWTH_FACTOR;
					System.arraycopy(localResponse, 0, responseCopy, 0, length);
					localResponse = responseCopy;
				}

				localResponse[pos] = current;
				pos++;
			}

			// fit response to correct size
			byte[] responseCopy = new byte[pos];
			System.arraycopy(localResponse, 0, responseCopy, 0, pos);
			this.request.setResponse(responseCopy);
		} finally {
			close(is);
			close(os);
			close(c);
		}
	}

	private void close(OutputStream stream) throws IOException {
		if (stream != null) {
			stream.close();
		}
	}

	private void close(InputStream stream) throws IOException {
		if (stream != null) {
			stream.close();
		}
	}

	private void close(Connection conn) throws IOException {
		if (conn != null) {
			conn.close();
		}
	}

	/**
	 * Shows if sending has already been started.
	 * 
	 * @return <code>true</code> if the request has already been sent,
	 *         otherwise <code>false</code>.
	 */
	public boolean isStarted() {
		return started;
	}
}