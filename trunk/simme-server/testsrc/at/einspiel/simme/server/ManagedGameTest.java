package at.einspiel.simme.server;

import junit.framework.TestCase;
import at.einspiel.simme.client.Move;

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

   /** Tests playing a simple game. */
   public void testPlayGame() {
      initGame();
      boolean p1IsFirst = player1.isOnTurn();
      ManagedUser firstPlayer, secondPlayer;
      if (p1IsFirst) {
         firstPlayer = player1;
         secondPlayer = player2;
      } else {
         firstPlayer = player2;
         secondPlayer = player1;
      }
         
      // now firstPlayer is really the first player
      
      // firstPlayer selects edge "0-1"
      assertTrue(firstPlayer.makeMove(new Move(0, 1)).success());
      // no further moves by firstPlayer
      for (byte i = 0; i < Move.MAX_EDGE_INDEX; i++) {
         assertFalse(firstPlayer.makeMove(new Move(i)).success());
      }
      // secondPlayer may not make the same move
      assertFalse(secondPlayer.makeMove(new Move(0, 1)).success());

      // secondPlayer selects edge "0-2"
      assertTrue(secondPlayer.makeMove(new Move(0, 2)).success());
      // secondPlayer may not make any move
      for (byte i = 0; i < Move.MAX_EDGE_INDEX; i++) {
         assertFalse(secondPlayer.makeMove(new Move(i)).success());
      }
      // firstPlayer may not make any of the moves already performed.
      assertFalse(firstPlayer.makeMove(new Move(0, 1)).success());
      assertFalse(firstPlayer.makeMove(new Move(0, 2)).success());
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