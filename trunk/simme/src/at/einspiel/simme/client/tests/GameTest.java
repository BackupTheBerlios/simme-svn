package at.einspiel.simme.client.tests;

import junit.framework.TestCase;

import at.einspiel.simme.client.Game;

/**
 * Class used to Test class {@link test.sim.Game}
 *
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

    /**
     * Sets up the game which is used for the tests.
     *
     * @see TestCase#setUp()
     */
    protected void setUp() {
        g = new Game();
    }

    /** Tests simple selections */
    public void testSelectNode() {
        System.out.println("-- test: select node");
        g.selectNode(n4);
        assertTrue(g.isActivated(n4));
        assertFalse(g.isActivated(n3));
    }

    /** Tests node activation. */
    public void testActivateNode() {
        System.out.println("-- test: activate node");
        g.selectNode(n4);
        assertTrue(g.isActivated(n4));
        g.selectNode(n4);
        assertFalse(g.isActivated(n4));
    }

    /** Tests disabling of nodes */
    public void testDisableNode() {
        System.out.println("-- test: disable node");
        // select 0-1
        assertTrue(g.selectNode(n0));

        // 0 enabled
        assertFalse(g.isDisabled(n0));
        assertTrue(g.selectNode(n1));

        // select 0-2
        assertTrue(g.selectNode(n0));

        // 0 enabled
        assertFalse(g.isDisabled(n0));
        assertTrue(g.selectNode(n2));

        // select 0-3
        assertTrue(g.selectNode(n0));

        // 0 enabled
        assertFalse(g.isDisabled(n0));
        assertTrue(g.selectNode(n3));

        // select 0-4
        assertTrue(g.selectNode(n0));

        // 0 enabled
        assertFalse(g.isDisabled(n0));
        assertTrue(g.selectNode(n4));

        // select 0-5
        assertTrue(g.selectNode(n0));

        // 0 enabled
        assertFalse(g.isDisabled(n0));
        assertTrue(g.selectNode(n5));

        // 0 is disabled
        assertTrue(g.isDisabled(n0));

        // 1 is still enabled
        assertFalse(g.isDisabled(n1));
    }

    /** Tests edge selection */
    public void testEdgeSelection() {
        System.out.println("-- test: edge selection");
        // P1 selects 0-1
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n1));

        // 0-1 is owned by P1
        assertEquals(Game.PLAYER1, g.getEdgeOwner(n0, n1));

        // 0-2 is owned by neutral
        assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n2));
    }

    /** Tests edge selection by setting edges directly */ 
    public void testEdgeSelection2() {
        System.out.println("-- test: edge selection (edgeIndex)");
        byte firstEdge = 0;
        // P1 selects 0-1
        assertTrue(g.selectEdge(firstEdge));

        // 0-1 is owned by P1
        assertEquals(Game.PLAYER1, g.getEdgeOwner(n0, n1));
        assertEquals(Game.PLAYER1, g.getEdgeOwner(firstEdge));

        // 0-2 is owned by neutral
        assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n2));
    }

    /** Tests turn switching and edge selection. */
    public void testPlayers() {
        System.out.println("-- test: players");
        // P1 selects 0-1
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n1));

        // 0-1 owned by P1
        assertEquals(Game.PLAYER1, g.getEdgeOwner(n0, n1));

        // neutral owns 0-2, 0-3
        assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n2));
        assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n3));

        // P2 selects 0-1, then 0-2
        assertTrue(g.selectNode(n0));
        assertFalse(g.selectNode(n1));
        assertTrue(g.selectNode(n2));

        // 0-2 owned by P2
        assertEquals(Game.PLAYER2, g.getEdgeOwner(n0, n2));

        // neutral owns 0-3
        assertEquals(Game.NEUTRAL, g.getEdgeOwner(n0, n3));

        // P1 selects 0-3
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n3));

        // 0-3 owned by P1
        assertEquals(Game.PLAYER1, g.getEdgeOwner(n0, n3));
    }

    /** Tests winning conditions. */
    public void testWin() {
        System.out.println("-- test: win");
        // P1 selects 0-1
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n1));

        // P2 selects 0-2
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n2));

        // P1 selects 0-3
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n3));

        // P2 selects 0-4
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n4));

        // P1 selects 1-3
        assertTrue(g.selectNode(n1));
        assertTrue(g.selectNode(n3));

        // game is over
        assertTrue(g.isGameOver());

        // P2 is winner
        assertEquals(Game.PLAYER2, g.getWinner());

        // Selecting a node is not possible
        assertFalse(g.selectNode(n5));
    }

    /** Tests restarting games. */
    public void testRestartGame() {
        System.out.println("-- test: restart");
        testWin();
        g.start();
        testWin();
        g.start();
        testWin();
    }

    /** Tests move counter.  */
    public void testMoveCounter() {
        System.out.println("-- test: move counter");
        // P1 selects 0-1
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n1));

        // one move done
        assertEquals(1, g.getMoveNr());

        // P2 selects 0-2
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n2));

        // two moves done
        assertEquals(2, g.getMoveNr());

        // P1 selects 0-3
        assertTrue(g.selectNode(n0));
        assertTrue(g.selectNode(n3));

        // three moves done
        assertEquals(3, g.getMoveNr());
    }

    /** @see junit.framework.TestCase#tearDown() */
    protected void tearDown() throws Exception {
        System.out.println("## movemessage=" + g.getMoveMessage());
    }
}
