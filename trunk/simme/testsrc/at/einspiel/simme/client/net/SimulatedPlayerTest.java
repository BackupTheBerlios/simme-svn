// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SimulatedPlayerTest.java
//                  $Date: 2004/09/13 23:41:11 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import java.io.IOException;

import junit.framework.TestCase;

import at.einspiel.simme.client.Game;

/**
 * @author kariem
 */
public class SimulatedPlayerTest extends TestCase {

	private static final int TIMEOUT_RECEIVE = 1000;
	private static final int TIMEOUT_SEND = 1000;

	private static final String NICK_NAME1 = "--Tester1";
	private static final String NICK_NAME2 = "--Tester2";
	private static final String PWD = "passwort";

	SimulatedPlayer player;

	/** @see junit.framework.TestCase#setUp() */
	protected void setUp() throws Exception {
		player = new SimulatedPlayer("testplayer", "password");
	}

	/**
	 * Tests the login process.
	 * @throws IOException
	 *             if the player could not login.
	 */
	public void testLogin() throws IOException {
		String communicationPath = player.login();
		assertEquals("SessionMgr", communicationPath);
	}

	/**
	 * Tests the process until the start of the game. This test can last longer,
	 * depending on the menu structure.
	 * 
	 * @throws IOException
	 *             if an error occured while either one of the players tried to
	 *             start a game.
	 */
	public void testStartGame() throws IOException {
		final SimulatedPlayer player2 = new SimulatedPlayer("testp2", "password");
		// start game for player2 in own thread
		Thread gameStarter = new Thread(player2.getStartRunnable());
		gameStarter.start();

		NetworkGame game = player.startGame();
		assertNotNull(game);
	}

	/** Tests move sending */
	public void testPlayGame() {
		SimulatedPlayer p1 = new SimulatedPlayer(NICK_NAME1, PWD);
		SimulatedPlayer p2 = new SimulatedPlayer(NICK_NAME2, PWD);

		assertFalse(p1.nick.equals(p2.nick));

		Thread start1 = new Thread(p1.getStartRunnable());
		Thread start2 = new Thread(p2.getStartRunnable());
		start1.start();
		start2.start();

		// wait until the game is started
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

		// game has not been started yet ... starting player is Game.NEUTRAL
		byte playersTurn = g1.getPlayersTurn();
		assertEquals(playersTurn, g2.getPlayersTurn());
		assertTrue(playersTurn != Game.NEUTRAL);
		assertTrue(playersTurn == Game.PLAYER1 || playersTurn == Game.PLAYER2);

		// start playing
		Thread play1 = new Thread(p1.getPlayRunnable());
		Thread play2 = new Thread(p2.getPlayRunnable());
		play1.start();
		play2.start();

		// wait until the end of the game.
		while (gamesAreRunning(g1, g2)) {
			sleep(1500);
		}

		// games should show the same winner
		assertEquals(g1.getWinner(), g2.getWinner());
	}

	private boolean gamesAreRunning(NetworkGame g1, NetworkGame g2) {
		return !(g1.isGameOver() && g2.isGameOver());
	}

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
		return new TestPlayRequest(playRequest.id, playRequest.nickname, playRequest.path,
				TIMEOUT_SEND, TIMEOUT_RECEIVE);
	}
}