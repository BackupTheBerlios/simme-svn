// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: IReqHandler.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.simme.server.ManagedUser;

/**
 * A request handler used to forward basic user requests. The handler can answer
 * to a given user according to the handler's configuration or type.
 * 
 * @author kariem
 */
public interface IReqHandler {

	/**
	 * The update time to wait for the next game in seconds. Currently this is
	 * set to 3 seconds.
	 */
	int WAIT_FOR_GAME = 3;
	/**
	 * The update time to start the game.
	 */
	int WAIT_FOR_START = 2;
	/**
	 * Answers the given user.
	 * 
	 * @param mu
	 *            the user, which will be the recipient of the message.
	 * @return a String that can be interpreted by the user.
	 */
	String answer(ManagedUser mu);
}