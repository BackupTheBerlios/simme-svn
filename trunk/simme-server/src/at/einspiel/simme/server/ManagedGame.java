// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ManagedGame.java
//                  $Date: 2003/12/30 10:18:25 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import at.einspiel.base.User;
import at.einspiel.mgmt.StateListener;
import at.einspiel.mgmt.StateEvent;

/**
 * Represents a game that is managed by the server.
 * 
 * @author kariem
 */
public class ManagedGame extends ServerGame implements StateListener {

   /**
    * Creates a new <code>ManagedGame</code> with the given managed users.
    * 
    * @param p1 the first user.
    * @param p2 the second user.
    * @throws RuntimeException if both users are the same.
    */
   public ManagedGame(ManagedUser p1, ManagedUser p2) throws RuntimeException {
      super(p1, p2);
      p1.setGame(this);
      p2.setGame(this);
   }

   /**
    * Creates a new <code>ManagedGame</code> with the given users.
    * @param p1 the first user.
    * @param p2 the second user.
    * @throws Exception if both users are the same.
    * 
    * @see #ManagedGame(ManagedUser, ManagedUser)
    */
   public ManagedGame(User p1, User p2) throws Exception {
      this(new ManagedUser(p1), new ManagedUser(p2));
   }

   ManagedUser getPlayer1() {
      return (ManagedUser) player1;
   }

   ManagedUser getPlayer2() {
      return (ManagedUser) player2;
   }

   /** @see at.einspiel.simme.server.ServerGame#stopGame() */
   public void stopGame() {
      super.stopGame();
      // remove listener from players
      getPlayer1().removeStateListener(this);
      getPlayer2().removeStateListener(this);
   }

   /** @see at.einspiel.mgmt.StateListener#updateState(StateEvent) */
   public void updateState(StateEvent evt) {
      Object source = evt.getSource();
      if (source instanceof ManagedUser) {
         if (!isRunning()) { // ... not yet running
            // see if both users have now changed to playing state
            if (getPlayer1().isPlaying() && getPlayer2().isPlaying()) {
               // game may be started
               startGame();
            }
         }
      }
   }

}