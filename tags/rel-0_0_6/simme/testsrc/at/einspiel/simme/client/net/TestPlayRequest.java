// ----------------------------------------------------------------------------
// [SimME]
//       Java Source File: TestPlayRequest.java
//                  $Date: 2004/09/13 23:41:11 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import at.einspiel.messaging.Request;
import at.einspiel.messaging.TestRequest;


/**
 * Uses a test request for internal move receiver and move sender by
 * overwriting {@linkplain PlayRequest#getNewRequest(int)}
 */
class TestPlayRequest extends PlayRequest {

	TestPlayRequest(String g, String nick, String path, int toSend, int toReceive) {
		super(g, nick, path, toSend, toReceive);
	}

	/** @see PlayRequest#getNewRequest(int) */
	Request getNewRequest(int timeout) {
		// use test request instead of Request to test within J2SE
		return new TestRequest(timeout);
	}
}