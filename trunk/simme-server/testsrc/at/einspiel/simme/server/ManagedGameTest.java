// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ManagedGameTest.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import java.util.*;

import junit.framework.TestCase;
import at.einspiel.simme.client.Move;
import at.einspiel.simme.server.net.MoveMessageXML;

/**
 * Class to test a managed game.
 * 
 * @author kariem
 */
public class ManagedGameTest extends TestCase {

   ManagedGame game;
   ManagedUser player1;
   ManagedUser player2;

   List moves;

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
      game.startGame();
   }

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

   /** Tests starting a game on user state change. */
   public void testStartWithStateChange() {
      // create players
      player1 = new ManagedUser();
      player1.setNick("player1");
      player2 = new ManagedUser();
      player2.setNick("player2");

      // create game
      game = new ManagedGame(player1, player2);
      assertNotNull(game);
      assertFalse(game.isRunning());

      // set player1 to playing
      player1.getUserState().setStateCategory(UserState.STATE_PLAYING);
      assertFalse(game.isRunning());

      // set player2 to playing
      player2.getUserState().setStateCategory(UserState.STATE_PLAYING);

      // both users are ready to play => game should be started
      assertTrue(game.isRunning());
   }

   /** Tests playing a simple game. */
   public void testPlayGame() {
      initGame();

      ManagedUser[] players = getGamePlayers(player1, player2);
      ManagedUser firstPlayer = players[0], secondPlayer = players[1];

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

      // firstPlayer selects edge 1-3
      assertTrue(firstPlayer.makeMove(new Move(1, 3)).success());
      // secondPlayer selects edge 1-2
      assertTrue(secondPlayer.makeMove(new Move(1, 2)).success());
      // firstPlayer selects edge 0-3 ... has formed a triangle
      assertTrue(firstPlayer.makeMove(new Move(0, 3)).success());

      // no more moves possible (for none of the players)
      for (byte i = 0; i < Move.MAX_EDGE_INDEX; i++) {
         assertNull(firstPlayer.makeMove(new Move(i)));
         assertNull(secondPlayer.makeMove(new Move(i)));
      }
   }

   /** Tests playing several games subsequently. */
   public void testPlayGamesSubsequently() {
      initGame();

      ManagedUser[] players = getGamePlayers(player1, player2);
      ManagedUser firstPlayer = players[0], secondPlayer = players[1];

      initMoves();
      while (game.isRunning()) {
         // make random moves
         assertTrue(makeRandomMove(firstPlayer));
         if (game.isRunning()) {
            assertTrue(makeRandomMove(secondPlayer));
         }
      }

      assertFalse(game.isPlayerOnTurn(firstPlayer));
      assertFalse(game.isPlayerOnTurn(secondPlayer));

      // restart game ... rotate players
      game.restartGame();
      initMoves();
      while (game.isRunning()) {
         // make random moves
         assertTrue(makeRandomMove(secondPlayer));
         if (game.isRunning()) {
            assertTrue(makeRandomMove(firstPlayer));
         }
      }

      // restart game ... rotate players
      game.restartGame();
      initMoves();
      while (game.isRunning()) {
         // make random moves
         assertTrue(makeRandomMove(firstPlayer));
         if (game.isRunning()) {
            assertTrue(makeRandomMove(secondPlayer));
         }
      }
   }

   /** Tests the creation of move messages for the other player */
   public void testMoveMessages() {
      initGame();
      ManagedUser[] players = getGamePlayers(player1, player2);
      ManagedUser firstPlayer = players[0], secondPlayer = players[1];

      initMoves();

      while (game.isRunning()) {
         // create random move
         Move m = getRandomMove();
         // perform move with first player
         firstPlayer.makeMove(m);
         // compare move with message on second player
         assertTrue(((MoveMessageXML) secondPlayer.getClientMessage())
               .getMove() == m.getEdge());
         if (game.isRunning()) {
            // create random move
            m = getRandomMove();
            // perform move with second player
            secondPlayer.makeMove(m);
            // compare move with message on first player
            assertTrue(((MoveMessageXML) firstPlayer.getClientMessage())
                  .getMove() == m.getEdge());
         }
      }
   }

   private void initMoves() {
      Move[] moveArray = new Move[Move.MAX_EDGE_INDEX + 1];
      for (byte i = 0; i <= Move.MAX_EDGE_INDEX; i++) {
         moveArray[i] = new Move(i);
      }
      moves = new ArrayList(Arrays.asList(moveArray));
   }

   private static ManagedUser[] getGamePlayers(ManagedUser p1, ManagedUser p2) {
      ManagedUser first, second;
      boolean p1IsFirst = p1.isOnTurn();
      if (p1IsFirst) {
         first = p1;
         second = p2;
      } else {
         first = p2;
         second = p1;
      }
      assertTrue(first.isOnTurn());
      assertFalse(second.isOnTurn());
      return new ManagedUser[]{first, second};
   }

   private boolean makeRandomMove(ManagedUser u) {
      return u.makeMove(getRandomMove()).success();
   }

   private Move getRandomMove() {
      Collections.shuffle(moves);
      return (Move) moves.remove(0);
   }
}