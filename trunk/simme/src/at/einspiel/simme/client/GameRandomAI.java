//----------------------------------------------------------------------------
//[Simme]
//    Java Source File: GameRandomAI.java
//               $Date: 2004/09/13 15:26:53 $
//           $Revision: 1.8 $
//----------------------------------------------------------------------------
package at.einspiel.simme.client;

import java.util.Random;

import at.einspiel.simme.client.util.PersonalPrefs;
import at.einspiel.simme.client.util.PrefsException;

/**
 * Game class with random AI. This class only implements the method to perform
 * non-player moves.
 * 
 * @author kariem
 */
public class GameRandomAI extends GameOnePlayer {

	/**
	 * Creates a new instance of <code>GameRandomAI</code>. The user's name
	 * is automatically set to "Player" while the computer's name is set to
	 * "Computer".
	 */
	public GameRandomAI() {
		super();
		GameInfo info = getGameInfo();
		String playerName = "Player";
		try {
			// try to retrieve the player's name from the personal preferences
			String[] playerInfo = PersonalPrefs.getPlayerInfo();
			playerName = playerInfo[0];
		} catch (PrefsException e) {
			// ignore Exception at this point
		} catch (NullPointerException ex) {
			// can only be executed within MIDP environment .. this is for tests
		}
		info.setP1Name(playerName);
		info.setP2Name("Computer");
	}

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
		showCurrentPlayer();
		return true;
	}
}