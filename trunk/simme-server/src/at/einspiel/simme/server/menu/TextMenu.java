// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: TextMenu.java
//                  $Date: 2004/02/21 23:03:13 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A simple text menu containing only a single message. There are two possible
 * formats:
 *  
 * <pre>
 *   &lt;text title="${text}" 
 *         id="${id}" 
 *         msg="${text}"/&gt;
 * </pre>
 * 
 * which is used for short messages and
 * 
 * <pre>
 *   &lt;text title="${text}" id="${id}"&gt;
 *       &lt;msg&gt;${text}&lt;/msg&gt;
 *   &lt;/text&gt;
 * </pre>
 * 
 * for longer messages.
 * 
 * @author kariem
 */
class TextMenu extends AbstractMenu implements IMenu {

   static final String STR_MESSAGE = "msg";
   static final String TAG_NAME = "text";

   String xml;

   /**
    * Creates a new instance of <code>TextMenu</code>.
    * @param e an xml element.
    */
   TextMenu(Element e) {
      super(e);
      boolean longMessage = false;
      // first get attribute "msg"
      String message = e.getAttribute(STR_MESSAGE);

      if (message == null || message.trim().length() == 0) {
         // no such attribute => retrieve text of msg element
         longMessage = true;
         Element elementMsg = (Element) e.getElementsByTagName(STR_MESSAGE)
               .item(0);
         NodeList children = elementMsg.getChildNodes();
         for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(0);
            if (n.getNodeType() == Node.TEXT_NODE) {
               // assign text of text node to message
               message = n.getNodeValue().trim();
               break;
            }
         }

         // tokenize ... retain "\n"
         StringTokenizer strTok = new StringTokenizer(message, " \t\r\f");
         StringBuffer result = new StringBuffer();
         // concatenate
         while (strTok.hasMoreElements()) {
            result.append(strTok.nextToken());
            result.append(' ');
         }
         message = result.toString().trim();
         // replace single newlines with spaces
         message = message.replaceAll("\n ", " ");
         // remaining newlines are converted into double newlines.
         message = message.replaceAll("\n ", "\n\n");
      }

      // build xml representation
      StringBuffer xmlBuf = new StringBuffer(createXMLStart(TAG_NAME, false));
      if (!longMessage) {
         xmlBuf.append(" ");

         // attribute msg
         xmlBuf.append(STR_MESSAGE);
         xmlBuf.append("=\"");
         // value
         xmlBuf.append(message);
         // close attribute and text element
         xmlBuf.append("\"/>");
      } else {
         xmlBuf.append(">");

         //element msg
         // open element
         xmlBuf.append("<");
         xmlBuf.append(STR_MESSAGE);
         xmlBuf.append(">");
         // element content
         xmlBuf.append(message);
         // close element
         xmlBuf.append("</");
         xmlBuf.append(STR_MESSAGE);
         xmlBuf.append(">");
         
         // close text element
         xmlBuf.append("</");
         xmlBuf.append(TAG_NAME);
         xmlBuf.append(">");
      }
      xml = xmlBuf.toString();
   }

   /** @see at.einspiel.simme.server.menu.IMenu#getXml() */
   public String getXml() {
      return xml;
   }
}