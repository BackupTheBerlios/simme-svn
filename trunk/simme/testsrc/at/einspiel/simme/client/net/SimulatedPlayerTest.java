// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: SimulatedPlayerTest.java
//                  $Date: 2004/09/13 15:22:00 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * @author kariem
 */
public class SimulatedPlayerTest extends TestCase {

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
}