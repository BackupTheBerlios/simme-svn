// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: NetworkGame.java
//                  $Date: 2004/09/14 22:30:38 $
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import at.einspiel.logging.Logger;
import at.einspiel.simme.client.GameInfo;
import at.einspiel.simme.client.GameOnePlayer;
import at.einspiel.simme.client.Move;
import at.einspiel.simme.nanoxml.XMLElement;

/**
 * A networked game. After executing a move, the move is sent over the network.
 * The game does not allow playing, before the opponent's move is received.
 * 
 * @author kariem
 */
public class NetworkGame extends GameOnePlayer {

	PlayRequest pr;
	final Move localMove = new Move(0);
	Move remoteMove;

	/**
	 * Creates a new instance of <code>NetworkGame</code>.
	 * @param xmlInfo
	 *            the meta information for this game.
	 * @param nick
	 *            the nick name.
	 * @param defaultPath
	 *            the path used to communicate game information, if no
	 *            information is contained in the xml info object.
	 */
	public NetworkGame(XMLElement xmlInfo, String nick, String defaultPath) {
		super(xmlInfo);
		// set the update url
		final String url = xmlInfo.getAttribute("path", defaultPath);
		// set the controlled player by comparing to the given nick name
		GameInfo info = getGameInfo();
		boolean thisClientIsPlayer1 = info.getP1Name().equals(nick);
		Logger.debug(getClass(), "this client is player1: " + thisClientIsPlayer1);
		setControlledPlayer(thisClientIsPlayer1 ? PLAYER1 : PLAYER2);
		// initialize the play request
		pr = new PlayRequest(getId(), nick, url);
	}

	/** @see GameOnePlayer#doOtherPlayersMove() */
	protected void doOtherPlayersMove() {
		Logger.debug(getClass(), "waiting for other player's move");

		// receive the next move
		remoteMove = null;
		do {
			remoteMove = pr.receiveMove();
		} while (!isAlreadySelected(remoteMove));

		// if move == null, an error has occured
		if (remoteMove == null) {
			setMoveMessage(pr.getMessage());
			return;
		}
		setEdgeOwner(remoteMove.getEdge());
		endTurn(remoteMove);
	}

	/**
	 * Indicates whether the given move is already selected by a player.
	 * 
	 * @param m
	 *            the move in question.
	 * @return <code>false</code> if the move's edge is already selected in
	 *         this game, <code>true</code> if the edge's owner is
	 *         {@linkplain at.einspiel.simme.client.Game#NEUTRAL}, or
	 *         <code>m</code> is <code>null</code>.
	 */
	private boolean isAlreadySelected(Move m) {
		if (m == null) {
			// there was an error, save pr.getMessage()
			return true;
		}
		byte selection = m.getEdge();
		if (this.getEdgeOwner(selection) != NEUTRAL) {
			return false;
		}
		return true;
	}

	/** @see at.einspiel.simme.client.GameOnePlayer#informOtherPlayer(byte) */
	protected boolean informOtherPlayer(byte edgeIndex) {
		Logger.debug(getClass(), "sending move to other player");
		// create move
		localMove.setEdge(edgeIndex);
		// send move to server by using the play request
		return pr.sendMove(localMove);
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	public boolean equals(Object obj) {
		if (obj instanceof NetworkGame) {
			return equals((NetworkGame) obj);
		}
		return false;
	}

	//
	// overridden java.lang.Object methods
	//

	/**
	 * Compares this object to another network game, and returns
	 * <code>true</code> if both games refer to the same network path and the
	 * implementation in the superclass returns <code>true</code>.
	 * 
	 * @param game
	 *            the game to be compared with.
	 * @return <code>true</code> if both games share the same network path,
	 *         and the super class' implementation returns <code>true</code>.
	 */
	public boolean equals(NetworkGame game) {
		return super.equals(game) && game.pr.path.equals(this.pr.path);
	}
}