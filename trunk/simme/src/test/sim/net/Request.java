package test.sim.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * Represents a request sent to the server.
 *
 * @author kariem
 */
public class Request {
    /** This request's default timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT = 5000;

    /** The base url where <code>Request</code>s are sent to. */
    //public static final String URL_BASE = Sim.getProperty("server.base");
    public static final String URL_BASE = "http://localhost:8080/simme/";
    private static final int RESPONSE_INITIAL_SIZE = 1024;
    private static final int RESPONSE_GROWTH_FACTOR = 512;

    /** The timeout used for this request */
    protected final int timeout;
    private Hashtable params;
    private IOException occurredException;
    private byte[] response;
    ConnectionThread sender;

    /**
     * Creates a new <code>Request</code>. The text has to be set manually.
     */
    public Request() {
        this(null, DEFAULT_TIMEOUT);
    }

    /**
     * Creates a new <code>Request</code> with the given text.
     *
     * @param parameters the parameters for this request.
     * @param timeout The timeout in milliseconds.
     */
    public Request(Hashtable parameters, int timeout) {
        this.params = parameters;

        if (params == null) {
            params = new Hashtable();
        }

        this.timeout = timeout;
    }

    /**
     * Creates a new request with the given timeout.
     *
     * @param timeout The timeout in milliseconds.
     */
    public Request(int timeout) {
        this(null, timeout);
    }

    /**
     * Sets a parameter of this request.
     *
     * @param name the name.
     * @param value the value. If value is <code>null</code>, the parameter will
     *         not be set.
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
     * @param path The path on the server ({@link #URL_BASE}).
     * @see #sendRequest(String, String)
     */
    public void sendRequest(String path) {
        sendRequest(URL_BASE, path);
    }

    /**
     * Sends this request to a server, using the specified path as url.
     * The response is saved in a byte array, which can be accessed via
     * {@link #getResponse()}. The default type is post.
     *
     * @param urlBase the server address (plus protocol, port, ...).
     *
     * @param path The path on the server identified by <code>urlBase</code>.
     *
     * @see #sendRequest(String, String, boolean)
     */
    public void sendRequest(String urlBase, String path) {
        sendRequest(urlBase, path, false);
    }

    /**
     * Sends this request to a server, using the specified path as url.
     * The response is saved in a byte array, which can be accessed via
     * {@link #getResponse()}.
     *
     * @param urlBase the server address (plus protocol, port, ...).
     *
     * @param path The path on the server identified by <code>urlBase</code>.
     *
     * @param post Whether this message should be a POST or a GET.
     */
    public void sendRequest(String urlBase, String path, boolean post) {
        sender = new ConnectionThread(urlBase, path, post);
        sender.start();
    }

    /**
     * Generates a String from the parameters.<br>
     * The format is <code>?name=value&amp;name2=value2...</code>
     *
     * @param post whether a POST-String or a GET-String should be generated.
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
     * @throws IOException if an error has occurred while getting the response.
     */
    public synchronized byte[] getResponse() throws IOException {
        if (sender != null) {
            if (sender.isStarted()) {
                while (sender.isAlive()) {
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
     * Sets the response of this request
     *
     * @return the response.
     */
    protected synchronized void setResponse(byte[] response) {
        this.response = response;
        notifyAll();
    }

    /**
     * Opens a http connection with the specified url.
     *
     * @param url The url to open.
     *
     * @return an open http connection.
     *
     * @throws IOException If some other kind of I/O error occurs.
     */
    public HttpConnection getHttpConnection(String url) throws IOException {
        return (HttpConnection) Connector.open(url);
    }

    /**
     * Returns the current error message.
     *
     * @return the current error, or <code>null</code> if no error has occurred.
     */
    public IOException getOccurredException() {
        return occurredException;
    }

    /**
     * Sets the current error.
     *
     * @param exception the exception that occurred.
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

    private class ConnectionThread extends Thread {
        private HttpConnection c;
        private InputStream is;
        private OutputStream os;
        private boolean post;
        private StringBuffer url;
        private boolean started;

        /**
         * @param urlBase the server address (plus protocol, port, ...).
         *
         * @param path The path on the server identified by <code>urlBase</code>.
         *
         * @param post <code>true</code> if it should be a post, otherwise a
         *        get will be executed.
         */
        public ConnectionThread(String urlBase, String path, boolean post) {
            c = null;
            is = null;
            os = null;
            url = new StringBuffer(urlBase);
            url.append(path);

            if (!post) {
                url.append(getParamString(false));
            }

            System.out.println("url=" + url.toString());
            this.post = post;
        }

        public synchronized void start() {
            started = true;
            super.start();
        }

        public void run() {
            try {
                sendRequest();
            } catch (IOException e) {
                setOccurredException(e);
            }
        }

        private void sendRequest() throws IOException {
            try {
                c = getHttpConnection(url.toString());

                // set user agent
                c.setRequestProperty("User-Agent", "Profile/MIDP-1.0 Configuration/CLDC-1.0");

                // set encoding to device encoding
                c.setRequestProperty(
                    "Content-Language",
                    System.getProperty("microedition.encoding"));

                if (post) {
                    c.setRequestMethod(HttpConnection.POST);

                    String postString = getParamString(true);
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
                    throw new IOException("Response: " + rc);
                }

                //receive response
                is = c.openDataInputStream();

                int counter = 0;
                byte current;

                byte[] localResponse = new byte[RESPONSE_INITIAL_SIZE];
                int length = RESPONSE_INITIAL_SIZE;

                while ((current = (byte) is.read()) != -1) {
                    if (counter > (length - 1)) {
                        // update length and copy current contents into bigger array
                        byte[] responseCopy = new byte[length + RESPONSE_GROWTH_FACTOR];
                        System.arraycopy(localResponse, 0, responseCopy, 0, length);
                        localResponse = responseCopy;
                        length += RESPONSE_GROWTH_FACTOR;
                    }

                    localResponse[counter] = current;
                    counter++;
                }

                // fit response to correct size
                byte[] responseCopy = new byte[counter];
                System.arraycopy(localResponse, 0, responseCopy, 0, counter);
                setResponse(localResponse);
            } finally {
                if (is != null) {
                    is.close();
                }

                if (os != null) {
                    os.close();
                }

                if (c != null) {
                    c.close();
                }
            }
        }

        /**
         * Shows if sending has already been started.
         *
         * @return <code>true</code> if the request has already been sent,
         * otherwise <code>false</code>.
         */
        public boolean isStarted() {
            return started;
        }
    }
}
