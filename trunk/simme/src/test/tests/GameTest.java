package test.tests;

import junit.framework.TestCase;
import test.sim.Game;

/**
 * @author kariem
 */
public class GameTest extends TestCase {

   private Game g;
   
   // nodes 0-5
   private byte n0 = 0;
   private byte n1 = 1;
   private byte n2 = 2;
   private byte n3 = 3;
   private byte n4 = 4;
   private byte n5 = 5;

   protected void setUp() {
      g = new Game();
   }

	/**
	 * Tests simple selections
	 */
   public void testSelectNode() {
      g.selectNode(n4);
      assertEquals(true, g.isActivated(n4));
      assertEquals(false, g.isActivated(n3));
   }

	/**
	 * Tests node activation.
	 */
   public void testActivateNode() {
      g.selectNode(n4);
      assertEquals(true, g.isActivated(n4));
      g.selectNode(n4);
      assertEquals(false, g.isActivated(n4));
   }

	/**
	 * Tests disabling of nodes
	 */
   public void testDisabledNode() {
      // select 0-1
      assertEquals(true, g.selectNode(n0));
		// 0 enabled
      assertEquals(false, g.isDisabled(n0));
      assertEquals(true, g.selectNode(n1));

      // select 0-2
      assertEquals(true, g.selectNode(n0));
		// 0 enabled
      assertEquals(false, g.isDisabled(n0));
      assertEquals(true, g.selectNode(n2));

      // select 0-3
      assertEquals(true, g.selectNode(n0));
		// 0 enabled
      assertEquals(false, g.isDisabled(n0));
      assertEquals(true, g.selectNode(n3));

      // select 0-4
      assertEquals(true, g.selectNode(n0));
		// 0 enabled
      assertEquals(false, g.isDisabled(n0));
      assertEquals(true, g.selectNode(n4));

      // select 0-5
      assertEquals(true, g.selectNode(n0));
		// 0 enabled
      assertEquals(false, g.isDisabled(n0));
      assertEquals(true, g.selectNode(n5));

      // 0 is disabled		
      assertEquals(true, g.isDisabled(n0));

      // 1 is still enabled
      assertEquals(false, g.isDisabled(n1));
   }


	/**
	 * Tests edge selection
	 */
   public void testEdgeSelection() {
      // P1 selects 0-1
      assertEquals(true, g.selectNode(n0));
      assertEquals(true, g.selectNode(n1));
      
      // 0-1 is owned by P1
      assertEquals(Game.PLAYER1, g.getEdgeOwner(n0, n1));
      // 0-2 is owned by neutral
		assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n2));
   }


	/**
	 * Tests turn switching and edge selection.
	 */
   public void testPlayers() {
		// P1 selects 0-1
		assertEquals(true, g.selectNode(n0));
		assertEquals(true, g.selectNode(n1));
		// 0-1 owned by P1
		assertEquals(Game.PLAYER1, g.getEdgeOwner(n0, n1));

		// neutral owns 0-2, 0-3
		assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n2));
		assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n3));


		// P2 selects 0-1, then 0-2
		assertEquals(true, g.selectNode(n0));
		assertEquals(false, g.selectNode(n1));
		assertEquals(true, g.selectNode(n2));
		// 0-2 owned by P2
		assertEquals(Game.PLAYER2, g.getEdgeOwner(n0, n2));

		// neutral owns 0-3
		assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n3));


		// P1 selects 0-3
		assertEquals(true, g.selectNode(n0));
		assertEquals(true, g.selectNode(n3));
		// 0-3 owned by P1
		assertEquals(Game.PLAYER1, g.getEdgeOwner(n0, n3));
   }
   
	/**
	 * Tests winning conditions.
	 */
	public void testWin() {
		// P1 selects 0-1
		assertEquals(true, g.selectNode(n0));
		assertEquals(true, g.selectNode(n1));

		// P2 selects 0-2
		assertEquals(true, g.selectNode(n0));
		assertEquals(true, g.selectNode(n2));

		// P1 selects 0-3
		assertEquals(true, g.selectNode(n0));
		assertEquals(true, g.selectNode(n3));

		// P2 selects 0-4
		assertEquals(true, g.selectNode(n0));
		assertEquals(true, g.selectNode(n4));

		// P1 selects 1-3
		assertEquals(true, g.selectNode(n1));
		assertEquals(true, g.selectNode(n3));
		
		// game is over
		assertEquals(true, g.isGameOver());
		
		// P2 is winner
		assertEquals(Game.PLAYER2, g.getWinner());

		// Selecting a node is not possible
		assertEquals(false, g.selectNode(n5));
	}

	/**
	 * Tests possibility of restarting games.
	 */
	public void testRestartGame() {
		testWin();
		g.start();
		testWin();
		g.start();
		testWin();
	}
}
