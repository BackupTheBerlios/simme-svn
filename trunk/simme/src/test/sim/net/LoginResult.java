package test.sim.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import nanoxml.XMLElement;
import nanoxml.XMLParseException;

/**
 * The result of a login process.
 * 
 * @author kariem
 */
public class LoginResult {

   /** The element name of the string representation */
   public static final String ELEMENT_NAME = "loginresult";

   /** login succeeded */
   private boolean succeed;
   /** login message */
   private String message;

   /**
    * Creates a new <code>LoginResult</code> from the data in the xml string. If
    * <code>xmlString</code> does not contain valid data, the login result is
    * set to not succeeded and the message contains a general error message. 
    * 
    * @param xmlString the string containing data in XML format.
    */
   public LoginResult(String xmlString) {
      XMLElement element = new XMLElement();
      try {
         element.parseString(xmlString.trim());
         setSucceed(element.getBooleanAttribute("succeed"));
         setMessage((String) element.getAttribute("msg"));
      } catch (XMLParseException xex) {
         setSucceed(false);
         setMessage("Nachricht enthielt Fehler."); 
      }
   }

   /**
    * Creates a new <code>LoginResult</code> from the given properties.
    * 
    * @param succeeded <code>true</code> if the login process succeeded,
    *        otherwise <code>false</code>.
    * @param message the message with additional information.
    */
   public LoginResult(boolean succeeded, String message) {
      setSucceed(succeeded);
      setMessage(message);
   }

   /**
    * @return an XML representation of this result or <code>null</code> if a
    *         problem occured while generating XML.
    * 
    * @see Object#toString()
    */
   public String toString() {
      XMLElement element = new XMLElement();
      element.setName(ELEMENT_NAME);
      element.setAttribute("succeed", succeed ? "1" : "0");
      element.setAttribute("msg", message);

      ByteArrayOutputStream bas = new ByteArrayOutputStream();
      OutputStreamWriter writer = new OutputStreamWriter(bas);
      try {
         element.write(writer);
         writer.flush();
      } catch (IOException e) {
         return null;
      }
      return bas.toString();
   }

   /**
    * Returns the message.
    * 
    * @return the message contained in this <code>LoginResult</code>.
    */
   public String getMessage() {
      return message;
   }

   /**
    * Shows success or failure of the login process.
    * 
    * @return <code>true</code> if the process succeeded, otherwise
    *         <code>false</code>.
    */
   public boolean isSucceed() {
      return succeed;
   }

   /**
    * Sets the message.
    * @param string the message to set.
    */
   public void setMessage(String string) {
      message = string;
   }

   /**
    * Sets the succeeded value.
    * 
    * @param b succeeded or failed.
    */
   public void setSucceed(boolean b) {
      succeed = b;
   }

}
