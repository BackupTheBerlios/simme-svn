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

   /** The base url where <code>Request</code>s are sent to. */
   public static final String URL_BASE = "http://www.einspiel.at/simme/";

   private static final int RESPONSE_INITIAL_SIZE = 1024;
   private static final int RESPONSE_GROWTH_FACTOR = 512;

   private HttpConnection c;
   private InputStream is;
   private OutputStream os;
   private Hashtable params;

   String text;

   ConnectionThread sender;

   private byte[] response;

   /**
    * Creates a new <code>Request</code>. The text has to be set manually.
    */
   public Request() {
      this(null);
   }

   /**
    * Creates a new <code>Request</code> with the given text.
    * 
    * @param parameters the parameters for this request.
    */
   public Request(Hashtable parameters) {
      c = null;
      is = null;
      os = null;
      this.params = parameters;
      if (params == null) {
         params = new Hashtable();
      }
   }

   /**
    * Sets a parameter of this request.
    * 
    * @param name the name.
    * @param value the value.
    */
   public void setParam(String name, String value) {
      params.put(name, value);
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
    * {@link #getResponse()}. The default type is post.
    * 
    * @param urlBase the server address (plus protocol, port, ...).
    * 
    * @param path The path on the server identified by <code>urlBase</code>.
    * 
    * @throws IOException if a problem has occured while sending the request.
    * 
    * @see #sendRequest(String, String, boolean)
    */
   public void sendRequest(String urlBase, String path) throws IOException {
      sendRequest(urlBase, path, true);
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
    *
    * @throws IOException if a problem has occured while sending the request.
    */
   public void sendRequest(String urlBase, String path, boolean post) throws IOException {
      sender = new ConnectionThread(urlBase, path, post, Thread.currentThread());
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
    */
   public synchronized byte[] getResponse() {
      if (sender != null) {
         System.out.println("sender not null");
         if (sender.isStarted()) {
            System.out.println("sender is started");
            while (sender.isAlive()) {
               System.out.println("sender is alive");
               try {
                  wait(10000);
               } catch (InterruptedException e) {
                  ;
               }
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
   private synchronized void setResponse(byte[] response) {
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

   private class ConnectionThread extends Thread {
      boolean post;
      StringBuffer url;
      boolean started;
      Thread caller;

      /**
       * @param urlBase the server address (plus protocol, port, ...).
       * 
       * @param path The path on the server identified by <code>urlBase</code>.
       * 
       * @param post <code>true</code> if it should be a post, otherwise a
       *        get will be executed.
       */
      public ConnectionThread(String urlBase, String path, boolean post, Thread caller) {
         url = new StringBuffer(urlBase);
         url.append(path);
         if (!post) {
            url.append(getParamString(false));
         }
         this.post = post;
         this.caller = caller;
      }

      public synchronized void start() {
         started = true;
         super.start();
      }

      public void run() {
         System.out.println("running connectionthread");
         try {
            sendRequest(post);
         } catch (IOException e) {
            // TODO do something here

         } finally {
            System.out.println("stopping connectionthread");
         }
      }

      private void sendRequest(boolean post) throws IOException {
         try {
            c = getHttpConnection(url.toString());

            c.setRequestMethod(HttpConnection.POST);
            c.setRequestProperty("IF-Modified-Since", "25 Nov 2001 15:17:19 GMT");
            c.setRequestProperty("User-Agent","Profile/MIDP-1.0 Configuration/CLDC-1.0");
            c.setRequestProperty("Content-Language", "en-CA");
            c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
     
            os = c.openOutputStream();
            os.write(("name=test").getBytes());
            os.flush();

            is = c.openDataInputStream();
            
            /*
            int ch;
            while ((ch = is.read()) != -1) {
             b.append((char) ch);
             System.out.print((char)ch);
            }
            t = new TextBox("Date", b.toString(), 1024, 0);
            t.setCommandListener(this);
            */
            
            setResponse(new byte[] {});
            /*
            c.setRequestProperty("User-Agent", "Profile/MIDP-1.0 Configuration/CLDC-1.0");
            // TODO add content language via param
            // c.setRequestProperty("Content-Language", "en-CA");

            if (post) {
               c.setRequestMethod(HttpConnection.POST);
               String postString = getParamString(true);
               System.out.println("postString: " + postString);
               if (postString.length() > 0) {
                  os = c.openOutputStream();
                  os.write(postString.getBytes());
                  os.flush();
                  //os.close();

               }
            } else {
               c.setRequestMethod(HttpConnection.GET);
            }

            int rc = c.getResponseCode();
            if (rc != HttpConnection.HTTP_OK) {
               throw new IOException("Response code: " + rc);
            }

            //receive response
            is = c.openDataInputStream();

            int counter = 0;
            byte current;

            byte[] localResponse = new byte[RESPONSE_INITIAL_SIZE];
            int length = RESPONSE_INITIAL_SIZE;

            while ((current = (byte) is.read()) != -1) {
               if (counter > length - 1) {
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

            setResponse(localResponse);*/
            
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
       * @return
       */
      public boolean isStarted() {
         return started;
      }

   }
}
