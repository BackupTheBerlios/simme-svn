// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: UserManager.java
//                  $Date: 2004/09/07 13:30:36 $
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------

package at.einspiel.simme.server;

import java.util.*;

/**
 * Manages the users. Each user who has logged in will added to a list of active
 * users. After some time of inactivity this user will be removed from the list.
 * 
 * The list is used to look up active users and to show their state (playing,
 * waiting for a game, chatting, ...).
 * 
 * @see SessionManager
 * 
 * @author kariem
 */
public class UserManager {

   private static final int DEF_UPDATE_INTERVAL = 30000; // 30 seconds
   private static final int DEF_GAME_INTERVAL = 10000; // 10 seconds
   private static final int DEF_SECONDS_IDLE = 300; // 5 minutes
   private static final int DEF_SECONDS_WAITING = 600; // 10 minutes

   private static int findGameInterval = DEF_GAME_INTERVAL;
   private static int updateInterval = DEF_UPDATE_INTERVAL;
   private static int maxSecondsIdle = DEF_SECONDS_IDLE;
   private static int maxSecondsWaiting = DEF_SECONDS_WAITING;

   private Map allUsers;
   private Map managedGames;
   StateUpdater updater;

   private int usersOnline;
   private int usersWaitingForGame;
   private int usersPlaying;
   
   private UserManager(Map users) {
      this.allUsers = users;
      // create new user updater that manages user states and their timeouts
      updater = new StateUpdater(allUsers.values());
      managedGames = new HashMap();
   }

   static UserManager createUserManager(Map users) {
      return new UserManager(users);
   }

   /**
    * Adds the user.
    * @param user the user.
    */
   public void addUser(ManagedUser user) {
      synchronized (allUsers) {
         allUsers.put(user.getNick(), user);
      }
   }

   /**
    * Removes the user with the nick name <code>nick</code>.
    * @param nick the user's nick name.
    * @return whether the user was removed.
    */
   public boolean removeUser(String nick) {
      synchronized (allUsers) {
         return allUsers.remove(nick) != null;
      }
   }

   /** Starts managing  */
   public void manage() {
      updater.start();
   }

   /**
    * Starts a game between two managed users
    * @param mu1 first user.
    * @param mu2 second user.
    */
   void startGame(ManagedUser mu1, ManagedUser mu2) {
      try {
         ManagedGame game = new ManagedGame(mu1, mu2);
         addGame(game);
      } catch (RuntimeException ex) {
         System.err.print(new Date() + ": "); // add date for err tracing
         ex.printStackTrace();
      }
   }
   
   void addGame(ManagedGame game) {
      managedGames.put(game.getId(), game);
   }
   
   boolean gameIsManaged(ManagedGame game) {
      if (game == null) {
         return false;
      }
      return managedGames.containsKey(game.getId());
   }

   /** 
    * Updates the states of the users, that are managed by this user manager.
    */
   private class StateUpdater extends Thread {

      private boolean running;
      private Collection users;
      private final Comparator c = new Comparator() {
         public int compare(Object o1, Object o2) {
            return ((ManagedUser) o1).secondsSinceLastUpdate()
                  - ((ManagedUser) o1).secondsSinceLastUpdate();
         }
      };

      private long timeToUpdate;
      private long timeToFind;

      /**
       * Creates a new <code>UserUpdater</code> that updates the given users
       * regularly.
       * 
       * @param userCollection list of users.
       */
      public StateUpdater(Collection userCollection) {
         this.users = userCollection;
      }

      /**
       * Currently this method removes users after a certain amount of not
       * responding according to their state.
       * 
       * @see Thread#run()
       */
      public void run() {
         running = true;
         while (running) {
            if (users.isEmpty()) {
               getSleep();
               continue;
            }
            findGames();
            updateState();

            // sleep for the rest of update interval
            getSleep();
         }
      }

      /** @see Thread#destroy() */
      public void destroy() {
         running = false;
      }

      /** Finds waiting players and starts games among them */
      void findGames() {
         if (timeToFind > 0) {
            return; // not ready yet
         }
         long timeStarted = System.currentTimeMillis();
         synchronized (users) {
            if (users.size() > 1) {
               List tmpSet = new ArrayList();
               // copy waiting users to sorted set
               for (Iterator i = users.iterator(); i.hasNext(); ) {
                  ManagedUser mu = (ManagedUser) i.next();
                  // add if user is in waiting state, and user's game is not
                  // managed yet
                  if (mu.isWaiting() && !gameIsManaged(mu.getGame())) {
                     tmpSet.add(mu);
                  }
               }
               // sort by seconds since last update
               Collections.sort(tmpSet, c);
               
               setUsersWaitingForGame(tmpSet.size());
               for (Iterator i = tmpSet.iterator(); i.hasNext(); ) {
                  ManagedUser mu1 = (ManagedUser) i.next();
                  if (i.hasNext()) { // get next user
                     ManagedUser mu2 = (ManagedUser) i.next();
                     startGame(mu1, mu2);
                  }
               }
            }
         }
         long timeUsed = System.currentTimeMillis() - timeStarted;
         timeToFind += getFindGameInterval() - timeUsed;
         if (timeToFind < 0) {
            timeToFind = 0;
         }
      }

