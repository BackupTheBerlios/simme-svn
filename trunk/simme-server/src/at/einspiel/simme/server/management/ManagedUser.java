package at.einspiel.simme.server.management;

import at.einspiel.simme.server.base.NoSuchUserException;
import at.einspiel.simme.server.base.User;

/**
 * A user that is currently online. In addition to the members found in
 * {@link User} this class also stores state and activity.
 * 
 * @author kariem
 */
public class ManagedUser extends User {

   /** user has just logged in and has not yet started a game */
   public static final int STATE_IDLE = 0;
   /** user is waiting for other players to play with him */
   public static final int STATE_WAITING = 1;
   /** user is playing */
   public static final int STATE_PLAYING = 2;

   private int state;
   private long lastStatusUpdate;

   /**
    * Creates a new <code>ManagedUser</code> by querying the database with
    * the nickname.
    * 
    * @param nick The nick name of the user.
    * 
    * @throws NoSuchUserException if the user with the given id cannot
    *         be found
    */
   public ManagedUser(String nick) throws NoSuchUserException {
      super(nick);
      state = STATE_IDLE;
      lastStatusUpdate = System.currentTimeMillis();
   }

   /**
    * Sets the state of this user.
    * 
    * @param state The new state. One of {@link #STATE_IDLE},
    *        {@link #STATE_WAITING}, {@link #STATE_PLAYING}.
    */
   public void setState(int state) {
      if ((state >= 0) && (state <= 2)) {
         this.state = state;
         update();
      }
   }

   /**
    * Returns the current state of the user.
    * 
    * @return The current state. One of {@link #STATE_IDLE},
    *         {@link #STATE_WAITING}, {@link #STATE_PLAYING}.
    */
   public int getState() {
      return state;
   }

   /**
    * Returns the seconds since the last update of this user was performed.
    * 
    * @return The time in seconds since the last update.
    */
   public int secondsSinceLastUpdate() {
      return (int) ((System.currentTimeMillis() - lastStatusUpdate) / 1000);
   }

   /**
    * Updates the user, setting the time of last update to the time of
    * executing this method.
    */
   public void update() {
      lastStatusUpdate = System.currentTimeMillis();
   }

   /**
    * Shows the state of the user in a String representation.
    * 
    * @return User's state as String.
    */
   public String getStateAsString() {
      String state = "unknown";
      switch (getState()) {
         case STATE_IDLE :
            state = "idle";
            break;

         case STATE_PLAYING :
            state = "playing";
            break;

         case STATE_WAITING :
            state = "waiting";
            break;

      }
      return state;
   }
}
