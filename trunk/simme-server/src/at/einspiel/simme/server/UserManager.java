package at.einspiel.simme.server;

import java.util.*;

/**
 * Manages the users. This class is used by {@linkplain SessionManager}.
 * 
 * @author kariem
 */
class UserManager {

    private static final int DEF_UPDATE_INTERVAL = 30000; // 30 seconds
    private static final int DEF_GAME_INTERVAL = 10000; // 10 seconds
    private static final int DEF_SECONDS_IDLE = 300; // 5 minutes
    private static final int DEF_SECONDS_WAITING = 600; // 10 minutes

    private static int findGameInterval = DEF_GAME_INTERVAL;
    private static int updateInterval = DEF_UPDATE_INTERVAL;
    private static int maxSecondsIdle = DEF_SECONDS_IDLE;
    private static int maxSecondsWaiting = DEF_SECONDS_WAITING;

    private Map allUsers;
    StateUpdater updater;

    UserManager(Map users) {
        this.allUsers = users;
        // create new user updater that manages user states and their timeouts
        updater = new StateUpdater(allUsers.values());
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
        // TODO start game with mu mu2
        try {
            // mu1 is the first player
            ManagedGame game = new ManagedGame(mu1, mu2);
            game.initializeGame();
        } catch (RuntimeException ex) {
            System.err.print(new Date() + ": "); // add date for err tracing
            ex.printStackTrace();
        }

    }

    private class StateUpdater extends Thread {

        private boolean running;
        private Collection users;
        private Comparator c = new Comparator() {
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
                    SortedSet tmpSet = new TreeSet(c);
                    // copy waiting users to sorted set
                    for (Iterator i = users.iterator(); i.hasNext();) {
                        ManagedUser mu = (ManagedUser) i.next();
                        if (mu.getUserState().getStateCategory() == UserState.STATE_WAITING) {
                            tmpSet.add(mu);
                        }
                    }
                    for (Iterator i = tmpSet.iterator(); i.hasNext();) {
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
                for (Iterator i = users.iterator(); i.hasNext();) {
                    ManagedUser mu = (ManagedUser) i.next();
                    switch (mu.getUserState().getStateCategory()) {
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
                            throw new AssertionError("No such state category defined");
                    }
                }
            }
            long timeUsed = System.currentTimeMillis() - timeStarted;
            timeToUpdate += getUpdateInterval() - timeUsed;
            if (timeToUpdate < 0) {
                timeToUpdate = 0;
            }
        }

        /** sleeps until the next necessary update */
        private void getSleep() {
            long sleepTime = Math.min(0, Math.min(timeToUpdate, timeToFind));
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
     * Sets the maximum time that idle users are allowed to not respond or update
     * their status.
     * 
     * @param i Time in seconds.
     */
    public static void setMaxSecondsIdle(int i) {
        maxSecondsIdle = i;
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
     * Sets the maximum time that waiting users are allowed to not respond or
     * update their status.
     *
     * @param i Time in seconds.
     */
    public static void setMaxSecondsWaiting(int i) {
        maxSecondsWaiting = i;
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
     * Set the update interval in milliseconds.
     * 
     * @param i Interval to update the managed users in milliseconds.
     */
    public static void setUpdateInterval(int i) {
        updateInterval = i;
    }

    /**
     * Returns the game interval in milliseconds. This is the intervall in which
     * waiting players are associated with each other to play random games.
     * 
     * @return the find game interval.
     */
    protected static int getFindGameInterval() {
        return findGameInterval;
    }

    /**
     * Sets the find game interval
     * 
     * @param findGameInterval the new interval.
     */
    protected static void setFindGameInterval(int findGameInterval) {
        UserManager.findGameInterval = findGameInterval;
    }
}