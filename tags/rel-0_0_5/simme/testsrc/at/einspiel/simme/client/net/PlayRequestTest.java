// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: PlayRequestTest.java
//                  $Date: 2004/09/13 23:41:10 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import junit.framework.TestCase;

import at.einspiel.simme.client.Move;

/**
 * Test class for {@linkplain at.einspiel.simme.client.net.PlayRequest}.
 * @author kariem
 */
public class PlayRequestTest extends TestCase {

	private static final int TIMEOUT_RECEIVE = 1000;
	private static final int TIMEOUT_SEND = 1000;

	private static final String NICK = "--test";
	private static final String PATH = "Gameplay";
	private static final String GAME_ID = "gameid";
	private PlayRequest pr;

	/** Sets up the test */
	protected void setUp() {
		pr = new TestPlayRequest(GAME_ID, NICK, PATH, TIMEOUT_SEND, TIMEOUT_RECEIVE);
	}

	/** Tests the constructor */
	public void testPlayRequest() {
		// see if everything was set correctly
		assertEquals(GAME_ID, pr.id);
		assertEquals(NICK, pr.nickname);
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
	
	/** Tests sending of a move. */
	public void testMoveSend() {
		// no message yet
		assertNull(pr.getMessage());
		pr.sendMove(new Move(2));
		// error message after a certain amount of reconnects
		assertNotNull(pr.getMessage());
	}
}