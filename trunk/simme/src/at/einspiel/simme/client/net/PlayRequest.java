// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: PlayRequest.java
//                  $Date: 2004/09/07 13:23:45 $
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import java.io.IOException;

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
		this.id = id;
		this.path = path;
		this.nickname = nickname;

		moveSender = new MoveSender();
		moveReceiver = new MoveReceiver();
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
	 * @return the last move from the server performed by the other player.
	 */
	public Move receiveMove() {
		Move m = moveReceiver.getMove();
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
		moveSender = new MoveSender();
		moveReceiver = new MoveReceiver();
		message = null;
	}

	/**
	 * Sends a single move to the server. The response only contains a boolean
	 * value, which indicates whether the move was accepted by the server, and
	 * an optional message.
	 */
	class MoveSender extends Request {
		String msg;

		MoveSender() {
			super(TIMEOUT_MOVE_SEND);
		}

		/**
		 * Sends the move and returns whether the move was accepted.
		 * @param m
		 *            the move to be sent.
		 * @return <code>true</code> if the move was accepted. If an error
		 *         occurs, this method returns <code>false</code> and an
		 *         optional message or cause may be retrieved by a call to
		 *         {@linkplain #getMessage()}.
		 *  
		 */
		boolean sendMove(Move m) {
			// set parameters
			setParam("gameid", id);
			setParam("nick", nickname);
			setParam("edge", new Byte(m.getEdge()).toString());

			sendRequest(path);

			try {
				// retrieve response
				String response = new String(getResponse());
				if (response.equals("OK")) { // TODO rethink
					// everything fine, move successfully sent
					return true;
				}
				// otherwise save response
				msg = response;
			} catch (IOException e) {
				// error occured - save in message
				msg = "Error: " + e.getMessage();
			}
			return false;
		}

		/** @see Request#reset() */
		public void reset() {
			super.reset();
			msg = null;
		}

		String getMessage() {
			return msg;
		}
	}

	/**
	 * Connects to the server in order to retrieve the opponents move. After a
	 * certain
	 */
	class MoveReceiver extends Request {
		boolean retry;
		boolean interrupted;
		byte retries = -1;
		byte defaultRetries = -1;
		String msg;

		/**
		 * Creates a new instance with the specified number of retries.
		 * 
		 * @param retries
		 *            if <code>0</code> or <code>&gt;0</code>, then this is
		 *            used as the number of retries. If it is set to
		 *            <code>&lt;0</code>, the receiver will reconnect until a
		 *            response is received.
		 */
		MoveReceiver(byte retries) {
			super(TIMEOUT_MOVE_RECEIVE);
			if (retries >= 0) {
				retry = true;
				this.retries = retries;
				this.defaultRetries = retries;
			}
		}

		/**
		 * Creates a standard <code>MoveReceiver</code> which tries to
		 * reconnect until an answer is retrieved.
		 */
		MoveReceiver() {
			this((byte) -1);
		}

		/** @see Request#reset() */
		public void reset() {
			super.reset();
			interrupted = false;
			msg = null;
		}

		/**
		 * Tries to receive the next move from the server
		 * @return
		 */
		Move getMove() {
			// set parameters
			setParam("nick", nickname);
			setParam("gameid", id);

			Move move = null;
			while (condition()) {
				if (retry) {
					retries--; // use retry counter
				}
				move = receiveMove();
				if (move != null) {
					retries = defaultRetries;
					break; // move received, exit loop
				}
				if (condition()) {
					pause();
				}
			}

			return move;
		}

		/**
		 * Checks whether the condition to retry is still given
		 * @return <code>true</code> if another retry is possible,
		 *         <code>false</code> otherwise.
		 */
		boolean condition() {
			if (interrupted) {
				return false;
			}
			if (retry) {
				return retries > 0;
			}
			return true;
		}

		private void pause() {
			try {
				Thread.sleep(WAIT_MOVE_RECEIVE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String getMessage() {
			return msg;
		}

		/**
		 * Receives the move as string and returns a newly built move. An
		 * optional message that holds additional information.
		 * 
		 * @return the received move.
		 */
		private Move receiveMove() {
			sendRequest(path);
			try {
				// message has been received - interrupt possible loop
				interrupted = true;
				MoveMessage mmsg = new MoveMessage(new String(getResponse()));
				// set message from response
				msg = mmsg.getInfo();

				return new Move(mmsg.getMove());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}