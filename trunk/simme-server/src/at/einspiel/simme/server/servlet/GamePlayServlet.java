// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: GamePlayServlet.java
//                  $Date: 2004/09/13 23:43:48 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.einspiel.base.Result;
import at.einspiel.messaging.IConstants;
import at.einspiel.messaging.IMessage;
import at.einspiel.simme.client.Move;
import at.einspiel.simme.server.ManagedGame;
import at.einspiel.simme.server.ManagedUser;
import at.einspiel.simme.server.SessionManager;
import at.einspiel.simme.server.net.MoveMessageXML;

/**
 * This servlet is used to synchronise gaming between opponents.
 * 
 * @author kariem
 */
public class GamePlayServlet extends AbstractServlet {

	ServletOutputStream out;

	/**
	 * @see AbstractServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);

		out = resp.getOutputStream();

		// see if user is logged in
		if (u == null) {
			out.print("User not logged in.");
			return;
		}

		// see if user is currently participating in a game
		final ManagedGame game = u.getGame();
		if (game == null) {
			out.print("User '" + u.getNick()
					+ "does not seem to have a game running at the moment.");
		}
		
		// if not set to playing, set the player's state to playing
		if (!u.isPlaying()) {
			u.startPlaying();
		}

		// move send or move receive?
		String edgeString = req.getParameter(IConstants.PARAM_EDGE);
		if (edgeString != null) {
			// move send
			byte edge = Byte.parseByte(edgeString);
			out.print(moveSend(game, edge));
		} else {
			out.print(moveReceive());
		}
	}

	/**
	 * Returns the last move performed on the game to the user.
	 * 
	 * @return the last move as a move message.
	 * @see at.einspiel.simme.client.net.MoveMessage
	 * @see ManagedUser#getClientMessage()
	 */
	private String moveReceive() {
		IMessage msg = u.getClientMessage();
		getServletContext().log("User " + u.getNick() + " called move receive");
		if (msg == null) {
			return new MoveMessageXML((byte) -1).getMessage();
		}
		return msg.getMessage();
	}

	/**
	 * Indicates whether the server accepts the move which the client sent to
	 * it.
	 * 
	 * @param game
	 *            the user's game.
	 * @param edge
	 *            the edge selected by the user.
	 * @return a message showing whether the the selected edge was accepted for
	 *         <code>game</code>.
	 */
	private String moveSend(ManagedGame game, byte edge) {
		// see if user participates in this game
		ManagedGame serverGame = SessionManager.getInstance().getGame(game.getId());
		if (serverGame == null) {
			return "No corresponding game found";
		}

		if (!serverGame.getId().equals(game.getId())) {
			return "Game id does not match id on the server.";
		}

		// perform the move
		Result r = u.makeMove(new Move(edge));
		boolean ok = r.success();
		getServletContext().log("User " + u.getNick() + " sent move: " + edge);
		return ok ? "OK" : "Edge could not be selected";
	}
}