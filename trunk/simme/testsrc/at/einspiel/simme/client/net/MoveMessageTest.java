// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: MoveMessageTest.java
//                  $Date: 2003/12/30 23:05:29 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import junit.framework.TestCase;

/**
 * Class to test the move message.
 * @author kariem
 */
public class MoveMessageTest extends TestCase {


   /** Test move message constructor */
   public final void testMoveMessageString() {
      final String TEST_STRING1 = "<move move='2'/>";
      final String TEST_STRING2 = "<move move='2' id='0'/>";
      final String TEST_STRING3 = "<move move='2' info='text'/>";
      
      MoveMessage m1 = new MoveMessage(TEST_STRING1);
      MoveMessage m2 = new MoveMessage(TEST_STRING2);
      MoveMessage m3 = new MoveMessage(TEST_STRING3);

      // check move
      assertTrue(m1.getMove() == 2);
      assertSame(m1.getMove(), m2.getMove(), m3.getMove());

      // check id
      assertTrue(m1.getId() == 0);
      assertSame(m1.getId(), m2.getId(), m3.getId());

      // check info
      assertNull(m1.getInfo());
      assertNull(m2.getInfo());
      assertEquals("text", m3.getInfo());
   }

   void assertSame(int a, int b, int c) {
      assertTrue(a == b);
      assertTrue(a == c);
   }

   /** Test move message constructor with incorrect strings. */
   public final void testMoveMessageStringIncorrect() {
      final String TEST_STRING1 = "<move/>";
      final String TEST_STRING2 = "<move move='a' id='0'/>";
      final String TEST_STRING3 = "<move move='300' info='text'/>";
      
      MoveMessage mmsg = null;
      Exception e = null;
      
      // test string 1
      try {
         mmsg = new MoveMessage(TEST_STRING1);
      } catch (NumberFormatException ex) {
         e = ex;
      }
      assertNotNull(e);
      assertNull(mmsg);
      
      // reset exception
      e = null;
      
      // test string 2
      try {
         mmsg = new MoveMessage(TEST_STRING2);
      } catch (NumberFormatException ex) {
         e = ex;
      }
      assertNotNull(e);
      assertNull(mmsg);
      
      // reset exception
      e = null;

      // test string 3
      try {
         mmsg = new MoveMessage(TEST_STRING3);
      } catch (NumberFormatException ex) {
         e = ex;
      }
      assertNotNull(e);
      assertNull(mmsg);
      e = null;
   }
}
