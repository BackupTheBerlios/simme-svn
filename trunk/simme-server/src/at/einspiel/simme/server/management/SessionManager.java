package at.einspiel.simme.server.management;

import at.einspiel.simme.server.base.User;
import at.einspiel.simme.server.base.UserException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import test.sim.net.LoginResult;
import test.sim.net.SendableUI;

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

    private static final String MGMT_PAGE = "sessionsMgr.jsp";

    SortedMap users;
    UserManager stMgr;

    private static SessionManager instance;

    private String baseUrl;

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
        stMgr = new UserManager(users);
        stMgr.manage();
    }

    /**
     * Creates a message using <code>SendableUI</code>.
     * @param title the title.
     * @param message the text for the message.
     * @return an xml string containing the formatted message.
     */
    public static String makeMessage(String title, String message) {
       SendableUI sui = new SendableUI();
       sui.setTitle(title);
       sui.setText(message);
       return sui.getXmlString();
    }   

    /**
     * Adds a user to the list of managed users.
     * 
     * @param u the user.
     * @param version the user's version.
     * 
     * @return a login result
     */
    public LoginResult addUser(User u, String version) {
        String response = null;
        // TODO check version with most current version and inform client
        try {
            User userFromDB = User.getUser(u.getNick(), u.getPwd());
            if (!userFromDB.equals(u)) {
                u.saveToDB();
            }
            addManagedUser(new ManagedUser(u));
            response = "User signed on";
            return new LoginResult(true, response, baseUrl + MGMT_PAGE);
        } catch (UserException e) {
            response = e.getMessage();
            return new LoginResult(false, response, baseUrl + MGMT_PAGE);
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
        try {
            addManagedUser(ManagedUser.getManagedUser(nick, pwd));
            response = "User signed on";
            return new LoginResult(true, response, baseUrl + MGMT_PAGE);
        } catch (UserException e) {
            response = e.getMessage();
            return new LoginResult(false, response, baseUrl + MGMT_PAGE);
        }
    }

    /**
     * Adds the user to the list of managed users.
     * 
     * @param user The user which is added.
     */
    public void addManagedUser(ManagedUser user) {
        System.out.println("[" + System.currentTimeMillis() + "] adding " + user.getNick());
        stMgr.addUser(user);
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
        return stMgr.removeUser(nick);
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
     * Sets the base URL.
     * @param string the base URL.
     */
    public void setBaseUrl(String string) {
        if (!string.endsWith("/")) {
            string = string + "/";
        }
        this.baseUrl = string;
    }

    /**
     * Returns an iterator over the currently managed users
     * 
     * @return an <code>Iterator</code> running over currently managed users.
     */
    public Iterator userIterator() {
        return users.values().iterator();
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
    public static void setDefaults(int secondsIdle, int secondsWaiting, int updtInterval) {
        UserManager.setMaxSecondsIdle(secondsIdle);
        UserManager.setMaxSecondsWaiting(secondsWaiting);
        UserManager.setUpdateInterval(updtInterval);
    }
}
