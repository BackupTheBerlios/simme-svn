// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: MoveMessageXML.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.net;

import at.einspiel.simme.client.Move;
import at.einspiel.simme.client.net.MoveMessage;
import at.einspiel.simme.nanoxml.XMLElement;

/**
 * A move message that can be serialized to XML via {@link #getMessage()}.
 * @author kariem
 */
public class MoveMessageXML extends MoveMessage {

   /**
    * Creates a new instance of <code>MoveMessageXML</code> from an XML string.
    * @param xmlString
    */
   public MoveMessageXML(String xmlString) {
      super(xmlString);
   }

   /**
    * Creates a new instance of <code>MoveMessageXML</code>.
    * @param move the move.
    * @param info the information.
    * @param id the message id.
    */
   public MoveMessageXML(byte move, String info, byte id) {
      this.move = move;
      this.info = info;
      this.id = id;
   }

   /**
    * Creates a new instance of <code>MoveMessageXML</code>.
    * @param move the move.
    * @param info the information.
    */
   public MoveMessageXML(byte move, String info) {
      this(move, info, NO_ID);
   }

   /**
    * Creates a new instance of <code>MoveMessageXML</code>.
    * @param m the move.
    */
   public MoveMessageXML(Move m) {
      this(m.getEdge());
   }
   
   /**
    * Creates a new instance of <code>MoveMessageXML</code>.
    * @param move the move.
    */
   public MoveMessageXML(byte move) {
      this(move, null);
   }

   /** 
    * Returns the MoveMessage as XML representation.
    * @see at.einspiel.simme.client.net.MoveMessage#getMessage() 
    */
   public String getMessage() {
      XMLElement xml = new XMLElement();
      xml.setName(NAME);
      xml.setAttribute(ATTR_MOVE, Byte.toString(move));
      if (id != NO_ID) {
         xml.setAttribute(ID, Byte.toString(id));
      }
      if (info != null) {
         xml.setAttribute(ATTR_INFO, info);
      }
      return xml.toString();
   }
}
