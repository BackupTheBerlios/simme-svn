// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SessionManager.java
//                  $Date: 2003/12/30 23:04:47 $
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import java.util.*;

import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.messaging.LoginMessage;
import at.einspiel.messaging.SimpleClientMessage;

/**
 * This class is intended to be used for User management. Each user who
 * has logged in will added to a list of active users. After some time of
 * inactivity this user will be removed from the list.
 * 
 * The list is used to look up active users and to show their state (playing,
 * waiting for a game, chatting, ...).
 * 
 * @author kariem
 */
public class SessionManager {

   private static final String MGMT_PAGE = "sessionMgr.jsp";

   SortedMap users;
   UserManager userManager;

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
      userManager = UserManager.createUserManager(users);
      userManager.manage();
   }

   /**
    * Creates a message using <code>SendableUI</code>.
    * @param title the title.
    * @param message the text for the message.
    * @return an xml string containing the formatted message.
    */
   public static String makeMessage(String title, String message) {
      return new SimpleClientMessage(title, message).getMessage();
   }

   /**
    * Adds a user to the list of managed users.
    * 
    * @param u the user.
    * @param version the user's version.
    * 
    * @return a login result
    */
   public LoginMessage addUser(User u, String version) {
      String response = null;
      // TODO check version with most current version and inform client
      try {
         User userFromDB = User.getUser(u.getNick(), u.getPwd());
         if (!userFromDB.equals(u)) {
            u.saveToDB();
         }
         addManagedUser(new ManagedUser(u));
         response = "User signed on";
         return new LoginMessage(true, response, MGMT_PAGE);
      } catch (UserException e) {
         response = e.getMessage();
         return new LoginMessage(false, response, MGMT_PAGE);
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
   public LoginMessage addUser(String nick, String pwd) {
      String response = null;
      try {
         addManagedUser(ManagedUser.getManagedUser(nick, pwd));
         response = "User signed on";
         return new LoginMessage(true, response, MGMT_PAGE);
      } catch (UserException e) {
         response = e.getMessage();
         return new LoginMessage(false, response, MGMT_PAGE);
      }
   }

   /* *
    * Sets the given user into the WAITING state. If another user is waiting
    * for a game to start, a new game will be created and the two players may
    * play. If no waiting user is available yet, <code>mu</code> will be added
    * to the waiting list.
    * 
    * @param mu the user
    * @return a string containing progress information for the client.
    *
    public String startGame(ManagedUser mu1, ManagedUser mu2) {
    if (mu.)
    }

    public String getMessage(ManagedUser mu) {
    
    }*/

   /**
    * Adds the user to the list of managed users.
    * @param user The user which is added.
    */
   private void addManagedUser(ManagedUser user) {
      System.out.println("[" + System.currentTimeMillis() + "] adding "
            + user.getNick());
      userManager.addUser(user); // adding to user manager
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
      System.out.println("[" + System.currentTimeMillis() + "] removing "
            + nick);
      return userManager.removeUser(nick);
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

   /**
    * Returns a copy of the current users.
    * 
    * @return the users.
    */
   public Collection getUsers() {
      return new ArrayList(users.values());
   }

   /**
    * Sets the defaults for the user management.
    * 
    * @param secondsIdle the maximum amount of seconds a user may stay idle.
    * @param secondsWaiting the maximum amount of seconds a user may wait.
    * @param updtInterval the update intervall.
    */
   static void setDefaults(int secondsIdle, int secondsWaiting,
         int updtInterval) {
      UserManager.setMaxSecondsIdle(secondsIdle);
      UserManager.setMaxSecondsWaiting(secondsWaiting);
      UserManager.setUpdateInterval(updtInterval);
   }
}