// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SessionManager.java
//                  $Date: 2004/09/02 10:24:51 $
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import java.net.URL;
import java.util.*;

import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.db.UserDB;
import at.einspiel.messaging.LoginMessage;
import at.einspiel.messaging.SimpleClientMessage;
import at.einspiel.simme.server.menu.MenuCreateException;
import at.einspiel.simme.server.menu.MenuManager;

/**
 * This class is intended to be the primary interface to the client. A new user
 * logs into the system by calling {@linkplain #addUser(String, String)}with
 * username and password.
 * 
 * The class {@linkplain at.einspiel.simme.server.UserManager}is used for user
 * management.
 * 
 * @author kariem
 */
public class SessionManager {

	private static final String MGMT_PAGE = "sessionMgr";

	/**
	 * The set of users who are currently logged in. This set is shared with the
	 * user manager. The mapping is Nick => ManagedUser
	 */
	private SortedMap users;
	/** Manages the users. */
	private UserManager userManager;
	/** Loads the menu and serves menu requests. */
	private MenuManager menuManager;

	private static SessionManager instance;

	/**
	 * Ensures singleton
	 * 
	 * @return The existing instance if a <code>SessionManager</code> already
	 *         exists. Otherwise a new object of type
	 *         <code>SessionManager</code> will be instantiated.
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
	 * Loads the menu indicated by <code>menuXmlPath</code> and initializes
	 * the internal MenuManager.
	 * @param menuXmlPath
	 *            the path to the <code>menu.xml</code> file, as seen from the
	 *            server.
	 * @throws MenuCreateException
	 *             if the menu could not be created.
	 */
	public void loadMenu(URL menuXmlPath) throws MenuCreateException {
		menuManager = MenuManager.getMenuManager(menuXmlPath);
	}

	/**
	 * Shows, if a menu has already been loaded.
	 * @return <code>false</code> if no menu has been loaded yet;
	 *         <code>true</code> otherwise.
	 */
	public boolean menuLoaded() {
		return menuManager != null;
	}

	/**
	 * Creates a message using <code>SendableUI</code>.
	 * 
	 * @param title
	 *            the title.
	 * @param message
	 *            the text for the message.
	 * @return an xml string containing the formatted message.
	 */
	public static String makeMessage(String title, String message) {
		return new SimpleClientMessage(title, message).getMessage();
	}

	/**
	 * Adds a user to the list of managed users.
	 * 
	 * @param u
	 *            the user.
	 * @param version
	 *            the user's version.
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
	 * @param nick
	 *            The nickname of the new user.
	 * @param pwd
	 *            the user's pwd.
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
	 * 
	 * @param user
	 *            The user which is added.
	 */
	private void addManagedUser(ManagedUser user) {
		System.out.println("[" + System.currentTimeMillis() + "] adding " + user.getNick());
		userManager.addUser(user); // adding to user manager
	}

	/**
	 * Removes a user from the currently managed users.
	 * 
	 * @param nick
	 *            The nickname of the user.
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
	 * Returns a copy of the users currently managed by the session manager.
	 * 
	 * @return the users currently managed by the session manager. This list is
	 *         equal to the users, that are currently online (either waiting,
	 *         playing or just surfing).
	 */
	public List getUsers() {
		return new ArrayList(users.values());
	}

	/**
	 * Returns a copy of the users currently waiting for a game, or not playing.
	 * 
	 * @return a list of users that may be challenged for a game.
	 */
	public List getUsersAvailable() {
		List usersOnline = getUsers();
		for (Iterator i = usersOnline.iterator(); i.hasNext();) {
			ManagedUser mu = (ManagedUser) i.next();
			if (mu.isPlaying()) {
				i.remove();
			}
		}
		return usersOnline;
	}

	/**
	 * Sets the defaults for the user management.
	 * 
	 * @param secondsIdle
	 *            the maximum amount of seconds a user may stay idle.
	 * @param secondsWaiting
	 *            the maximum amount of seconds a user may wait.
	 * @param updtInterval
	 *            the update intervall.
	 */
	static void setDefaults(int secondsIdle, int secondsWaiting, int updtInterval) {
		UserManager.setMaxSecondsIdle(secondsIdle);
		UserManager.setMaxSecondsWaiting(secondsWaiting);
		UserManager.setUpdateInterval(updtInterval);
	}

	/**
	 * Returns an answer for the user identified by <code>userNick</code>.
	 * The user has to be logged in and in the correct state to perform the
	 * requested menu selection or action.
	 * 
	 * @param userNick
	 *            the user's nick name.
	 * @param menuId
	 *            the menu id.
	 * @param selection
	 *            the selection within the menu.
	 * @return an answer for the user.
	 */
	public String getAnswerFor(String userNick, String menuId, String selection) {
		// see, if user is online
		ManagedUser u = (ManagedUser) users.get(userNick);
		if (u == null) {
			// user is not online => inform client to log in again
			return makeMessage("Not logged in.", "The session for " + userNick
					+ " has expired, or you did not log in correctly."
					+ " Please try to reconnect to SimME online.");
		}

		// user is online and seems to be active
		if (selection != null) {
			// see if user has selected a menu element
			return menuManager.getMenu(menuId, Integer.parseInt(selection), u).getXml();
		}

		return menuManager.getMenu(menuId, u).getXml();
	}
}