package at.einspiel.simme.server.management;

import at.einspiel.simme.server.base.User;
import at.einspiel.simme.server.base.UserException;

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

    private static final int DEF_UPDATE_INTERVAL = 30000; // 30 seconds
    private static final int DEF_SECONDS_IDLE = 300; // 5 minutes
    private static final int DEF_SECONDS_WAITING = 600; // 10 minutes
    

    private static int updateInterval = DEF_UPDATE_INTERVAL; 
    private static int maxSecondsIdle = DEF_SECONDS_IDLE; // 5 minutes
    private static int maxSecondsWaiting = DEF_SECONDS_WAITING; // 10 minutes
    private static final String MGMT_PAGE = "sessionsMgr.jsp";

    SortedMap users;
    Thread updater;

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
        updater = new UserUpdater(users.values());
        updater.start();
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

    private class UserUpdater extends Thread {

        private boolean running;
        private Collection users;

        /**
         * Creates a new <code>UserUpdater</code> that updates the given users
         * regularly.
         * 
         * @param userCollection list of users.
         */
        public UserUpdater(Collection userCollection) {
            this.users = userCollection;
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
                                if (mu.secondsSinceLastUpdate() > getMaxSecondsIdle()) {
                                    i.remove();
                                }
                                break;

                            case UserState.STATE_WAITING :
                                if (mu.secondsSinceLastUpdate() > getMaxSecondsWaiting()) {
                                    i.remove();
                                }
                                break;

                            case UserState.STATE_PLAYING :
                                break;
                            default :
                                assert false : "No such state defined";
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
