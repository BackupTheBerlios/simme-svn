package at.einspiel.simme.server.management;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Manages the users. This class is used by {@linkplain SessionManager}.
 * 
 * @author kariem
 */
class UserManager {

    private static final int DEF_UPDATE_INTERVAL = 30000; // 30 seconds
    private static final int DEF_SECONDS_IDLE = 300; // 5 minutes
    private static final int DEF_SECONDS_WAITING = 600; // 10 minutes

    private static int updateInterval = DEF_UPDATE_INTERVAL;
    private static int maxSecondsIdle = DEF_SECONDS_IDLE;
    private static int maxSecondsWaiting = DEF_SECONDS_WAITING;

    private Map allUsers;
    UserUpdater updater;


    UserManager(Map users) {
        this.allUsers = users;
        // create new user updater that manages user states and their timeouts
        updater = new UserUpdater(allUsers.values());
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
     */
    public boolean removeUser(String nick) {
        synchronized (allUsers) {
            return allUsers.remove(nick) != null;
        }
    }

    /**
     * Starts managing
     */
    public void manage() {
        updater.start();
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
        }
        
        /** @see Thread#destroy() */
        public void destroy() {
            running = false;
        }
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