package at.einspiel.simme.server.base;

import java.util.Date;

import test.sim.Game;

/**
 * Represents a game that is played between two users.
 *
 * @author kariem
 */
public class ServerGame {

   /** date of game in seconds */
   private int date;
   private User player1;
   private User player2;
   private StopWatch stopwatch;
   private Game game;
   private boolean running;
   private boolean gameover;

   int moves;
   User winner;

   /**
    * Creates a new ServerGame object.
    *
    * @param player1 first player
    * @param player2 second player
    *
    * @throws Exception if rules are broken.
    */
   public ServerGame(User player1, User player2) throws Exception {
      if (player1.compareTo(player2) == 0) {
         throw new Exception("Users have same nick names.");
      }

      this.player1 = player1;
      this.player2 = player2;
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
      return new Date(date * 1000);
   }

   /**
    * Sets the date of this game. All information more exact than seconds will
    * be dropped.
    * @param d The new date.
    */
   public void setDate(Date d) {
      this.date = (int) (d.getTime() / 1000);
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
