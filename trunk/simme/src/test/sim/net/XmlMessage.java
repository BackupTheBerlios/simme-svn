package test.sim.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import nanoxml.XMLElement;
import test.sim.util.StringReader;

/**
 * A message in XML format.
 * 
 * @author kariem
 */
public class XmlMessage {

   XMLElement xmlElement;

   /** The request object used to transmit information */
   protected Request request;

   /**
    * Creates a new XmlMessage from the given byte data.
    * 
    * @param data a byte array representing the data for this message.
    * 
    * @throws IOException see {@link XMLElement#parseFromReader(Reader)}.
    * 
    * @see #XmlMessage(String)
    */
   public XmlMessage(byte[] data) throws IOException {
      this(new String(data));
   }

   /**
    * Creates a new <code>XmlMessage</code> from the given string data.
    *
    * @param data a String representing the data for this message.
    * 
    * @throws IOException see {@link XMLElement#parseFromReader(Reader)}.
    */
   public XmlMessage(String data) throws IOException {
      xmlElement = new XMLElement();
      if ((data == null) || (data.length() == 0)) {
         throw new IOException("Supplied String is empty");
      }
      xmlElement.parseFromReader(new StringReader(data));
      request = new Request();
   }

   /**
    * Writes the contents of this message to a writer.
    * 
    * @param writer The writer that should receive the output
    * 
    * @throws IOException if an error occurs while writing to the writer.
    */
   public void write(Writer writer) throws IOException {
      xmlElement.write(writer);
   }

   /**
    * Sends this XML message to the specified path. The server is
    * {@link Request#URL_BASE}.
    * 
    * @param path the path where to send the information to.
    * @throws IOException if xml data cannot be correctly written.
    */
   public void sendRequest(String path) throws IOException {
      sendRequest(Request.URL_BASE, path);
   }

   /**
    * Sends this XML message to the specified path.
    * 
    * @param urlBase the first part of the URL (e.g.
    * <i>http://www.einspiel.at/</i>).
    * 
    * @param path the path where to send the information to.
    * @throws IOException if xml data cannot be correctly written.
    */
   public void sendRequest(String urlBase, String path) throws IOException {
      ByteArrayOutputStream bas = new ByteArrayOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(bas);
      write(writer);
      writer.flush();
      //System.out.println("sending: " + bas.toString());
      request.setParam("xmldata", bas.toString());
      //System.out.println("sending: " + bas.toString());
      request.sendRequest(urlBase, path);
   }

   /**
    * Return the underlying XMLElement.
    * 
    * @return the underlying XMLElement.
    */
   public XMLElement getXmlElement() {
      return xmlElement;
   }

   /**
    * Return the response
    * 
    * @return response of the request.
    */
   public byte[] getResponse() {
      return request.getResponse();
   }
}
