// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: GameRandomAITest.java
//                  $Date: 2004/04/15 10:00:55 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client;

import junit.framework.TestCase;

/**
 * Class to test {@linkplain at.einspiel.simme.client.GameRandomAI}.
 * 
 * @author kariem
 */
public class GameRandomAITest extends TestCase {

    private Game game;
    
    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() {
        game = new GameRandomAI();
        game.start();
    }
    
    /** Tests automatic node selection with {@linkplain Game#selectNode(byte)} */
    public void testSelectNode() {
        // no move has been performed yet
        assertTrue(game.getMoveNr() == 0);

        // select first node
        assertTrue(game.selectNode((byte) 0));
        
        // still no move performed
        assertTrue(game.getMoveNr() == 0);

        // select second node
        assertTrue(game.selectNode((byte) 1));
        
        // one edge is selected, computer should now perform a move
        
        assertTrue(game.getMoveNr() == 2); // move count at 2
    }

    
    /** Tests automatic node selection with {@linkplain Game#selectEdge(byte)} */
    public void testSelectEdge() {
        // no move has been performed yet
        assertTrue(game.getMoveNr() == 0);

        // select first edge
        assertTrue(game.selectEdge((byte) 0));
        
        // one edge is selected, computer should now perform a move
        
        assertTrue(game.getMoveNr() == 2);
    }

}
