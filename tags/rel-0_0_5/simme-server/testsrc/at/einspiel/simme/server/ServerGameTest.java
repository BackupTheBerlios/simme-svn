package at.einspiel.simme.server;

import junit.framework.TestCase;
import at.einspiel.base.IGame;
import at.einspiel.base.User;
import at.einspiel.simme.client.Move;

/**
 * Class to test Server Game.
 * 
 * @author kariem
 */
public class ServerGameTest extends TestCase {

    IGame game;
    User player1;
    User player2;
    
    /** Tests Game constructor */
    public final void testManagedGameConstructor() {
        player1 = new User();
        player1.setNick("player1");
        player2 = new User();
        player2.setNick("player1");
        Exception ex = null;
        try {
            // should throw error => same nick names
            game = new ServerGame(player1, player2);
        } catch (Exception e) {
            ex = e;
        }
        assertNotNull(ex);
        assertNull(game);
        
        initGame();
    }
    
    /** Tests playing a game */
    public final void testPlayGame() {
        initGame();
        game.startGame();
        
        // make moves until game ends
        for (byte i = 0; i <= Move.MAX_EDGE_INDEX; i++) {
            if (!game.makeMove(new Move(i)).success()) {
                break;
            }
        }

        game.getLength();
        
        // no more moves possible
        for (byte i = 0; i <= Move.MAX_EDGE_INDEX; i++) {
            assertFalse(game.makeMove(new Move(i)).success());
        }
    }
    
    /** Game initialization */
    private void initGame() {
        // create players
        player1 = new User();
        player1.setNick("player1");
        player2 = new User();
        player2.setNick("player2");
        
        // create game
        game = new ServerGame(player1, player2);
        assertNotNull(game);
    }
}