// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: GameSinglePlayer.java
//                  $Date: 2004/04/15 10:00:55 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client;

/**
 * A simple single player game
 * @author kariem
 */
public abstract class GameOnePlayer extends Game {

    /** 
     * Performs the move of the other player. This method has to be implemented
     * in subclasses in order to perform any computer move or to simulate a
     * multiplayer game.
     */
    protected abstract void doOtherPlayersMove();

    /** @see at.einspiel.simme.client.Game#doMove(byte) */
    protected final void doMove(byte edgeIndex) {
        super.doMove(edgeIndex);
        informOtherPlayer(edgeIndex);
        doOtherPlayersMove();
    }

    /**
     * Override this method in order to inform the other player about the move.
     * E.g. send the move information over the network to the other player.
     * 
     * @param edgeIndex the edge index.
     */
    protected void informOtherPlayer(byte edgeIndex) {
        // has to be overridden by subclasses
    }
    
}
