// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: MoveReceiver.java
//                  $Date: 2004/09/13 23:38:26 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import java.io.IOException;

import at.einspiel.logging.Logger;
import at.einspiel.messaging.Request;
import at.einspiel.simme.client.Move;
import at.einspiel.simme.nanoxml.XMLParseException;

/**
 * Connects to the server in order to retrieve the opponents move.
 */
class MoveReceiver {
	private final PlayRequest pr;
	private final Request r;

	private String msg;

	boolean retry;
	boolean interrupted;
	// Set this to 0 for now retries to -1 for infinite replies, and to >= 1
	// for a set number of replies.
	byte retries = -1;
	byte defaultRetries = -1;
	byte errCount;

	/**
	 * Creates a new instance with the specified number of retries.
	 * 
	 * @param retries
	 *            if <code>0</code> or <code>&gt;0</code>, then this is
	 *            used as the number of retries. If it is set to
	 *            <code>&lt;0</code>, the receiver will reconnect until a
	 *            response is received.
	 * @param request
	 *            the request that created this object.
	 */
	MoveReceiver(PlayRequest request, byte retries) {
		this.pr = request;
		r = request.getNewRequest(request.getMoveReceiveTimeout());
		if (retries >= 0) {
			retry = true;
			this.retries = retries;
			this.defaultRetries = retries;
		}
	}

	/**
	 * Creates a standard <code>MoveReceiver</code> which tries to reconnect
	 * until an answer is retrieved.
	 * @param request
	 *            the request that created this object.
	 */
	MoveReceiver(PlayRequest request) {
		this(request, (byte) -1);
	}

	/** @see Request#reset() */
	public void reset() {
		r.reset();
		// reset interrupted
		interrupted = false;
		// reset retries
		retries = defaultRetries;
		errCount = 0;
	}

	/**
	 * Tries to receive the next move from the server
	 * @return the received move.
	 */
	Move getMove() {
		msg = null;
		// set parameters
		pr.setParams(r);

		Move move = null;
		while (condition()) {
			if (retry) {
				retries--; // use retry counter
			}
			move = receiveMove();
			if (move != null) {
				// message has been received - interrupt possible loop
				interrupted = true;
				// move received, exit loop
				break;
			}
			pause();
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
		if (errCount > 3) {
			msg = "Verbindung konnte nicht hergestellt werden. Versuche: " + errCount;
			return false;
		}
		return true;
	}

	private void pause() {
		try {
			Thread.sleep(PlayRequest.WAIT_MOVE_RECEIVE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	String getMessage() {
		return msg;
	}

	/**
	 * Receives the move as string and returns a newly built move. An optional
	 * message that holds additional information.
	 * 
	 * @return the received move.
	 */
	private Move receiveMove() {
		r.sendRequest(this.pr.path);
		String response = null;
		try {
			response = new String(r.getResponse());

			MoveMessage mmsg = new MoveMessage(response);

			if (mmsg.move == -1) {
				// no move available yet
				return null;
			}
			// set message from response
			msg = mmsg.getInfo();
			Logger.debug(getClass(), "received Move: " + mmsg.getMove());

			return new Move(mmsg.getMove());
		} catch (IOException e) {
			msg = e.getMessage();
			errCount++;
			return null;
		} catch (XMLParseException e) {
			msg = response;
			interrupted = true;
			return null;
		}
	}
}