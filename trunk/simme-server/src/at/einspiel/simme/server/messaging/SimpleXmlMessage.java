package at.einspiel.simme.server.messaging;

import at.einspiel.simme.nanoxml.XMLElement;


/**
 * A simple xml message. It has the format:
 * 
 * &lt;message msg="text" /&gt; 
 * 
 * @author kariem
 */
public class SimpleXmlMessage implements Message {

   private XMLElement xml;

   /**
    * Creates a simple message with the given string.
    * @param msg the message's text.
    */
   public SimpleXmlMessage(String msg) {
      xml = new XMLElement();
      xml.setName("message");
      xml.setAttribute("msg", msg);
   }

   /** @see Message#getMessage() */
   public String getMessage() {
      return xml.toString();
   }
}
