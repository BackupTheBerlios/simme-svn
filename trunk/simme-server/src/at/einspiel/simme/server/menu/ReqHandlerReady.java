// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ReqHandlerReady.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.simme.client.GameInfo;
import at.einspiel.simme.nanoxml.XMLElement;
import at.einspiel.simme.server.ManagedGame;
import at.einspiel.simme.server.ManagedUser;

/**
 * Checks all prerequisites for a network game managed by the server and
 * synchronises the participating player's start time. The users have to be in
 * "playing state".
 * 
 * @see at.einspiel.simme.server.UserManager
 * @see at.einspiel.simme.server.UserState#STATE_PLAYING
 * 
 * @author kariem
 */
class ReqHandlerReady extends AbstractReqHandler {

	/** @see IReqHandler#answer(ManagedUser) */
	public String answer(ManagedUser mu) {
		ManagedGame game = mu.getGame();
		GameInfo info = game.getGame().getGameInfo();

		XMLElement gameInfoXml = info.toXml();
		gameInfoXml.setAttribute("path", "Gameplay");
		XMLElement gameMsg = new XMLElement();
		gameMsg.setName("game");
		gameMsg.addChild(gameInfoXml);
		return gameMsg.toString();
	}
}