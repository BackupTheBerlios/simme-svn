package at.einspiel.simme.server.tests;

import junit.framework.TestCase;
import at.einspiel.simme.server.management.ManagedUser;
import at.einspiel.simme.server.management.SessionManager;

/**
 * @author kariem
 *
 */
public class SessionManagerTest extends TestCase {

   private SessionManager sMgr;

   private String[] nicks = { "one", "two", "three", "four", "five", "six" };
   private ManagedUser[] users;

   /** @see TestCase#setUp() */
   protected void setUp() {
      SessionManager.setMaxSecondsIdle(1);
      SessionManager.setMaxSecondsWaiting(2);
      SessionManager.setUpdateInterval(100);
      sMgr = SessionManager.getInstance();
   }

   /**
    * Tests adding and removal.
    */
   public void testAddingAndRemoval() {
      printTitle("addingAndRemoval");
      for (int i = 0; i < nicks.length; i++) {
         sMgr.addUser(nicks[i]);
      }
      assertEquals(true, sMgr.removeUser(nicks[0]));
   }

   // TODO add some users and don't let them respond
   /**
    * Tests if not responding users are kicked away
    */
   public void testAddNotRespondingUsers() {
      printTitle("addNotRespondingUsers");
      users = new ManagedUser[6];

      // add 6 users
      for (int i = 0; i < nicks.length; i++) {
         users[i] = sMgr.addUser(nicks[i]);
      }
      
      // 6 users in management
      assertEquals(6, sMgr.getNumberOfUsers());
      
      
      // wait for more than a second
      try {
         System.out.println("[" + System.currentTimeMillis() + "] starting sleep");
         Thread.sleep(1500);
         System.out.println("[" + System.currentTimeMillis() + "] sleep finished");
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      
      // update two users, set "five" to WAITING
      users[4].setState(ManagedUser.STATE_WAITING);      
      users[3].update();      
      
      
      // wait again for more than a second (sum > 2 seconds for non-updated users)
      try {
         System.out.println("[" + System.currentTimeMillis() + "] starting sleep");
         Thread.sleep(1500);
         System.out.println("[" + System.currentTimeMillis() + "] sleep finished");
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      // after more than 2 seconds users should be removed
      assertEquals(2, sMgr.getNumberOfUsers());

      // update both remaining users
      users[4].update();
      users[3].update();
      
      // wait for more than two seconds
      try {
         System.out.println("[" + System.currentTimeMillis() + "] starting sleep");
         Thread.sleep(2100);
         System.out.println("[" + System.currentTimeMillis() + "] sleep finished");
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      // only waiting user ("five") left
      assertEquals(1, sMgr.getNumberOfUsers());
   }

   // TODO add some users that are not in database

   // TODO add some users with wrong addresses

   private void printTitle(String s) {
      System.out.println("-----------" + s + "-----------");
   }

}
