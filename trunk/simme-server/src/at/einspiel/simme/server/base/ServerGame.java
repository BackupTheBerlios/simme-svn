package at.einspiel.simme.server.base;

import java.util.Date;

import test.sim.Game;

/**
 * Represents a game that is played between two users.
 *
 * @author kariem
 */
public class ServerGame {

    private static final int ACCURACY = 1000;

    /** date of game in seconds */
    private long date;
    private StopWatch stopwatch;
    private Game game;
    private boolean running;
    private boolean gameover;

    /** first player */
    protected User player1;
    /** second player */
    protected User player2;

    int moves;
    User winner;

    /**
     * Creates a new ServerGame object.
     *
     * @param p1 first player
     * @param p2 second player
     *
     * @throws Exception if rules are broken.
     */
    public ServerGame(User p1, User p2) throws Exception {
        if (p1.getNick().equals(p2.getNick())) {
            throw new Exception("Users have same nick names.");
        }

        this.player1 = p1;
        this.player2 = p2;
        game = new Game();
        running = false;
        gameover = false;
    }

    /**
     * Starts the game
     */
    public void startGame() {
        running = true;
        game.start();
        stopwatch.start();
    }

    /**
     * Stops the game
     */
    public void stopGame() {
        stopwatch.stop();
        running = false;
        gameover = true;
        moves = game.getMoveNr();
    }

    /**
     * Cancels this game.
     */
    public void cancelGame() {
    }

    /**
     * Returns the length of this game in ms
     *
     * @return The length of this game.
     */
    public long getLength() {
        if (gameover) {
            return stopwatch.getDuration();
        }

        return 0;
    }

    /**
     * Returns the date of the game. The accuracy is in seconds.
     * @return The date, of this game.
     */
    public Date getDate() {
        return new Date(date - (date % ACCURACY));
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
    protected boolean isRunning() {
        return running;
    }

    /**
     * Class that is used to stop the length of this game
     */
    class StopWatch {
        private long startTime;
        private long accumulatedTime;
        private boolean running;

        public void start() {
            startTime = System.currentTimeMillis();
            accumulatedTime = 0;
            running = true;
        }

        public void stop() {
            accumulatedTime = System.currentTimeMillis() - startTime;
            running = false;
        }

        public long getDuration() {
            if (running) {
                return 0;
            }

            return accumulatedTime;
        }
    }
}