package at.einspiel.simme.server.base;

/**
 * Used to save statistics on a single {@link User}.
 *
 * @author kariem
 */
public class Statistics {

   /** reference to user */
   private User player;
   /** reference to game type */
   private GameType gametype;
   /** number of games */
   private int nbGames;
   /** average duration */
   private double avgDuration;
   /** total points */
   private int points;
   /** number of wins */
   private int wins;
   /** number of losses */
   private int losses;
   /** number of ties */
   private int ties;
   /** number of cancels */
   private int cancels;

   /**
    * @return the average duration of games.
    */
   public double getAvgDuration() {
      return avgDuration;
   }

   /**
    * @return the number of cancelled games.
    */
   public int getCancels() {
      return cancels;
   }

   /**
    * @return the game type
    */
   public GameType getGametype() {
      return gametype;
   }

   /**
    * @return the number of losses.
    */
   public int getLosses() {
      return losses;
   }

   /**
    * @return the number of games.
    */
   public int getNbGames() {
      return nbGames;
   }

   /**
    * @return a reference to the player of this stats.
    */
   public User getPlayer() {
      return player;
   }

   /**
    * @return the number of points.
    */
   public int getPoints() {
      return points;
   }

   /**
    * @return the number of ties.
    */
   public int getTies() {
      return ties;
   }

   /**
    * @return the number of wins.
    */
   public int getWins() {
      return wins;
   }

   /**
    * @param d the average duration.
    */
   public void setAvgDuration(double d) {
      avgDuration = d;
   }

   /**
    * @param i the number of cancelled games.
    */
   public void setCancels(int i) {
      cancels = i;
   }

   /**
    * @param type the game type.
    */
   public void setGametype(GameType type) {
      gametype = type;
   }

   /**
    * @param i the number of losses.
    */
   public void setLosses(int i) {
      losses = i;
   }

   /**
    * @param i the total number of games.
    */
   public void setNbGames(int i) {
      nbGames = i;
   }

   /**
    * @param user the player/user.
    */
   public void setPlayer(User user) {
      player = user;
   }

   /**
    * @param i the number of points
    */
   public void setPoints(int i) {
      points = i;
   }

   /**
    * @param i the number of tied games.
    */
   public void setTies(int i) {
      ties = i;
   }

   /**
    * @param i the number of wins.
    */
   public void setWins(int i) {
      wins = i;
   }

}
