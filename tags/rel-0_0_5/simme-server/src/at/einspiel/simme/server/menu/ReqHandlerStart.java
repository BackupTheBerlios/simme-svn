// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ReqHandlerStart.java
//                  $Date: 2004/09/13 15:11:53 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.menu;

import at.einspiel.simme.server.ManagedGame;
import at.einspiel.simme.server.ManagedUser;
import at.einspiel.simme.server.SessionManager;

/**
 * Starts the game for the user, if a second user is in waiting state.
 * 
 * @see at.einspiel.simme.server.UserManager
 * @see at.einspiel.simme.server.UserState#STATE_PLAYING
 * 
 * @author kariem
 */
class ReqHandlerStart extends AbstractReqHandler {

	/**
	 * Creates a new instance of <code>ReqHandlerStart</code>.
	 * @see AbstractReqHandler#AbstractReqHandler(String, String)
	 */
	public ReqHandlerStart(String idCurrent, String idNext) {
		super(idCurrent, idNext);
	}

	/** @see IReqHandler#answer(ManagedUser) */
	public String answer(ManagedUser mu) {
		ManagedGame game = mu.getGame();
		final String title;
		final String message;
		final int time;

		boolean hasGame = game != null;

		if (hasGame) {
			// a game has been created, that means, there are already two
			// players ready to play
			final ManagedUser player1 = game.getPlayer1();
			final ManagedUser player2 = game.getPlayer2();

			final String nick1 = player1.getNick();
			final String nick2 = player2.getNick();

			StringBuffer buf = new StringBuffer();
			buf.append("Spiel:\n  ");
			buf.append(nick1);
			buf.append(" vs ");
			buf.append(nick2);
			buf.append("\nGameID: ");
			buf.append(game.getNiceId());
			buf.append("\n(noch einen Moment)");

			title = "Spiel initialisiert";
			message = buf.toString();
			time = WAIT_FOR_START;
		} else {
			// no game yet
			
			int playersOnline = SessionManager.getInstance().getNumberOfUsers() - 1; 
			StringBuffer buf = new StringBuffer();
			buf.append("...auf Gegenspieler. Noch einen Moment Geduld. (");
			buf.append(playersOnline);
			buf.append(" Spieler auﬂer dir ");
			buf.append(playersOnline == 1 ? "ist " : "sind ");
			buf.append("gerade online.)");
			
			title = "Warte...";
			message = buf.toString();
			time = WAIT_FOR_GAME;
		}

		return createTextMessage(title, message, time, hasGame);
	}
}