      /** Removes inactive users */
      void updateState() {
         if (timeToUpdate > 0) {
            return; // not ready yet
         }
         long timeStarted = System.currentTimeMillis();
         synchronized (users) {
            int countPlaying = 0;
            for (Iterator i = users.iterator(); i.hasNext(); ) {
               ManagedUser mu = (ManagedUser) i.next();
               switch (mu.getUserState().getStateCategory()) {
                  case UserState.STATE_IDLE :
                     if (mu.secondsSinceLastUpdate() > getMaxSecondsIdle()) {
                     	// TODO log this
                     	//System.out.println(System.currentTimeMillis() + " " + mu + "\tremoved (idle)");
                        i.remove();
                     }
                     break;

                  case UserState.STATE_WAITING :
                     if (mu.secondsSinceLastUpdate() > getMaxSecondsWaiting()) {
                     	// TODO log this
                     	// System.out.println(System.currentTimeMillis() + " " + mu + "\tremoved (waiting)");
                        i.remove();
                     }
                     break;

                  case UserState.STATE_PLAYING :
                     countPlaying++;
                     break;
                  default :
                     throw new AssertionError("No such state category defined");
               }
            }
            setUsersOnline(users.size());
            setUsersPlaying(countPlaying);
         }
         long timeUsed = System.currentTimeMillis() - timeStarted;
         timeToUpdate += getUpdateInterval() - timeUsed;
         if (timeToUpdate < 0) {
            timeToUpdate = 0;
         }
      }

      /** sleeps until the next necessary update */
      private void getSleep() {
         long sleepTime = Math.min(timeToUpdate, timeToFind);
         try { // go to sleep again
            sleep(sleepTime);
            timeToUpdate -= sleepTime;
            timeToFind -= sleepTime;
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }

   }

   /*
    * 
    * Accessors for state managed
    * 
    */
   
   /**
    * @param usersOnline The usersOnline to set.
    */
   void setUsersOnline(int usersOnline) {
      this.usersOnline = usersOnline;
   }
   /**
    * @param usersPlaying The usersPlaying to set.
    */
   void setUsersPlaying(int usersPlaying) {
      this.usersPlaying = usersPlaying;
   }
   /**
    * @param usersWaitingForGame The usersWaitingForGame to set.
    */
   void setUsersWaitingForGame(int usersWaitingForGame) {
      this.usersWaitingForGame = usersWaitingForGame;
   }
   
   
   /*
    * 
    * Getters and setters
    * 
    */

   /**
    * Returns the maximum time in seconds that idle users are allowed to not
    * respond or update their status
    * 
    * @return Time in seconds.
    */
   static int getMaxSecondsIdle() {
      return maxSecondsIdle;
   }

   /**
    * Sets the maximum time that idle users are allowed to not respond or update
    * their status.
    * 
    * @param i Time in seconds.
    */
   static void setMaxSecondsIdle(int i) {
      maxSecondsIdle = i;
   }

   /**
    * Returns the maximum time in seconds that waiting users are allowed to not
    * respond or update their status
    * 
    * @return Time in seconds.
    */
   static int getMaxSecondsWaiting() {
      return maxSecondsWaiting;
   }

   /**
    * Sets the maximum time that waiting users are allowed to not respond or
    * update their status.
    *
    * @param i Time in seconds.
    */
   static void setMaxSecondsWaiting(int i) {
      maxSecondsWaiting = i;
   }

   /**
    * Returns the update interval in milliseconds. This is the interval in
    * which the list of managed users is updated.
    * 
    * @return update interval.      
    */
   static int getUpdateInterval() {
      return updateInterval;
   }

   /**
    * Set the update interval in milliseconds.
    * 
    * @param i Interval to update the managed users in milliseconds.
    */
   static void setUpdateInterval(int i) {
      updateInterval = i;
   }

   /**
    * Returns the game interval in milliseconds. This is the intervall in which
    * waiting players are associated with each other to play random games.
    * 
    * @return the find game interval.
    */
   static int getFindGameInterval() {
      return findGameInterval;
   }

   /**
    * Sets the find game interval
    * 
    * @param findGameInterval the new interval.
    */
   static void setFindGameInterval(int findGameInterval) {
      UserManager.findGameInterval = findGameInterval;
   }
   
   /**
    * @return Returns the usersOnline.
    */
   public int getUsersOnline() {
      return usersOnline;
   }
   /**
    * @return Returns the usersPlaying.
    */
   public int getUsersPlaying() {
      return usersPlaying;
   }
   /**
    * @return Returns the usersWaitingForGame.
    */
   public int getUsersWaitingForGame() {
      return usersWaitingForGame;
   }
}