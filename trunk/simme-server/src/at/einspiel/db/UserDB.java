// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: UserDB.java
//                  $Date: 2004/09/15 23:36:40 $
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package at.einspiel.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import at.einspiel.base.*;
import at.einspiel.base.User;

/**
 * @author kariem
 */
public class UserDB {

	/** user table */
	static final String TABLE_USER = "Player";

	/** Reference to database */
	private static Database db = new Database();

	/** Field name for nick name */
	private static final String FIELD_NICK = "Nick";

	/**
	 * Loads a user from the database with the given nick name and password
	 * combination. If the user did not exist yet, a new user with the given
	 * password and nick name will be created.
	 * 
	 * @param nick
	 *            the user's nick name.
	 * @param password
	 *            the user's passwort.
	 * @return either an already existing user with the given nick/password
	 *         combination, or a newly created user.
	 * 
	 * @throws UserException
	 *             if the nick exists but the password doesn't match, or the new
	 *             user could not be created.
	 */
	public static User getUser(String nick, String password) throws UserException {

		User u = null;
		try {
			u = getUserByNick(nick);

			if (u.getPwd().equals(password)) {
				return u;
			}
			StringBuffer buf = new StringBuffer();
			buf.append("Ein User mit dem angegebenen Nick existiert bereits. ");
			buf.append("Das angegebene Passwort ist falsch.");
			throw new WrongUserException(buf.toString());
		} catch (NoSuchUserException e) {
			u = new User(nick, password);
			try {
				insert(u);
				return u;
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new UserException("User nicht gefunden. Neuer Benutzer "
						+ "konnte aber nicht angelegt werden.");
			}
		}
	}

	/**
	 * Loads a <code>User</code> from the database with the corresponding nick
	 * name.
	 * 
	 * @param nick
	 *            The nick name of the user.
	 * 
	 * @return the user identified by nick with all his data.
	 * 
	 * @throws NoSuchUserException
	 *             if the user with the given id cannot be found
	 */
	public static User getUserByNick(String nick) throws NoSuchUserException {
		try {
			Map params = new HashMap(1);
			params.put(FIELD_NICK, nick);
			ResultSet rs = db.executeQuery(TABLE_USER, params);
			// get first result
			rs.next();
			return buildUser(rs);
		} catch (SQLException e) {
			throw new NoSuchUserException(nick);
		}
	}

	/**
	 * Creates a user from a result set
	 * 
	 * @param rs
	 *            the result set.
	 * @return a user with the information from the result set.
	 * @throws SQLException
	 *             if a database access error occurs. See {@linkplain
	 *             ResultSet#getString(java.lang.String)}.
	 */
	private static User buildUser(ResultSet rs) throws SQLException {
		/*
		 * CREATE TABLE Player ( Nick varchar(10) NOT NULL PRIMARY KEY, Password
		 * varchar(10) NOT NULL, WinMsg varchar(20) DEFAULT 'Gutes Spiel',
		 * PublicInfo text, Location varchar(10) DEFAULT 'AT', Client
		 * varchar(12) DEFAULT 'unbekannt' NOT NULL );
		 */
		User u = new User(rs.getString(FIELD_NICK), rs.getString("Password"), rs
				.getString("WinMsg"), rs.getString("PublicInfo"), rs.getString("Location"), rs
				.getString("Client"));
		return u;
	}

	/**
	 * Creates an array of users from a result set
	 * 
	 * @param rs
	 *            the result set. The position of the cursor should be before
	 *            the first row.
	 * 
	 * @return an array of users, that corresponds to the list of users in
	 *         <code>rs</code>
	 * @throws SQLException
	 *             if a database access error occurs.
	 */
	private static User[] buildUsers(ResultSet rs) throws SQLException {
		List userList = new ArrayList();
		while (rs.next()) {
			userList.add(buildUser(rs));
		}
		return (User[]) userList.toArray(new User[userList.size()]);
	}

	/**
	 * Returns all users, that are currently in the database.
	 * 
	 * @return all users currently in the database.
	 * @throws UserException
	 *             if an error occured while trying to load the users.
	 */
	public static User[] getAllUsers() throws UserException {
		try {
			ResultSet allUsers = db.selectAll(TABLE_USER);
			return buildUsers(allUsers);
		} catch (SQLException e) {
			throw new UserException("No users found", e);
		}
	}

	/**
	 * Inserts the current user into the database.
	 * 
	 * @param u
	 *            the user to be inserted.
	 * @return how many rows were inserted.
	 * 
	 * @throws SQLException
	 *             if an error occured.
	 */
	public static int insert(User u) throws SQLException {
		Map attrs = buildAttributes(u);
		return db.insert(TABLE_USER, attrs.keySet(), attrs.values());
	}

	/**
	 * Inserts the updates the current user in the database.
	 * 
	 * @param u
	 *            the user to be updated.
	 * @return how many rows were updated.
	 * 
	 * @throws SQLException
	 *             if an error occured.
	 */
	public static int update(User u) throws SQLException {
		Map attrs = buildAttributes(u);
		Map where = new HashMap();
		where.put(FIELD_NICK, u.getNick());
		return db.update(TABLE_USER, attrs, where);
	}

	/**
	 * Builds a map with user's attributes
	 * 
	 * @param u
	 *            the user.
	 * @return a map with the user's attributes.
	 */
	private static Map buildAttributes(User u) {
		/*
		 * CREATE TABLE Player ( Nick varchar(10) NOT NULL PRIMARY KEY, Password
		 * varchar(10) NOT NULL, WinMsg varchar(20) DEFAULT 'Gutes Spiel',
		 * PublicInfo text, Location varchar(10) DEFAULT 'AT', Client
		 * varchar(12) DEFAULT 'unbekannt' NOT NULL );
		 */
		Map m = new HashMap();
		m.put(UserDB.FIELD_NICK, u.getNick());
		m.put("Password", u.getPwd());
		m.put("WinMsg", u.getWinmsg());
		m.put("PublicInfo", u.getInfo());
		m.put("Location", u.getLocation());
		m.put("Client", u.getClientmodel());
		for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			if (entry.getValue() == null) {
				i.remove();
			}
		}

		return m;
	}

	/**
	 * Deletes the given user.
	 * 
	 * @param u
	 *            the user to be deleted.
	 * @return how many rows were deleted.
	 * @throws SQLException
	 *             if an error occured while deleting.
	 */
	public static int delete(User u) throws SQLException {
		return db.delete(TABLE_USER, "NICK", u.getNick());
	}

}