// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SessionManager.java
//                  $Date: 2004/04/06 22:28:31 $
//              $Revision: 1.5 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import java.util.*;

import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.db.UserDB;
import at.einspiel.messaging.LoginMessage;
import at.einspiel.messaging.SimpleClientMessage;

/**
 * This class is intended to be the primary interface to the client. A new 
 * user logs into the system by calling {@linkplain #addUser(String, String)}
 * with username and password.
 * 
 * The class {@linkplain at.einspiel.simme.server.UserManager} ist used for user
 * management. 
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
            User userFromDB = UserDB.getUser(u.getNick(), u.getPwd());
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
     * Adds a user to the list of managed users. Additionally the login message
     * will contain 
     * 
     * @param nick The nickname of the new user.
     * @param pwd the user's pwd.
     * 
     * @return a login result
     * 
     * @see #addUser(User, String)
     */
    public LoginMessage addUser(String nick, String pwd) {
        try {
            return addUser(ManagedUser.getManagedUser(nick, pwd), null);
        } catch (UserException e) {
            return new LoginMessage(false, e.getMessage(), MGMT_PAGE);
        }
    }

    /**
     * Adds the user to the list of managed users.
     * @param user The user which is added.
     */
    private void addManagedUser(ManagedUser user) {
        System.out.println("[" + System.currentTimeMillis() + "] adding " + user.getNick());
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
        System.out.println("[" + System.currentTimeMillis() + "] removing " + nick);
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
    static void setDefaults(int secondsIdle, int secondsWaiting, int updtInterval) {
        UserManager.setMaxSecondsIdle(secondsIdle);
        UserManager.setMaxSecondsWaiting(secondsWaiting);
        UserManager.setUpdateInterval(updtInterval);
    }
}