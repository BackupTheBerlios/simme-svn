// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: NetworkGame.java
//                  $Date: 2004/04/15 10:00:55 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import at.einspiel.simme.client.GameOnePlayer;

/**
 * A networked game. After executing a move, the move is sent over the network.
 * The game does not allow playing, before the opponent's move is received.
 * 
 * @author kariem
 */
public class NetworkGame extends GameOnePlayer {

    /** @see GameOnePlayer#doOtherPlayersMove() */
    protected void doOtherPlayersMove() {
        // TODO wait for receipt of other move and show it in the game
    }
    
    /** @see at.einspiel.simme.client.GameOnePlayer#informOtherPlayer(byte) */
    protected void informOtherPlayer(byte edgeIndex) {
        // TODO create play message and send to other player
        super.informOtherPlayer(edgeIndex);
    }
}
