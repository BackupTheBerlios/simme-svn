// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SessionManagerTest.java
//                  $Date: 2004/09/13 15:10:28 $
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import junit.framework.TestCase;

import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.db.UserDB;

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

    private static final int UPDATE_INTERVAL = 100;
    private static final int TIMEOUT1 = 1500;
    private static final int TIMEOUT2 = 2100;

    /** @see TestCase#setUp() */
    protected void setUp() {
        SessionManager.setDefaults(1, 2, UPDATE_INTERVAL);
        sMgr = SessionManager.getInstance();
    }

    /**
     * Tests adding and removal.
     */
    public void testAddingAndRemoval() {
        for (int i = 0; i < nicks.length; i++) {
            sMgr.addUser(nicks[i], pwds[i]);
        }
        // remove users
        assertEquals(true, sMgr.removeUser(nicks[0]));
    }

    private static final int NB_USERS = 6;
    /**
     * Tests if not responding users are kicked away
     * @throws UserException if errors occur.
     */
    public void testAddNotRespondingUsers() throws UserException {
        users = new ManagedUser[NB_USERS];

        // add 6 users
        for (int i = 0; i < nicks.length; i++) {
            users[i] = ManagedUser.getManagedUser(nicks[i], pwds[i]);
            users[i].login(null); // no version
        }

        // 6 users in management
        assertEquals(NB_USERS, sMgr.getNumberOfUsers());

        // set "five" to WAITING
        users[4].startGame();

        // wait for more than a second
        try {
            Thread.sleep(TIMEOUT1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // update user "four"
        users[3].updateStatus();

        // wait again for more than a second (sum > 2 seconds for non-updated users)
        try {
            Thread.sleep(TIMEOUT1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // after more than 2 seconds users should be removed
        assertEquals("Users online: " + sMgr.getUsers(), 2, sMgr.getNumberOfUsers());

        // update both remaining users
        users[4].updateStatus();
        users[3].updateStatus();

        // wait for more than two seconds
        try {
            Thread.sleep(TIMEOUT2);
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
                User u = UserDB.getUserByNick(nicks[i]);
                u.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}