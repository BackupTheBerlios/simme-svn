package at.einspiel.simme.server.management;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

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
    * @param nick The nickname of the new user.
    * 
    * @return The managed user that was added.
    *     
    * @see ManagedUser#ManagedUser(String)
    */
   public ManagedUser addUser(String nick) {
      ManagedUser u;
      try {
         u = new ManagedUser(nick);
         addUser(u);
         return u;
      } catch (NoSuchUserException e) {
         e.printStackTrace();
      }
      return null;
   }

   /**
    * Adds the user to the list of managed users.
    * 
    * @param user The user which is added.
    */
   public void addUser(ManagedUser user) {
      System.out.println("[" + System.currentTimeMillis() + "] adding " + user.getNick());
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
      System.out.println("[" + System.currentTimeMillis() + "] removing " + nick);
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
            // TODO No updates for a long time => contact users (own thread)

            synchronized (users) {
               for (Iterator i = users.iterator(); i.hasNext();) {
                  ManagedUser mu = (ManagedUser) i.next();
                  switch (mu.getState()) {
                     case ManagedUser.STATE_IDLE :
                        if (mu.secondsSinceLastUpdate() > getMaxSecondsIdle()) {
                           i.remove();
                        }
                        break;

                     case ManagedUser.STATE_WAITING :
                        if (mu.secondsSinceLastUpdate() > getMaxSecondsWaiting()) {
                           i.remove();
                        }
                        break;

                     case ManagedUser.STATE_PLAYING :
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

   private class UserStateComparator implements Comparator {

      /**
       * Compares two ManagedUsers by comparing their state.
       * @see java.util.Comparator#compare(Object, Object)
       */
      public int compare(Object o1, Object o2) {
         ManagedUser mu1 = (ManagedUser) o1, mu2 = (ManagedUser) o2;
         return new Integer(mu1.getState()).compareTo(new Integer(mu2.getState()));
      }
   }

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

}
