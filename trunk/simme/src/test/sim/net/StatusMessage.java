package test.sim.net;

import java.io.IOException;

/**
 * Statusmessage.
 *  
 * @author kariem
 */
public class StatusMessage extends XmlMessage {

   /**
    * @param data
    * @throws IOException
    */
   public StatusMessage(byte[] data) throws IOException {
      super(data);
   }

   /**
    * @param data
    * @throws IOException
    */
   public StatusMessage(String data) throws IOException {
      super(data);
   }

   /**
    * Returns the content of the statusmessage, or <code>null</code>
    * 
    *
    * @return
    */
   public String getString() {
      String message = (String) xmlElement.getAttribute("msg");
      return message;
   }

}
