// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: GameUndoable.java
//                  $Date: 2004/06/07 09:27:25 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client;

import java.util.Stack;

/**
 * A game that supports undoing a move. This game type is used for two-player
 * games on a single device.
 * 
 * @author kariem
 */
public class GameUndoable extends Game {

    /** for undoing edge operations*/
    private Stack undoStack;

    /** @see at.einspiel.simme.client.Game#start(boolean) */
    public void start(boolean playerOneFirst) {
        super.start(playerOneFirst);
        undoStack = new Stack();
    }

    /** @see at.einspiel.simme.client.Game#selectNode(byte) */
    public boolean selectNode(byte index) {
        if (index == -1) {
            return undo();
        }
        return super.selectNode(index);
    }

    /** @see at.einspiel.simme.client.Game#setEdgeOwner(byte) */
    protected void setEdgeOwner(byte index) throws ArrayIndexOutOfBoundsException {
        super.setEdgeOwner(index);
        undoStack.push(new Byte(index));
    }

    /**
     * Undoes the last move.
     * @return whether the undo was possible.
     */
    protected boolean undo() {
        if (gameOver) {
            return false;
        }
        
        // else undo
        if (undoTurn()) {
            setMoveMessage("Undo successful!");

            return true;
        } 
        
        setMoveMessage("Undo not possible!");
        
        return false;
    }

    /**
     * Undos last edge operation.
     * @return whether undo has been performed.
     */
    private boolean undoTurn() {
        byte edgeIdx = -1;

        if (!undoStack.empty()) {
            moveNr--;

            edgeIdx = ((Byte) undoStack.pop()).byteValue();
            // reset edge to neutral
            setEdgeOwner(edgeIdx, NEUTRAL);
            // activate surrounding nodes
            enableEdge(edgeIdx, true);

            switchPlayers();

            return true;
        } 
        
        return false;
    }
}