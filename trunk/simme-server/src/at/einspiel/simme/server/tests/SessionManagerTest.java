package at.einspiel.simme.server.tests;

import junit.framework.TestCase;
import at.einspiel.simme.server.base.User;
import at.einspiel.simme.server.base.UserException;
import at.einspiel.simme.server.management.ManagedUser;
import at.einspiel.simme.server.management.SessionManager;
import at.einspiel.simme.server.management.UserState;

/**
 * Class to test the session manager
 * 
 * @author kariem
 */
public class SessionManagerTest extends TestCase {

    private SessionManager sMgr;

    private String[] nicks = { "one", "two", "three", "four", "five", "six" };
    private String[] pwds = { "pwd1", "pwd2", "pwd3", "pwd4", "pwd5", "pwd6" };

    private ManagedUser[] users;

    /** @see TestCase#setUp() */
    protected void setUp() {
        SessionManager.setMaxSecondsIdle(1);
        SessionManager.setMaxSecondsWaiting(2);
        SessionManager.setUpdateInterval(100);
        sMgr = SessionManager.getInstance();
    }

    /**
     * Tests adding and removal.
     */
    public void testAddingAndRemoval() {
        printTitle("addingAndRemoval");
        for (int i = 0; i < nicks.length; i++) {
            sMgr.addUser(nicks[i], pwds[i]);
        }
        assertEquals(true, sMgr.removeUser(nicks[0]));
        
        // remove users
    }

    private static final int NB_USERS = 6;
    /**
     * Tests if not responding users are kicked away
     * @throws UserException if errors occur.
     */
    public void testAddNotRespondingUsers() throws UserException {
        printTitle("addNotRespondingUsers");
        users = new ManagedUser[NB_USERS];

        // add 6 users
        for (int i = 0; i < nicks.length; i++) {
            users[i] = ManagedUser.getManagedUser(nicks[i], pwds[i]);
            sMgr.addManagedUser(users[i]);
        }

        // 6 users in management
        assertEquals(NB_USERS, sMgr.getNumberOfUsers());

        // wait for more than a second
        try {
            System.out.println("[" + System.currentTimeMillis() + "] starting sleep");
            Thread.sleep(1500);
            System.out.println("[" + System.currentTimeMillis() + "] sleep finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // update two users, set "five" to WAITING
        users[4].setStateCategory(UserState.STATE_WAITING);
        users[3].update();

        // wait again for more than a second (sum > 2 seconds for non-updated users)
        try {
            System.out.println("[" + System.currentTimeMillis() + "] starting sleep");
            Thread.sleep(1500);
            System.out.println("[" + System.currentTimeMillis() + "] sleep finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // after more than 2 seconds users should be removed
        assertEquals(2, sMgr.getNumberOfUsers());

        // update both remaining users
        users[4].update();
        users[3].update();

        // wait for more than two seconds
        try {
            System.out.println("[" + System.currentTimeMillis() + "] starting sleep");
            Thread.sleep(2100);
            System.out.println("[" + System.currentTimeMillis() + "] sleep finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // only waiting user ("five") left
        assertEquals(1, sMgr.getNumberOfUsers());

        // remove users
        removeUsers();
    }

    void removeUsers() {
        try {
            for (int i = 0; i < nicks.length; i++) {
                User u = User.getUserByNick(nicks[i]);
                u.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private void printTitle(String s) {
        System.out.println("-----------" + s + "-----------");
    }

}
