package test.sim.net;

import java.io.IOException;

/**
 * Statusmessage.
 *  
 * @author kariem
 */
public class StatusMessage extends XmlMessage {

   /** @see XmlMessage#XmlMessage(byte[]) */
   public StatusMessage(byte[] data) throws IOException {
      super(data);
   }

   /** @see XmlMessage#XmlMessage(String) */
   public StatusMessage(String data) throws IOException {
      super(data);
   }

   /**
    * Returns the content of the statusmessage, or <code>null</code>
    *
    * @return the string that this message contained.
    */
   public String getString() {
      String message = (String) xmlElement.getAttribute("msg");
      return message;
   }

}
