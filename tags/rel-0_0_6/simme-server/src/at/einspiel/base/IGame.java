// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IGame.java
//                  $Date: 2004/09/13 15:12:58 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.base;

import java.util.Date;

import at.einspiel.simme.client.Game;
import at.einspiel.simme.client.Move;

/**
 * Interface to represent a generic game. It is designed for games with two
 * players.
 * @author kariem
 */
public interface IGame {
   /**
    * Sets the players for this game.
    * @param p1 player 1.
    * @param p2 player 2.
    */
   void setPlayers(User p1, User p2);

   /** Starts the game */
   void startGame();

   /** Stops the game */
   void stopGame();

   /** Cancels this game. */
   void cancelGame();

   /**
    * Returns the length of this game in ms
    *
    * @return The length of this game.
    */
    long getLength();

   /**
    * Returns the date of the game. The accuracy is in seconds.
    * @return The date, of this game.
    */
    Date getDate();

   /**
    * Sets the date of this game. All information more exact than seconds will
    * be dropped.
    * @param d The new date.
    */
    void setDate(Date d);

   /**
    * Returns whether the game is currently running.
    * 
    * @return <code>true</code> if the game is running.
    */
    boolean isRunning();

   /**
    * Returns the game's id.
    * @return the id of the game.
    */
    String getId();

   /**
    * Returns the current move number.
    * @return the current move number.
    */
    byte getMoveNr();

   /**
    * Performs the given move for a user.
    * @param u the user.
    * @param m the move to perform.
    * @return the move's result.
    */
    Result makeMove(User u, Move m);

   /**
    * Performs the given move.
    * 
    * @param m the move to perform.
    * @return the move's result.
    */
    Result makeMove(Move m);

   /**
    * Returns whether it is the given user's turn to play.
    * @param user the user.
    * @return <code>true</code> if the game is running and it is the given
    *          user's turn. <code>false</code> if the game is not running or
    *          has already ended, or the given user is not allowed to make the
    *          next move.
    */
    boolean isPlayerOnTurn(User user);
   
    /**
     * Returns the game object.
     * @return the game object.
     */
    Game getGame();
   
}