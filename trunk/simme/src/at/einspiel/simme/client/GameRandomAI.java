//----------------------------------------------------------------------------
//[Simme]
//    Java Source File: GameRandomAI.java
//               $Date: 2004/06/07 09:27:25 $
//           $Revision: 1.6 $
//----------------------------------------------------------------------------
package at.einspiel.simme.client;

import java.util.Random;

/**
 * Game class with random AI. This class only implements the method to perform
 * non-player moves.
 * 
 * @author kariem
 */
public class GameRandomAI extends GameOnePlayer {

    /** @see GameOnePlayer#doOtherPlayersMove() */
    protected void doOtherPlayersMove() {
        Random random = new Random();
        byte edge = (byte) Math.abs(random.nextInt() % NB_EDGES);
        while (getEdgeOwner(edge) != NEUTRAL) {
            edge = (byte) Math.abs(random.nextInt() % NB_EDGES);
        }
        setEdgeOwner(edge);

        // switch players and see if someone has won
        endTurn(new Move(edge));
    }

    /** @see at.einspiel.simme.client.GameOnePlayer#informOtherPlayer(byte) */
    protected boolean informOtherPlayer(byte edgeIndex) {
        return true;
    }
}