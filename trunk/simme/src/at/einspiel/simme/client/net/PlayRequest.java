// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: PlayRequest.java
//                  $Date: 2004/09/13 23:38:46 $
//              $Revision: 1.9 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import at.einspiel.logging.Logger;
import at.einspiel.messaging.IConstants;
import at.einspiel.messaging.Request;
import at.einspiel.simme.client.Move;

/**
 * <p>
 * This request is used to transfer move information from the client to the
 * server. It contains two internal request classes that handle information
 * transfer. They inherit from {@link at.einspiel.messaging.Request}.
 * </p>
 * 
 * <p>
 * A single play request should be created for one game. All local and remote
 * moves are communicated through the play request. By calling {@link
 * #sendMove(Move)} the move is sent to the server.
 * 
 * @author kariem
 */
public class PlayRequest {

	static final int TIMEOUT_MOVE_SEND = 4000; // four seconds
	static final int TIMEOUT_MOVE_RECEIVE = 10000; // ten seconds
	static final int WAIT_MOVE_RECEIVE = 2000; // two seconds

	String id;
	String path;
	String nickname;

	String message;

	MoveSender moveSender;
	MoveReceiver moveReceiver;
	private final int moveSendTimeout;
	private final int moveReceiveTimeout;

	PlayRequest(String id, String nickname, String path, int sendTimeout, int receiveTimeout) {
		this.id = id;
		this.path = path;
		this.nickname = nickname;

		this.moveSendTimeout = sendTimeout;
		this.moveReceiveTimeout = receiveTimeout;

		moveSender = new MoveSender(this);
		moveReceiver = new MoveReceiver(this);
	}

	/**
	 * Creates a request that is used to transfer move information from the
	 * client to the server and back again.
	 * 
	 * @param id
	 *            the game id.
	 * @param nickname
	 *            the name of the player.
	 * @param path
	 *            the location where to send the information to.
	 */
	public PlayRequest(String id, String nickname, String path) {
		this(id, nickname, path, TIMEOUT_MOVE_SEND, TIMEOUT_MOVE_RECEIVE);
	}

	/**
	 * Sends the move <code>m</code> to the server. The return value indicates
	 * whether the server has accepted the move. Additionally the message field
	 * may be set with an optional information message.
	 * 
	 * @param m
	 *            the move to send.
	 * @return <code>true</code> if the server has accepted the move,
	 *         <code>false</code> otherwise. This method also returns false,
	 *         if there were connection problems.
	 */
	public boolean sendMove(Move m) {
		Logger.debug(getClass(), "sending move " + m.getEdge() + " for " + nickname);
		boolean sent = moveSender.sendMove(m);
		message = moveSender.getMessage();
		moveSender.reset();
		return sent;
	}

	/**
	 * <p>
	 * Returns the last move from the server performed by the other player.
	 * Additionally the message field may be set with an optional information
	 * message.
	 * </p>
	 * 
	 * <p>
	 * If the move cannot be received within a specified timeout, the request
	 * will be resent after a short break.
	 * </p>
	 * 
	 * @return the last move from the server performed by the other player or
	 *         <code>null</code> if the move could not be received due to
	 *         network problems. In the latter case, the reason is indicated by
	 *         {@linkplain #getMessage()}.
	 */
	public Move receiveMove() {
		Logger.debug(getClass(), "trying to receive move for " + nickname);
		Move m = moveReceiver.getMove();
		if (m == null) {
			Logger.debug(getClass(), "received move was null");
		} else {
			Logger.debug(getClass(), "received move: " + m.getEdge() + " for " + nickname);
		}
		message = moveReceiver.getMessage();
		moveReceiver.reset();
		return m;
	}

	/**
	 * Returns the message associated with the last operation performed on this
	 * object. This field does not necessarily have to hold a value.
	 * 
	 * @return the last message.
	 */
	public String getMessage() {
		return message;
	}

	/** Resets this PlayRequest */
	public void reset() {
		moveSender = new MoveSender(this);
		moveReceiver = new MoveReceiver(this);
		message = null;
	}

	Request getNewRequest(int timeout) {
		return new Request(timeout);
	}

	int getMoveSendTimeout() {
		return moveSendTimeout;
	}

	int getMoveReceiveTimeout() {
		return moveReceiveTimeout;
	}

	/**
	 * Sets the parameters for the given request.
	 * @param r the request to set the parameters on.
	 */
	protected void setParams(Request r) {
		r.setParam(IConstants.PARAM_USER, nickname);
		r.setParam(IConstants.PARAM_GAMEID, id);
	}
}