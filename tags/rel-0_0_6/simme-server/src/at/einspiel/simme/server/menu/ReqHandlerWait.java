// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ReqHandlerWait.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.simme.server.ManagedUser;

/**
 * Sets the user in "waiting" state.
 * 
 * @see at.einspiel.simme.server.UserState#STATE_WAITING
 * 
 * @author kariem
 */
class ReqHandlerWait extends AbstractReqHandler {

	/**
	 * Creates a new instance of <code>ReqHandlerWait</code>.
	 * @see AbstractReqHandler#AbstractReqHandler(String)
	 */
	public ReqHandlerWait(String idNext) {
		super(idNext);
	}

	/** @see IReqHandler#answer(ManagedUser) */
	public String answer(ManagedUser mu) {
		// set user to wait for game
		mu.waitForGame();
		// at the next session manager's update, two waiting users will be
		// combined and a new game will be started

		// tell the user that his client will update automatically
		return createTextMessage(
				"Warte auf nächstes Spiel",
				"Du wartest auf das nächste Spiel. Je nach Anzahl der verfügbaren Gegner kann das einige Sekunden dauern...",
				WAIT_FOR_GAME, true);
	}
}