package at.einspiel.simme.server;

import junit.framework.TestCase;
import at.einspiel.simme.server.management.ManagedGame;
import at.einspiel.simme.server.management.ManagedUser;

/**
 * Class to test Managed Game.
 * 
 * @author kariem
 */
public class ManagedGameTest extends TestCase {
   
   ManagedGame game;
   ManagedUser player1;
   ManagedUser player2;

   /** Tests ManagedGame constructor */
   public final void testManagedGameConstructor() {
      player1 = new ManagedUser();
      player1.setNick("player1");
      player2 = new ManagedUser();
      player2.setNick("player1");
      Exception ex = null;
      try {
         // should throw error => same nick names
         game = new ManagedGame(player1, player2);
      } catch (Exception e) {
         ex = e;
      }
      assertNotNull(ex);
      assertNull(game);
      
      initGame();
   }

   /** Game initialization */
   private void initGame() {
      // create players
      player1 = new ManagedUser();
      player1.setNick("player1");
      player2 = new ManagedUser();
      player2.setNick("player2");
      
      // create game
      game = new ManagedGame(player1, player2);
      assertNotNull(game);
   }
}