// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: GameSinglePlayer.java
//                  $Date: 2004/09/14 22:29:49 $
//              $Revision$
// ----------------------------------------------------------------------------
package at.einspiel.simme.client;

import at.einspiel.simme.client.messages.Messages;
import at.einspiel.simme.nanoxml.XMLElement;

/**
 * A simple single player game
 * @author kariem
 */
public abstract class GameOnePlayer extends Game {

	// by default the controlled player is player 1
	private byte controlledPlayer = PLAYER1;

	/**
	 * Creates a new instance of <code>GameOnePlayer</code>.
	 */
	public GameOnePlayer() {
		super();
	}

	/**
	 * Creates a new instance of <code>GameOnePlayer</code>.
	 * @param xmlInfo
	 *            the meta information in XML.
	 */
	public GameOnePlayer(XMLElement xmlInfo) {
		super(xmlInfo);
	}

	/**
	 * Sets the controlled player. This should be either
	 * {@linkplain Game#PLAYER1}or {@linkplain Game#PLAYER2}.
	 * 
	 * @param controlledPlayer
	 *            The controlledPlayer to set.
	 * @throws IllegalArgumentException
	 *             if the argument does not match {@linkplain Game#PLAYER1}or
	 *             {@linkplain Game#PLAYER2}.
	 */
	public void setControlledPlayer(byte controlledPlayer) throws IllegalArgumentException {
		if (controlledPlayer != PLAYER1 && controlledPlayer != PLAYER2) {
			throw new IllegalArgumentException("Argument 'controlledPlayer' is incorrect.");
		}
		this.controlledPlayer = controlledPlayer;
	}

	/**
	 * Starts the game. If the current player does not match the controlled
	 * player, the game waits for the opponent to perform the move.
	 * @see at.einspiel.simme.client.Game#start(boolean)
	 */
	public void start(boolean playerOneFirst) {
		super.start(playerOneFirst);

		// if it is not the user's turn, then call doOtherPlayersMove
		if (getPlayersTurn() != controlledPlayer) {
			doOtherPlayersMove();
		}
	}

	/**
	 * Selects the node after verifiying, that the current user may select this
	 * node.
	 * 
	 * @param index
	 *            the node index to be selected
	 * @return whether the selection was successfull. Will return
	 *         <code>false</code>, if the controlled player is not currently
	 *         on turn.
	 * @see at.einspiel.simme.client.Game#selectNode(byte)
	 */
	public boolean selectNode(byte index) {
		if (getPlayersTurn() != controlledPlayer) {
			setMoveMessage(Messages.getString("game.move-opponent"));
			return false;
		}
		return super.selectNode(index);
	}

	/**
	 * Performs the move of the other player. This method has to be implemented
	 * in subclasses in order to perform any computer move or to simulate a
	 * multiplayer game.
	 */
	protected abstract void doOtherPlayersMove();

	/** @see at.einspiel.simme.client.Game#doMove(byte) */
	protected final void doMove(byte edgeIndex) {
		super.doMove(edgeIndex);
		// send move to opponent
		setMoveMessage(Messages.getString("game.move-send"));
		informOtherPlayer(edgeIndex);
		if (!gameOver) {
			// receive move from opponent
			setMoveMessage(Messages.getString("game.move-receive"));
			doOtherPlayersMove();
		}
	}

	/**
	 * Override this method in order to inform the other player about the move.
	 * E.g. send the move information over the network to the other player.
	 * 
	 * @param edgeIndex
	 *            the edge index.
	 * @return whether the other player was successfully informed.
	 */
	protected abstract boolean informOtherPlayer(byte edgeIndex);

}