// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: MoveSender.java
//                  $Date: 2004/09/13 15:22:00 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import java.io.IOException;

import at.einspiel.logging.Logger;
import at.einspiel.messaging.IConstants;
import at.einspiel.messaging.Request;
import at.einspiel.simme.client.Move;

/**
 * Sends a single move to the server. The response only contains a boolean
 * value, which indicates whether the move was accepted by the server, and an
 * optional message.
 */
class MoveSender {

	private final PlayRequest pr;
	private final Request r;
	private String msg;

	MoveSender(PlayRequest request) {
		this.pr = request;
		r = request.getNewRequest(request.getMoveSendTimeout());
	}

	/**
	 * Sends the move to the server and indicates whether the move was accepted.
	 * @param m
	 *            the move to be sent.
	 * @return <code>true</code> if the move was accepted. If an error occurs,
	 *         this method returns <code>false</code> and an optional message
	 *         or cause may be retrieved by a call to {@linkplain #getMessage()}.
	 *  
	 */
	boolean sendMove(Move m) {
		pr.setParams(r);
		// set parameters
		r.setParam(IConstants.PARAM_EDGE, new Byte(m.getEdge()).toString());

		Logger.debug(getClass(), "sending move: " + m.getEdge());
		r.sendRequest(this.pr.path);

		try {
			// retrieve response
			String response = new String(r.getResponse());
			Logger.debug(getClass(), "response=" + response);
			if (response.equals("OK")) { // TODO rethink
				// everything fine, move successfully sent
				return true;
			}
			// otherwise save response
			msg = response;
		} catch (IOException e) {
			// error occured - save in message
			msg = "Error: " + e.getMessage();
			Logger.debug(getClass(), msg);
		}
		return false;
	}

	/** @see Request#reset() */
	public void reset() {
		r.reset();
		msg = null;
	}

	String getMessage() {
		return msg;
	}
}