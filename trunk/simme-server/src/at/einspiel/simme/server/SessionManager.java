// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: SessionManager.java
//                  $Date: 2004/09/13 15:17:12 $
//              $Revision: 1.9 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import java.net.URL;
import java.util.*;

import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.db.UserDB;
import at.einspiel.messaging.IConstants;
import at.einspiel.messaging.LoginMessage;
import at.einspiel.messaging.SimpleClientMessage;
import at.einspiel.simme.server.menu.IMenu;
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

	private static final String MGMT_PAGE = "SessionMgr";

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
	 * @param mu
	 *            the user.
	 * @param version
	 *            the user's version.
	 * 
	 * @return a login result
	 */
	public LoginMessage addUser(ManagedUser mu, String version) {
		String response = "User signed in";
		// TODO check version with most current version and inform client
		boolean success = true;
		try {
			User userFromDB = UserDB.getUser(mu.getNick(), mu.getPwd());
			if (!userFromDB.equals(mu)) {
				mu.saveToDB(); // update user in database
			}
			addManagedUser(mu);
		} catch (UserException e) {
			response = e.getMessage();
			success = false;
		}
		return new LoginMessage(success, response, MGMT_PAGE);
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
	 * @see #addUser(ManagedUser, String)
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
		// TODO log this
		// System.out.println(System.currentTimeMillis() + " adding " +
		// user.getNick());
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
		// TODO log this
		// System.out.println("[" + System.currentTimeMillis() + "] removing " +
		// nick);
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
	 * Returns the ManagedUser object with the specified nick, if the user is
	 * online.
	 * 
	 * @param nick
	 *            the user's nick name.
	 * @return the user associated with the specified nick name, or
	 *         <code>null</code> if the user does not exist or does not have
	 *         an active session with the session manager.
	 */
	public ManagedUser getOnlineUser(String nick) {
		return (ManagedUser) users.get(nick);
	}

	/**
	 * Returns the game with the given game id.
	 * @param gameId
	 *            the game's identification.
	 * @return the game identified by the given id, or <code>null</code> if no
	 *         such game was found.
	 */
	public ManagedGame getGame(String gameId) {
		return (ManagedGame) userManager.getGames().get(gameId);
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
	 * @param m
	 *            a map with all the request's parameters.
	 * @return an answer for the user.
	 */
	public String getAnswerFor(Map m) {

		// retrieve parameters
		String userNick = getParam(m, IConstants.PARAM_USER);
		String menuId = getParam(m, IConstants.PARAM_MENUID);
		String selection = getParam(m, IConstants.PARAM_SEL);
		String meta = getParam(m, IConstants.PARAM_META);

		// see, if user is online
		ManagedUser u = userNick == null ? null : (ManagedUser) users.get(userNick);
		if (u == null) {
			// user is not online => inform client to log in again
			return makeMessage("Not logged in.", "The session for " + userNick
					+ " has expired, or you did not log in correctly."
					+ " Please try to reconnect to SimME online.");
		}

		// user has done something, so update status
		u.updateStatus();

		IMenu result;

		// user is online and seems to be active
		if (selection != null) {
			// see if user has selected a menu element
			result = menuManager.getMenu(menuId, Integer.parseInt(selection), u);
		} else {
			result = menuManager.getMenu(menuId, u);
		}

		// add the meta tag to the XML generation
		return result.getXml(meta);
	}

	private String getParam(Map m, String paramName) {
		Object o = m.get(paramName);
		if (o == null) {
			return null;
		}
		return ((String[]) o)[0];
	}
}