package at.einspiel.simme.server.management;

import java.util.LinkedList;
import java.util.List;

import at.einspiel.simme.server.base.NoSuchUserException;

/**
 * This class is intended to be used for User management. Each user who
 * has logged in will added to a list of active users. After some time of
 * inactivity this user will be removed from the list.
 * 
 * The list is used to look up active users and to show their state (playing,
 * waiting for a game, chatting, ...).
 * 
 * @author kariem
 *
 */
public class SessionManager {

   List users;
   Thread updater;

   private static SessionManager instance;

   /**
    * Ensures singleton
    * 
    * @return The existing instance if a <code>SessionManager</code> already
    * exists. Otherwise a new object of type <code>SessionManager</code> will
    * be instantiated.
    */
   public static SessionManager getInstance() {
      if (instance == null) {
         instance = new SessionManager();
      }
      return instance;
   }

   private SessionManager() {
      init();
   }

   /** Initializes this object. */
   private void init() {
      users = new LinkedList(); // RETHINK perhaps ArrayList better ?
      updater = new UserUpdater(users);
   }

   /**
    * Adds a user to the list of managed users.
    * 
    * @param nick The nickname of the new user.
    * 
    * @throws NoSuchUserException is thrown if the nick of the new user
    *         cannot be found in the database.
    * 
    * @see ManagedUser#ManagedUser(String)
    */
   public void addUser(String nick) throws NoSuchUserException {
      ManagedUser u = new ManagedUser(nick);
      addUser(u);
   }

   /**
    * Adds the user to the list of managed users.
    * 
    * @param user The user which is added.
    */
   public void addUser(ManagedUser user) {
      users.add(user);
   }

   private class UserUpdater extends Thread {

      private final int updateInterval;
      private boolean running;
      private List users;

      /**
       * Creates a new <code>UserUpdater</code> that updates the given users
       * regularly.
       * 
       * @param users list of users.
       */
      public UserUpdater(List users) {
         this(users, 30000);
         running = true;
      }

      /**
       * Creates a new <code>UserUpdater</code> that updates the given users
       * regularly.
       * 
       * @param users list of users.
       * 
       * @param updateInterval the time in milliseconds to update the list.
       */
      public UserUpdater(List users, int updateInterval) {
         this.users = users;
         this.updateInterval = updateInterval;
         running = true;
      }

      /** @see Thread#run() */
      public void run() {
         while (running) {
            long timeStarted = System.currentTimeMillis();
            /*
             * TODO go through list of users and look for recent updates
             * TODO No updates for a long time => contact users (own thread)
             * TODO User not responding => remove from list (relogin)
             */

            // see how much time was used for updates
            long timeUsed = System.currentTimeMillis() - timeStarted;

            // sleep for the rest of update interval
            if (updateInterval - timeUsed > 0) {
               try {
                  sleep(updateInterval - timeUsed);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
         }
      }

      /** @see Thread#destroy() */
      public void destroy() {
         running = false;
      }
   }

}
