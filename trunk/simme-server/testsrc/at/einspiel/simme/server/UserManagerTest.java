// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: UserManagerTest.java
//                  $Date: 2003/12/30 10:18:25 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Class to test the user manager.
 * 
 * @author kariem
 */
public class UserManagerTest extends TestCase {

   Map users;
   ManagedUser player1, player2, player3, player4;

   // overwrite UserManager.DEF_GAME_INTERVAL
   private static final int TIMEOUT_1_SECOND = 1000;
   private static final int TIMEOUT_5_SECONDS = 5000;
   private static final int TIMEOUT_100_MILLIS = 100;

   
   /** 
    * Creates a map of users and adds two users to this map
    * @see junit.framework.TestCase#setUp() 
    */
   protected void setUp() {
      users = new HashMap();
      player1 = new ManagedUser("player1");
      player2 = new ManagedUser("player2");
      player3 = new ManagedUser("player3");
      player4 = new ManagedUser("player4");
   }

   /** 
    * Tests automatic game initialization. 
    * execution time: ~ 1150 millis
    */
   public void testAutomaticGameInit() {
      UserManager.setFindGameInterval(TIMEOUT_1_SECOND);
      // add users to map
      users.put(player1.getNick(), player1);
      users.put(player2.getNick(), player2);

      // create manager for user map 
      UserManager mgr = UserManager.createUserManager(users);
      mgr.manage();

      // set to waiting
      player1.waitForGame();
      assertNull(player1.getGame());
      player2.waitForGame();
      assertNull(player2.getGame());

      // wait TIMEOUT 
      sleep(TIMEOUT_1_SECOND + TIMEOUT_100_MILLIS);

      assertNotNull(player1.getGame());
      assertNotNull(player2.getGame());
      assertSame(player1.getGame(), player2.getGame());

      ManagedGame game = player1.getGame();
      assertFalse(game.isRunning());

      // set player1 to playing
      player1.getUserState().setStateCategory(UserState.STATE_PLAYING);
      assertFalse(game.isRunning());

      // set player2 to playing
      player2.getUserState().setStateCategory(UserState.STATE_PLAYING);

      // both users are ready to play => game should be started
      assertTrue(game.isRunning());
   }

   /** 
    * Test correct game making 
    * execution time: ~ 5200 millis
    */
   public void testMultipleGameMaking() {
      // create manager for user map 
      UserManager mgr = UserManager.createUserManager(users);
      UserManager.setFindGameInterval(TIMEOUT_1_SECOND);
      mgr.manage();

      // add users to map
      users.put(player1.getNick(), player1);
      users.put(player2.getNick(), player2);
      users.put(player3.getNick(), player3);
      users.put(player4.getNick(), player4);

      // no game yet
      assertNull(player1.getGame());
      assertNull(player2.getGame());
      assertNull(player3.getGame());
      assertNull(player4.getGame());

      // set to wait and sleep one second
      player1.waitForGame();
      sleep(TIMEOUT_1_SECOND);
      // same with other users
      player2.waitForGame();
      sleep(TIMEOUT_1_SECOND);
      player3.waitForGame();
      sleep(TIMEOUT_1_SECOND);
      sleep(TIMEOUT_1_SECOND);
      player4.waitForGame();
      sleep(TIMEOUT_1_SECOND);
      
      // sleep until player4 has a game
      while (true) {
         if (player4.getGame() != null) {
            break;
         }
         sleep(TIMEOUT_100_MILLIS);
      }
      
      // every player has a game.
      assertNotNull(player1.getGame());
      assertNotNull(player2.getGame());
      assertNotNull(player3.getGame());
      assertNotNull(player4.getGame());
      
      // player1 vs player2
      assertSame(player1.getGame(), player2.getGame());
      // player3 vs player4
      assertSame(player3.getGame(), player4.getGame());
   
      // game "p1 vs p2" started before "p3 vs p4"
      assertTrue(player1.getGame().getDate().before(player3.getGame().getDate()));
   }

   private void sleep(long millis) {
      try {
         Thread.sleep(millis);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
