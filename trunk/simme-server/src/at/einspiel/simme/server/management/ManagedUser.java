package at.einspiel.simme.server.management;

import at.einspiel.simme.server.base.User;
import at.einspiel.simme.server.base.UserException;

/**
 * A user that is currently online. In addition to the members found in
 * {@link User} this class also stores state and activity.
 * 
 * @author kariem
 */
public class ManagedUser extends User {

   private UserState state;
   private long lastStatusUpdate;

   /**
    * Creates a new <code>ManagedUser</code> with the given properties.
    * @param u the user
    */
   public ManagedUser(User u) {
      super(
         u.getNick(),
         u.getPassword(),
         u.getWinmsg(),
         u.getLang(),
         u.getInfo(),
         u.getLocation(),
         u.getClientmodel());
      init();
   }

   /**
    * Creates a new <code>ManagedUser</code> by querying the database with
    * the nickname.
    * 
    * @param nick the user's nick name.
    * @return the user with the corresponding nick name.
    * 
    * @throws UserException if the user with the given id cannot
    *         be found
    */
   public static ManagedUser getManagedUserByNick(String nick)
      throws UserException {
      ManagedUser user = new ManagedUser(User.getUserByNick(nick));
      user.init();
      return user;
   }

   /**
    * Creates a new <code>ManagedUser</code> by querying the database with
    * the nickname.
    * 
    * @param nick the user's nick name.
    * @param pwd the user's passwort.
    * @return either an already existing user with the given nick/password
    *         combination, or a newly created user.
    * @throws UserException if the user with the given id cannot
    *         be found
    */
   public static ManagedUser getManagedUser(String nick, String pwd)
      throws UserException {
      ManagedUser user = new ManagedUser(User.getUser(nick, pwd));
      user.init();
      return user;
   }

   private void init() {
      state = new UserState(this);
      lastStatusUpdate = System.currentTimeMillis();
   }

   /**
    * Sets the state of this user.
    * 
    * @param state The new state (see {@link UserState#setState(int)}).
    */
   public void setStateCategory(int state) {
      this.state.setStateCategory(state);
   }

   /**
    * Returns the current state of the user.
    * 
    * @return The current state (see {@link UserState#getState()}).
    */
   public int getStateCategory() {
      return state.getStateCategory();
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
      return state.getStateAsString();
   }

   /**
    * Returns the users state
    * @return the user's state
    */
   public UserState getState() {
      return state;
   }

}
