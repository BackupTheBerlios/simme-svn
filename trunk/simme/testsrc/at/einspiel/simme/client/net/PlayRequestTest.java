// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: PlayRequestTest.java
//                  $Date: 2004/09/13 15:22:00 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import java.util.Random;

import junit.framework.TestCase;

import at.einspiel.messaging.Request;
import at.einspiel.messaging.TestRequest;
import at.einspiel.simme.client.Game;
import at.einspiel.simme.client.Move;

/**
 * Test class for {@linkplain at.einspiel.simme.client.net.PlayRequest}.
 * @author kariem
 */
public class PlayRequestTest extends TestCase {

	private static final int TIMEOUT_RECEIVE = 1000;
	private static final int TIMEOUT_SEND = 1000;

	private static final String PATH = "Gameplay";
	private static final String NICK_NAME1 = "--Tester1";
	private static final String NICK_NAME2 = "--Tester2";
	private static final String PWD = "passwort";
	private static final String GAME_ID = "gameid";
	private PlayRequest pr;

	/** Sets up the test */
	protected void setUp() {
		pr = new PlayTestRequest(GAME_ID, NICK_NAME1, PATH, TIMEOUT_SEND, TIMEOUT_RECEIVE);
	}

	/** Tests the constructor */
	public void testPlayRequest() {
		// see if everything was set correctly
		assertEquals(GAME_ID, pr.id);
		assertEquals(NICK_NAME1, pr.nickname);
		assertEquals(PATH, pr.path);

		assertNotNull(pr.moveReceiver);
		assertNotNull(pr.moveSender);
	}

	/** Tests the receipt of a move. */
	public void testMoveReceive() {
		// no message yet
		assertNull(pr.getMessage());
		pr.receiveMove();
		// error message after a certain amount of reconnects
		assertNotNull(pr.getMessage());
	}

	/** Tests move sending */
	public void testMoveSendReceive() {
		NetworkGame[] games = prepareGame();
		NetworkGame g1 = games[0];
		NetworkGame g2 = games[1];

		// games are started automatically by client
		startGame(g1);
		startGame(g2);

		// both games should show the same starting player
		byte playersTurn = g1.getPlayersTurn();
		assertEquals(playersTurn, g2.getPlayersTurn());
		assertTrue(playersTurn == Game.PLAYER1 || playersTurn == Game.PLAYER2);

		NetworkGame first, second;
		if (playersTurn == Game.PLAYER1) {
			first = g1;
			second = g2;
		} else {
			first = g2;
			second = g1;
		}

		Random r = new Random();
		// first player makes a random move
		makeRandomMove(first, r);
		// move should be sent to server
		sleep((TIMEOUT_RECEIVE + TIMEOUT_SEND) * 2);
		// and received by other game
		// both games should now be in the same state
		assertEquals(first, second);
	}

	// performs a random move on the given game
	private void makeRandomMove(NetworkGame g, Random r) {
		byte edge = (byte) r.nextInt(Move.MAX_EDGE_INDEX);
		if (!g.selectEdge(edge)) {
			// try to find an edge that is not selected
			makeRandomMove(g, r);
		}
	}

	private NetworkGame[] prepareGame() {
		SimulatedPlayer p1 = new SimulatedPlayer(NICK_NAME1, PWD);
		SimulatedPlayer p2 = new SimulatedPlayer(NICK_NAME2, PWD);

		assertFalse(p1.nick.equals(p2.nick));
		return createGame(p1, p2);
	}

	/**
	 * Starts the game for two players.
	 * @param p1
	 *            the first player.
	 * @param p2
	 *            the seconds player.
	 * @return the two newly created network game between these two players.
	 */
	private NetworkGame[] createGame(SimulatedPlayer p1, SimulatedPlayer p2) {
		Thread start1 = new Thread(p1.getStartRunnable());
		Thread start2 = new Thread(p2.getStartRunnable());
		start1.start();
		start2.start();

		while (p1.getGame() == null || p2.getGame() == null) {
			sleep(1500);
		}

		NetworkGame g1 = p1.getGame();
		NetworkGame g2 = p2.getGame();

		// both games should contain the same information
		assertEquals(g1, g2);

		// substitute network playrequests with TestRequests
		g1.pr = substituteRequest(g1.pr);
		g2.pr = substituteRequest(g2.pr);

		assertEquals(g1, g2);

		return new NetworkGame[]{g1, g2};
	}

	private void startGame(final NetworkGame g) {
		Thread t = new Thread("Game Starter") {
			/**
			 * Starts the given game.
			 * @see java.lang.Thread#run()
			 */
			public void run() {
				g.start();
			}
		};
		t.start();
	}

	/**
	 * Sleeps the current thread for the indicated amount of milliseconds.
	 * 
	 * @param i
	 *            the number of milliseconds to sleep.
	 * 
	 * @see Thread#sleep(long)
	 */
	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Substitutes the given play request with a play request that internally
	 * uses a TestRequest object.
	 * @param playRequest
	 *            the play request to be substituted
	 * @return a newly created test request that is equal to <code>pr</code>,
	 *         but uses a TestRequest object instead of a Request object.
	 */
	private PlayRequest substituteRequest(PlayRequest playRequest) {
		return new PlayTestRequest(playRequest.id, playRequest.nickname, playRequest.path,
				TIMEOUT_SEND, TIMEOUT_RECEIVE);
	}

	/**
	 * Uses a test request for internal move receiver and move sender by
	 * overwriting {@linkplain PlayRequest#getNewRequest(int)}
	 */
	class PlayTestRequest extends PlayRequest {

		PlayTestRequest(String g, String nick, String path, int toSend, int toReceive) {
			super(g, nick, path, toSend, toReceive);
		}

		/** @see PlayRequest#getNewRequest(int) */
		Request getNewRequest(int timeout) {
			// use test request instead of Request to test within J2SE
			return new TestRequest(timeout);
		}
	}
}