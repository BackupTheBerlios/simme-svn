// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ManagedGame.java
//                  $Date: 2004/09/15 23:38:20 $
//              $Revision: 1.9 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import at.einspiel.base.User;
import at.einspiel.mgmt.IStateListener;
import at.einspiel.mgmt.StateEvent;
import at.einspiel.simme.server.net.MoveMessageXML;

/**
 * Represents a game that is managed by the server. The game will be started
 * automatically, if both players' states are set to playing.
 *
 * @see at.einspiel.simme.server.ManagedUser#isPlaying()
 *  
 * @author kariem
 */
public class ManagedGame extends ServerGame implements IStateListener {

	/**
	 * Creates a new <code>ManagedGame</code> with the given managed users.
	 * 
	 * @param p1
	 *            the first user.
	 * @param p2
	 *            the second user.
	 * @throws RuntimeException
	 *             if both users are the same.
	 */
	public ManagedGame(ManagedUser p1, ManagedUser p2) throws RuntimeException {
		super(p1, p2);
		p1.setGame(this);
		p2.setGame(this);
	}

	/**
	 * Creates a new <code>ManagedGame</code> with the given users.
	 * @param p1
	 *            the first user.
	 * @param p2
	 *            the second user.
	 * @throws Exception
	 *             if both users are the same.
	 * 
	 * @see #ManagedGame(ManagedUser, ManagedUser)
	 */
	public ManagedGame(User p1, User p2) throws Exception {
		this(new ManagedUser(p1), new ManagedUser(p2));
	}

	/**
	 * Returns the first player.
	 * @return player 1 for this game.
	 */
	public ManagedUser getPlayer1() {
		return (ManagedUser) player1;
	}

	/**
	 * Returns the second player.
	 * @return player 2 for this game.
	 */
	public ManagedUser getPlayer2() {
		return (ManagedUser) player2;
	}

	/** @see at.einspiel.simme.server.ServerGame#stopGame() */
	public void stopGame() {
		super.stopGame();
		System.out.println("Game " + getNiceId() + " has ended.");
		// remove listener from players
		getPlayer1().removeStateListener(this);
		getPlayer2().removeStateListener(this);
	}

	/** @see at.einspiel.mgmt.IStateListener#updateState(StateEvent) */
	public void updateState(StateEvent evt) {
		Object source = evt.getSource();
		if (source instanceof ManagedUser) {
			if (!isRunning()) { // ... not yet running
				// see if both users have already changed to playing state
				if (getPlayer1().isPlaying() && getPlayer2().isPlaying()) {
					// game may be started now
					System.out.println("Game " + getNiceId() + " is being started now.");
					startGame();
				}
			}
		}
	}

	/**
	 * @see at.einspiel.simme.server.ServerGame#selectEdge(at.einspiel.base.User,
	 *      byte)
	 */
	public boolean selectEdge(User u, byte edge) {
		boolean movePerformed = super.selectEdge(u, edge);
		if (movePerformed) {
			// send move message to other user.
			if (u == getPlayer1()) {
				setMoveMessage(getPlayer2(), edge);
			} else if (u == getPlayer2()) {
				setMoveMessage(getPlayer1(), edge);
			}
		}
		// return super.selectEdge(User, byte)
		return movePerformed;
	}

	/**
	 * Sets the message of the other users to the given move.
	 * @param user
	 * @param edge
	 */
	private void setMoveMessage(ManagedUser user, byte edge) {
		user.setClientMessage(new MoveMessageXML(edge));
	}
}