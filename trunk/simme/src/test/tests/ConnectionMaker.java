package test.tests;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.microedition.io.HttpConnection;

/**
 * This class is used to simulate a microedition.io.HttpConnection over a
 * standard java.ent.HttpURLConnection.
 * 
 * @author kariem
 */
public class ConnectionMaker implements HttpConnection {

   final HttpURLConnection conn;
   final URL url;

   /**
    * Creates a new Connection maker from a standard URLConnection.
    * 
    * @param connection the connection to use.
    */
   public ConnectionMaker(URLConnection connection) {
      conn = (HttpURLConnection) connection;
      url = connection.getURL();
   }

   /** @see javax.microedition.io.HttpConnection#getURL() */
   public String getURL() {
      return url.toString();
   }

   /** @see javax.microedition.io.HttpConnection#getProtocol() */
   public String getProtocol() {
      return url.getProtocol();
   }

   /** @see javax.microedition.io.HttpConnection#getHost() */
   public String getHost() {
      return url.getHost();
   }

   /** @see javax.microedition.io.HttpConnection#getFile() */
   public String getFile() {
      return url.getFile();
   }

   /** @see javax.microedition.io.HttpConnection#getRef() */
   public String getRef() {
      return url.getRef();
   }

   /** @see javax.microedition.io.HttpConnection#getQuery() */
   public String getQuery() {
      return url.getQuery();
   }

   /** @see javax.microedition.io.HttpConnection#getPort() */
   public int getPort() {
      return url.getPort();
   }

   /** @see javax.microedition.io.HttpConnection#getRequestMethod() */
   public String getRequestMethod() {
      return conn.getRequestMethod();
   }

   /** @see javax.microedition.io.HttpConnection#setRequestMethod(java.lang.String) */
   public void setRequestMethod(String method) throws IOException {
      conn.setRequestMethod(method);
   }

   /** @see javax.microedition.io.HttpConnection#getRequestProperty(java.lang.String) */
   public String getRequestProperty(String key) {
      return conn.getRequestProperty(key);
   }

   /** @see javax.microedition.io.HttpConnection#setRequestProperty(java.lang.String, java.lang.String) */
   public void setRequestProperty(String key, String value) throws IOException {
      conn.setRequestProperty(key, value);
   }

   /** @see javax.microedition.io.HttpConnection#getResponseCode() */
   public int getResponseCode() throws IOException {
      return conn.getResponseCode();
   }

   /** @see javax.microedition.io.HttpConnection#getResponseMessage() */
   public String getResponseMessage() throws IOException {
      return conn.getResponseMessage();
   }

   /** @see javax.microedition.io.HttpConnection#getExpiration() */
   public long getExpiration() throws IOException {
      return conn.getExpiration();
   }

   /** @see javax.microedition.io.HttpConnection#getDate() */
   public long getDate() throws IOException {
      return conn.getDate();
   }

   /** @see javax.microedition.io.HttpConnection#getLastModified() */
   public long getLastModified() throws IOException {
      return conn.getLastModified();
   }

   /** @see javax.microedition.io.HttpConnection#getHeaderField(java.lang.String) */
   public String getHeaderField(String field) throws IOException {
      return conn.getHeaderField(field);
   }

   /** @see javax.microedition.io.HttpConnection#getHeaderFieldInt(java.lang.String, int) */
   public int getHeaderFieldInt(String arg0, int arg1) throws IOException {
      return conn.getHeaderFieldInt(arg0, arg1);
   }

   /** @see javax.microedition.io.HttpConnection#getHeaderFieldDate(java.lang.String, long) */
   public long getHeaderFieldDate(String arg0, long arg1) throws IOException {
      return conn.getHeaderFieldDate(arg0, arg1);
   }

   /** @see javax.microedition.io.HttpConnection#getHeaderField(int) */
   public String getHeaderField(int arg0) throws IOException {
      return conn.getHeaderField(arg0);
   }

   /** @see javax.microedition.io.HttpConnection#getHeaderFieldKey(int) */
   public String getHeaderFieldKey(int arg0) throws IOException {
      return conn.getHeaderFieldKey(arg0);
   }

   /** @see javax.microedition.io.ContentConnection#getType() */
   public String getType() {
      return conn.getContentType();
   }

   /** @see javax.microedition.io.ContentConnection#getEncoding() */
   public String getEncoding() {
      return conn.getContentEncoding();
   }

   /** @see javax.microedition.io.ContentConnection#getLength() */
   public long getLength() {
      return conn.getContentLength();
   }

   /** @see javax.microedition.io.InputConnection#openInputStream() */
   public InputStream openInputStream() throws IOException {
      return conn.getInputStream();
   }

   /** @see javax.microedition.io.InputConnection#openDataInputStream() */
   public DataInputStream openDataInputStream() throws IOException {
      return new DataInputStream(openInputStream());
   }

   /** @see javax.microedition.io.Connection#close() */
   public void close() throws IOException {
      conn.disconnect();
   }

   /** @see javax.microedition.io.OutputConnection#openOutputStream() */
   public OutputStream openOutputStream() throws IOException {
      conn.setDoOutput(true);
      return conn.getOutputStream();
   }

   /** @see javax.microedition.io.OutputConnection#openDataOutputStream() */
   public DataOutputStream openDataOutputStream() throws IOException {
      return new DataOutputStream(openDataOutputStream());
   }

}