package at.einspiel.simme.server.base;

import java.util.Date;

import at.einspiel.simme.client.Game;
import at.einspiel.simme.client.Move;

/**
 * Represents a game that is played between two users.
 *
 * @author kariem
 */
public class ServerGame {

    private static final int ACCURACY = 1000;

    /** date of game in seconds */
    private long date;
    /** times each player needed to perform moves */
    private long time1, time2;

    private StopWatch stopwatch;
    private Game game;
    private boolean running;
    private boolean gameover;

    /** first player */
    protected User player1;
    /** second player */
    protected User player2;

    /** game id */
    private String id;

    int moves;
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

        this.player1 = p1;
        this.player2 = p2;
        game = new Game();
        setDate(new Date());

        // set id to something unique
        id = date + "#~#" + nick1 + "#~#" + nick2;

        // initialize game status
        running = false;
        gameover = false;
    }

    /** initializes the game */
    public void initializeGame() {
        // TODO write initialization code
    }

    /** Starts the game */
    public void startGame() {
        running = true;
        game.start();
        if (stopwatch == null) {
            stopwatch = new StopWatch();
        }
        stopwatch.start();
    }

    /** Stops the game */
    public void stopGame() {
        if (!gameover) {
            stopwatch.stop();
            running = false;
            gameover = true;
            moves = game.getMoveNr();
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

        long pause() {
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
     * Performs the given move.
     * 
     * @param m the move to perform.
     * @return the move's result.
     */
    public Result makeMove(Move m) {
        return selectEdge(m.getEdge()) ? Result.POSITIVE : Result.NEGATIVE;
    }

    /**
     * Selects an edge in the game on the server.
     * 
     * @param edge the edge to select (between 0 and 14)
     * @return whether the selection has succeeded.
     */
    public boolean selectEdge(byte edge) {
        if (isRunning()) {
            byte playersTurn = game.getPlayersTurn();
            if (playersTurn == Game.NEUTRAL) {
                return false; // do nothing
            }

            // make move
            boolean edgeSelection = game.selectEdge(edge);
            if (edgeSelection) {
                long duration = stopwatch.pause();

                // add duration to move time of player
                if (playersTurn == Game.PLAYER1) {
                    time1 += duration;
                } else {
                    time2 += duration;
                }
            }

            return edgeSelection;
        }
        return false;
    }
}