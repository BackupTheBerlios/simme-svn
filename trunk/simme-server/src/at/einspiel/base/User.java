package at.einspiel.base;

import java.io.Serializable;
import java.sql.SQLException;

import at.einspiel.db.UserDB;

/**
 * Represents a Simme user. Serializable to be used in JSPs.
 * 
 * @author kariem
 */
public class User implements Comparable, Serializable {

	/** Deutsch */
	public static final byte LANG_DE = 0;
	/** English */
	public static final byte LANG_EN = 1;
	/** Español */
	public static final byte LANG_ES = 2;
	/** Français */
	public static final byte LANG_FR = 3;
	/** Österreich */
	public static final String LOCATION_DEFAULT = "AT";

	private String nick;
	private String pwd;
	private String winmsg;
	private byte lang;
	private String info;
	private String location;
	private String clientmodel;

	/** Creates an empty user which should be filled up with data. */
	public User() {
		// System.out.println("creating empty user");
	}

	/**
	 * Creates a new <code>User</code> with the given properties.
	 * 
	 * @param name
	 *            Nickname of user.
	 * @param passwd
	 *            Password of user.
	 * @param winmessage
	 *            Win message, written after game ending in success for user.
	 * @param language
	 *            Indicating user language.
	 * @param information
	 *            Additional information on user.
	 * @param loc
	 *            Additional information on user location.
	 * @param client
	 *            the model of the users's client.
	 */
	public User(String name, String passwd, String winmessage, byte language,
			String information, String loc, String client) {
		setNick(name);
		setPwd(passwd);
		setWinmsg(winmessage);
		setLang(language);
		setInfo(information);
		setLocation(loc);
		setClientmodel(client);
	}

	/**
	 * Creates a new <code>User</code> with the given properties. The language
	 * is set to german.
	 * 
	 * @param name
	 *            Nickname of user.
	 * @param passwd
	 *            Password of user.
	 * @param winmessage
	 *            Win message, written after game ending in success for user.
	 * @param information
	 *            Additional information on user.
	 * @param loc
	 *            Additional information on user location.
	 * @param client
	 *            the model of the users's client.
	 */
	public User(String name, String passwd, String winmessage, String information, String loc,
			String client) {
		this(name, passwd, winmessage, LANG_DE, information, loc, client);
	}

	/**
	 * Creates a new <code>User</code> with nick and password. Other values
	 * are set to default.
	 * 
	 * @param name
	 *            Nickname of user.
	 * @param passwd
	 *            Password of user.
	 */
	public User(String name, String passwd) {
		this(name, passwd, null, null, LOCATION_DEFAULT, null);
	}

	/**
	 * @return String
	 */
	public String getClientmodel() {
		return clientmodel;
	}

	/**
	 * @return String
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @return byte
	 */
	public byte getLang() {
		return lang;
	}

	/**
	 * @return String
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return String
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @return String
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @return String
	 */
	public String getWinmsg() {
		return winmsg;
	}

	/**
	 * Sets the clientmodel.
	 * 
	 * @param clientmodel
	 *            The clientmodel to set
	 */
	public void setClientmodel(String clientmodel) {
		this.clientmodel = clientmodel;
	}

	/**
	 * Sets the info.
	 * 
	 * @param info
	 *            The info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * Sets the lang.
	 * 
	 * @param lang
	 *            The lang to set
	 */
	public void setLang(byte lang) {
		this.lang = lang;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            The location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Sets the nick.
	 * 
	 * @param nick
	 *            The nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            The password to set
	 */
	public void setPwd(String password) {
		this.pwd = password;
	}

	/**
	 * Sets the winmsg.
	 * 
	 * @param winmsg
	 *            The winmsg to set
	 */
	public void setWinmsg(String winmsg) {
		this.winmsg = winmsg;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		if (o instanceof User) {
			User otherUser = (User) o;
			return otherUser.getNick().compareTo(getNick());
		}
		return 1;
	}

	/**
	 * Deletes the current user from the database.
	 * 
	 * @return how many rows were deleted.
	 * 
	 * @throws UserException
	 *             if user could not be deleted.
	 */
	public int delete() throws UserException {
		try {
			return UserDB.delete(this);
		} catch (SQLException e) {
			throw new UserException("User could not be deleted.", e, this);
		}
	}

	/**
	 * Saves the user to the database (insert or update).
	 * 
	 * @return how many rows were saved/updated.
	 * @throws UserException
	 *             if user could not be inserted.
	 */
	public int saveToDB() throws UserException {
		try {
			return UserDB.insert(this);
		} catch (SQLException e) {
			try {
				return UserDB.update(this);
			} catch (SQLException e1) {
				e.printStackTrace();
				e1.printStackTrace();
				throw new UserException("User could not be inserted.", e, this);
			}
		}
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User u = (User) obj;
			boolean same = nick.equals(u.nick) && pwd.equals(pwd);
			// && location.equals(location) && clientmodel.equals(clientmodel)
			// && info.equals(info);
			return same;
		}
		return false;
	}

}