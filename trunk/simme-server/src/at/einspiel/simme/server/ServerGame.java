// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ServerGame.java
//                  $Date: 2003/12/29 07:24:27 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.einspiel.base.IGame;
import at.einspiel.base.Result;
import at.einspiel.base.User;
import at.einspiel.simme.client.Game;
import at.einspiel.simme.client.Move;

/**
 * Represents a simme game that is played between two users.
 *
 * @author kariem
 */
public class ServerGame implements IGame {

    private static final int ACCURACY = 1000;

    /** date of game in seconds */
    private long date;
    /** times each player needed to perform moves */
    private long time1, time2;

    /** moves that were performed */
    private List savedMoves; // used to reconstruct game later
    
    private final StopWatch stopwatch;
    private Game game;
    private boolean running;
    private boolean gameover;

    /** first player */
    protected User player1;
    /** second player */
    protected User player2;

    /** game id */
    private String id;

    byte nbMoves;
    User winner;

    /**
     * Creates a new ServerGame object.
     *
     * @param p1 first player
     * @param p2 second player
     *
     * @throws RuntimeException if rules are broken.
     */
    public ServerGame(User p1, User p2) throws RuntimeException {
        String nick1 = p1.getNick();
        String nick2 = p2.getNick();
        if (nick1.equals(nick2)) {
            throw new RuntimeException("Users have same nick names.");
        }

        game = new Game();
        if (Math.random() > 0.5) {
            setPlayers(p1, p2);
        } else {
            setPlayers(p2, p1);
        }
        setDate(new Date());

        // set id to something unique
        id = date + "#~#" + nick1 + "#~#" + nick2;

        // initialize game status
        running = false;
        gameover = false;
        // create stopwatch
        stopwatch = new StopWatch();
    }

    /**
     * Sets the players for this game.
     * @param p1 player 1.
     * @param p2 player 2.
     */
    public void setPlayers(User p1, User p2) {
        this.player1 = p1;
        this.player2 = p2;
    }

    /** Starts the game */
    public void startGame() {
        running = true;
        gameover = false;
        savedMoves = new ArrayList();
        game.start();
        stopwatch.start();
    }

    /** Rotates players and restarts the game. */
    public void restartGame() {
       setPlayers(player2, player1);
       startGame();
    }
    
    
    /** Stops the game */
    public void stopGame() {
        if (!gameover) {
            stopwatch.stop();
            running = false;
            gameover = true;
            nbMoves = game.getMoveNr();
        }
    }

    /** Cancels this game. */
    public void cancelGame() {
        // TODO implement cancel game on server
    }

    /**
     * Returns the length of this game in ms
     *
     * @return The length of this game.
     */
    public long getLength() {
        return stopwatch.getTotalDuration();
    }

    /**
     * Returns the date of the game. The accuracy is in seconds.
     * @return The date, of this game.
     */
    public Date getDate() {
        return new Date(date);
    }

    /**
     * Sets the date of this game. All information more exact than seconds will
     * be dropped.
     * @param d The new date.
     */
    public void setDate(Date d) {
        long time = d.getTime();
        this.date = (time - (time % ACCURACY));
    }

    /**
     * Returns whether the game is currently running.
     * 
     * @return <code>true</code> if the game is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the game's id.
     * @return the id of the game.
     */
    public String getId() {
        return id;
    }

    /** Class that is used to stop the length of this game */
    class StopWatch {
        private long startTime;
        private long lastPause;
        private long lastDuration;
        private long totalDuration;
        private boolean started;

        void start() {
            startTime = System.currentTimeMillis();
            lastPause = startTime;
            totalDuration = 0;
            started = true;
        }

        long elapsed() {
            pause(System.currentTimeMillis());
            return lastDuration;
        }

        void stop() {
            long time = System.currentTimeMillis();
            pause(time);
            totalDuration = time - startTime;
            started = false;
        }

        long getTotalDuration() {
            if (started) {
                return System.currentTimeMillis() - startTime;
            }
            return totalDuration;
        }

        private void pause(long time) {
            lastDuration = time - lastPause;
            lastPause = time;
        }

    }

    //
    // game method accessors
    //

    /**
     * Returns the move number.
     * @return the move number.
     */
    public byte getMoveNr() {
        return game.getMoveNr();
    }

    /**
     * Performs the given move for a user.
     * @param u the user.
     * @param m the move to perform.
     * @return the move's result.
     */
    public Result makeMove(User u, Move m) {
       return selectEdge(u, m.getEdge()) ? Result.POSITIVE : Result.NEGATIVE;
    }    
    
    /**
     * Performs the given move.
     * 
     * @param m the move to perform.
     * @return the move's result.
     */
    public Result makeMove(Move m) {
        return makeMove(null, m);
    }

    /**
     * Selects an edge in the game on the server.
     * 
     * @param u the user who performs the move.
     * @param edge the edge to select (between 0 and 14).
     * @return whether the selection has succeeded.
     */
    public boolean selectEdge(User u, byte edge) {
        if (isRunning()) {
            byte playersTurn = game.getPlayersTurn();
            if (isOnTurn(u, playersTurn)) {

                // make move
                boolean edgeSelection = game.selectEdge(edge);
                if (edgeSelection) {
                    long elapsed = stopwatch.elapsed();

                    // add elapsed time to move time of player
                    if (playersTurn == Game.PLAYER1) {
                        time1 += elapsed;
                    } else {
                        time2 += elapsed;
                    }

                    // add move to the list of performed moves
                    savedMoves.add(new Byte(edge));
                    
                    // stop game, if this was the lastmove
                    if (game.isGameOver()) {
                       stopGame();
                    }
                }

                return edgeSelection;
            }
        }
        return false;
    }

    /**
     * Returns whether it is the given user's turn to play.
     * @param user the user.
     * @return <code>true</code> if the game is running and it is the given
     *          user's turn. <code>false</code> if the game is not running or
     *          has already ended, or the given user is not allowed to make the
     *          next move.
     */
    public boolean isPlayerOnTurn(User user) {
       return isOnTurn(user, game.getPlayersTurn());
    }
    
    boolean isOnTurn(User u, byte playersTurn) {
        if (playersTurn == Game.NEUTRAL) {
            return false; // do nothing
        } else {
            if (u != null) {
                String nick = u.getNick();
                if (playersTurn == Game.PLAYER1) {
                    if (!nick.equals(player1.getNick())) {
                        return false;
                    }
                } else if (playersTurn == Game.PLAYER2) {
                    if (!nick.equals(player2.getNick())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}