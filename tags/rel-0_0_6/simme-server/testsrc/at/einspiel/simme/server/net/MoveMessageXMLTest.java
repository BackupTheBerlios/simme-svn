// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: MoveMessageXMLTest.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.net;

import junit.framework.TestCase;
import at.einspiel.simme.client.net.MoveMessage;

/**
 * Class to test the move message (xml).
 * 
 * @author kariem
 */
public class MoveMessageXMLTest extends TestCase {

   /** Tests get message */
   public final void testGetMessage() {
      byte move = 2;
      String info = "text";
      // create new message with constructor
      MoveMessage mmsg = new MoveMessageXML(move, "text");
      assertTrue(mmsg.getMove() == move);
      assertTrue(mmsg.getId() == 0);
      assertEquals(info, mmsg.getInfo());
      
      // output of mmsg to mmsg2
      MoveMessage mmsg2 = new MoveMessage(mmsg.getMessage());
      assertTrue(mmsg2.getMove() == move);
      assertTrue(mmsg.getId() == 0);
      assertEquals(info, mmsg2.getInfo());
   }
}
