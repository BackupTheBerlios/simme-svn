package test.sim.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * Represents a request sent to the server.
 * 
 * @author kariem
 */
public class Request {

   /** The base url where <code>Request</code>s are sent to. */
   public static final String URL_BASE = "http://www.einspiel.at/simme/";

   private static final int RESPONSE_INITIAL_SIZE = 1024;
   private static final int RESPONSE_GROWTH_FACTOR = 512;

   private HttpConnection c;
   private InputStream is;
   private OutputStream os;

   String text;

   private byte[] response;

   /**
    * Creates a new <code>Request</code>. The text has to be set manually.
    */
   public Request() {
      this("");
   }

   /**
    * Creates a new <code>Request</code> with the given text.
    * 
    * @param text The text for this request
    */
   public Request(String text) {
      c = null;
      is = null;
      os = null;
      this.text = text;
   }

   /**
    * Sends this request to default server, using the specified path as url.
    * The response is saved in a byte array, which can be accessed via
    * {@link #getResponse()}.
    * 
    * @param path The path on the server ({@link #URL_BASE}).
    * 
    * @throws IOException if a problem has occured while sending the request.
    *
    * @see #sendRequest(String, String)
    */
   public void sendRequest(String path) throws IOException {
      sendRequest(URL_BASE, path);
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
    * @throws IOException if a problem has occured while sending the request.
    */
   public void sendRequest(String urlBase, String path) throws IOException {
      StringBuffer url = new StringBuffer(urlBase);
      url.append(path);

      try {
         c = getHttpConnection(url.toString());
         c.setRequestMethod(HttpConnection.POST);
         c.setRequestProperty("User-Agent", "Profile/MIDP-1.0 Configuration/CLDC-1.0");
         // TODO add content language via param
         // c.setRequestProperty("Content-Language", "en-CA"); 
         os = c.openOutputStream();

         // send request to the CGI script
         String str = "name=163748";
         byte postmsg[] = str.getBytes();
         for (int i = 0; i < postmsg.length; i++) {
            os.write(postmsg[i]);
         }
         os.flush();

         //receive response
         is = c.openDataInputStream();

         int counter = 0;
         byte current;

         response = new byte[RESPONSE_INITIAL_SIZE];
         int length = RESPONSE_INITIAL_SIZE;

         while ((current = (byte) is.read()) != -1) {
            if (counter > length - 1) {
               // update length and copy current contents into bigger array
               byte[] responseCopy = new byte[length + RESPONSE_GROWTH_FACTOR];
               System.arraycopy(response, 0, responseCopy, 0, length);
               response = responseCopy;
               length += RESPONSE_GROWTH_FACTOR;
            }
            response[counter] = current;
            counter++;
         }

         // fit response to correct size
         byte[] responseCopy = new byte[counter];
         System.arraycopy(response, 0, responseCopy, 0, counter);
         response = responseCopy;

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
    * Holds the response of this request.
    * 
    * @return the response.
    */
   public byte[] getResponse() {
      return response;
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
}
