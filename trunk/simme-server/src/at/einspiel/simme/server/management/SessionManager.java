package at.einspiel.simme.server.management;

import java.util.*;

import test.sim.net.LoginResult;
import at.einspiel.simme.server.base.User;
import at.einspiel.simme.server.base.UserException;

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

   private static int updateInterval = 30000; // 30 seconds
   private static int maxSecondsIdle = 300; // 5 minutes
   private static int maxSecondsWaiting = 600; // 10 minutes

   SortedMap users;
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
      users = Collections.synchronizedSortedMap(new TreeMap());
      updater = new UserUpdater(users.values());
      updater.start();
   }

   /**
    * Adds a user to the list of managed users.
    * 
    * @param u the user
    * 
    * @return a login result
    */
   public LoginResult addUser(User u) {
      String response = null;
      boolean success = false;
      try {
         User userCopy = User.getUser(u.getNick(), u.getPassword());
         if (!userCopy.equals(u)) {
            u.saveToDB();
         }
         addUser(new ManagedUser(u));
         response = "Benutzer angemeldet";
         success = true;
      } catch (UserException e) {
         success = false;
         response = e.getMessage();
      } finally {
         return new LoginResult(success, response);
      }
   }

   /**
    * Adds a user to the list of managed users.
    * 
    * @param nick The nickname of the new user.
    * @param pwd the user's pwd.
    * 
    * @return a login result
    */
   public LoginResult addUser(String nick, String pwd) {
      String response = null;
      boolean success = false;
      try {
         addUser(ManagedUser.getManagedUser(nick, pwd));
         response = "Benutzer angemeldet";
         success = true;
      } catch (UserException e) {
         success = false;
         response = e.getMessage();
      } finally {
         return new LoginResult(success, response);
      }
   }

   /**
    * Adds the user to the list of managed users.
    * 
    * @param user The user which is added.
    */
   public void addUser(ManagedUser user) {
      System.out.println(
         "[" + System.currentTimeMillis() + "] adding " + user.getNick());
      synchronized (users) {
         users.put(user.getNick(), user);
      }
   }

   /**
    * Removes a user from the currently managed users.
    * 
    * @param nick The nickname of the user.
    * 
    * @return <code>true</code> if the user was successfully removed,
    *         <code>false</code> otherwise.
    */
   public boolean removeUser(String nick) {
      System.out.println(
         "[" + System.currentTimeMillis() + "] removing " + nick);
      synchronized (users) {
         return users.remove(nick) != null;
      }
   }

   /**
    * Shows the number of users currently managed.
    * 
    * @return The number of users.
    */
   public int getNumberOfUsers() {
      return users.size();
   }

   /**
    * Returns an iterator over the currently managed users
    * 
    * @return an <code>Iterator</code> running over currently managed users.
    */
   public Iterator userIterator() {
      return users.values().iterator();
   }

   private class UserUpdater extends Thread {

      private boolean running;
      private Collection users;

      /**
       * Creates a new <code>UserUpdater</code> that updates the given users
       * regularly.
       * 
       * @param users list of users.
       */
      public UserUpdater(Collection users) {
         this.users = users;
         running = true;
      }

      /**
       * Currently this method removes users after a certain amount of not
       * responding according to their state.
       * 
       * @see Thread#run()
       */
      public void run() {
         while (running) {
            if (users.size() == 0) {
               try {
                  sleep(getUpdateInterval());
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               continue;
            }
            long timeStarted = System.currentTimeMillis();
            synchronized (users) {
               for (Iterator i = users.iterator(); i.hasNext();) {
                  ManagedUser mu = (ManagedUser) i.next();
                  switch (mu.getStateCategory()) {
                     case UserState.STATE_IDLE :
                        if (mu.secondsSinceLastUpdate()
                           > getMaxSecondsIdle()) {
                           i.remove();
                        }
                        break;

                     case UserState.STATE_WAITING :
                        if (mu.secondsSinceLastUpdate()
                           > getMaxSecondsWaiting()) {
                           i.remove();
                        }
                        break;

                     case UserState.STATE_PLAYING :
                        break;
                  }
               }
            }

            // see how much time was used for updates
            long timeUsed = System.currentTimeMillis() - timeStarted;

            // sleep for the rest of update interval
            if (getUpdateInterval() - timeUsed > 0) {
               try {
                  sleep(getUpdateInterval() - timeUsed);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
         }
      } /** @see Thread#destroy() */
      public void destroy() {
         running = false;
      }
   }

   //   // currently not needed
   //   private class UserStateComparator implements Comparator {
   //
   //      /**
   //       * Compares two ManagedUsers by comparing their state.
   //       * @see java.util.Comparator#compare(Object, Object)
   //       */
   //      public int compare(Object o1, Object o2) {
   //         ManagedUser mu1 = (ManagedUser) o1, mu2 = (ManagedUser) o2;
   //         return new Integer(mu1.getState()).compareTo(new Integer(mu2.getState()));
   //      }
   //   }

   /**
    * Returns the maximum time in seconds that idle users are allowed to not
    * respond or update their status
    * 
    * @return Time in seconds.
    */
   public static int getMaxSecondsIdle() {
      return maxSecondsIdle;
   }

   /**
    * Returns the maximum time in seconds that waiting users are allowed to not
    * respond or update their status
    * 
    * @return Time in seconds.
    */
   public static int getMaxSecondsWaiting() {
      return maxSecondsWaiting;
   }

   /**
    * Returns the update interval in milliseconds. This is the interval in
    * which the list of managed users is updated.
    * 
    * @return update interval.      
    */
   public static int getUpdateInterval() {
      return updateInterval;
   }

   /**
    * Sets the maximum time that idle users are allowed to not respond or update
    * their status.
    * 
    * @param i Time in seconds.
    */
   public static void setMaxSecondsIdle(int i) {
      maxSecondsIdle = i;
   }

   /**
    * Sets the maximum time that waiting users are allowed to not respond or
    * update their status.
    *
    * @param i Time in seconds.
    */
   public static void setMaxSecondsWaiting(int i) {
      maxSecondsWaiting = i;
   }

   /**
    * Set the update interval in milliseconds.
    * 
    * @param i Interval to update the managed users in milliseconds.
    */
   public static void setUpdateInterval(int i) {
      updateInterval = i;
   }

   /**
    * Returns the users.
    * 
    * @return the users.
    */
   public SortedMap getUsers() {
      return users;
   }

}
