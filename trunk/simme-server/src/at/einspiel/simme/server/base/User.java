package at.einspiel.simme.server.base;

import java.io.Serializable;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import at.einspiel.simme.server.db.Database;

/**
 * Represents a Simme user. Serializable to be used in JSPs.
 *
 * @author  kariem
 */
public class User implements Comparable, Serializable {

    private static final String TABLE_USER = "\"User\"";

    /** Deutsch */
    public static final byte LANG_DE = 0;
    /** English */
    public static final byte LANG_EN = 1;
    /** Español */
    public static final byte LANG_ES = 2;
    /** Français*/
    public static final byte LANG_FR = 3;
    /** Österreich */
    public static final String LOCATION_DEFAULT = "AT";

    static final String FIELD_NICK = "Nick";


    private String nick;
    private String pwd;
    private String winmsg;
    private byte lang;
    private String info;
    private String location;
    private String clientmodel;

    private static Database db = new Database();

    /** Creates an empty user which should be filled up */
    public User() {
        //    System.out.println("creating empty user");
    }

    /**
     * Creates a new <code>User</code> with the given properties.
     * @param name Nickname of user.
     * @param passwd Password of user.
         * @param winmessage Win message, written after game ending in success for user.
     * @param language Indicating user language.
     * @param information Additional information on user.
     * @param loc Additional information on user location.
     * @param client the model of the users's client.
     */
    public User(
        String name,
        String passwd,
        String winmessage,
        byte language,
        String information,
        String loc,
        String client) {
        setNick(name);
        setPwd(passwd);
        setWinmsg(winmessage);
        setLang(language);
        setInfo(information);
        setLocation(loc);
        setClientmodel(client);
    }

    /**
     * Creates a new <code>User</code> with nick and password. Other values are
     * set to default.
     *
     * @param name Nickname of user.
     * @param pass Password of user.
     */
    public User(String name, String pass) {
        this(name, pass, null, LANG_DE, null, LOCATION_DEFAULT, null);
    }

    /**
     * Loads a user from the database with the given nick name and password
     * combination. If the user did not exist yet, a new user with the given
     * password and nick name will be created.
     *
     * @param nick the user's nick name.
     * @param password the user's passwort.
     * @return either an already existing user with the given nick/password
     *         combination, or a newly created user.
     *
     * @throws UserException if the nick exists but the password doesn't
     *         match, or the new user could not be created.
     */
    public static User getUser(String nick, String password) throws UserException {

        User u = null;
        try {
            u = getUserByNick(nick);

            if (u.getPwd().equals(password)) {
                return u;
            } else {
                StringBuffer buf = new StringBuffer();
                buf.append("Ein User mit dem angegebenen Nick existiert bereits. ");
                buf.append("Das angegebene Passwort ist falsch.");
                throw new WrongUserException(buf.toString());
            }
        } catch (NoSuchUserException e) {
            u = new User(nick, password);
            try {
                u.insert();
                return u;
            } catch (SQLException e1) {
                throw new UserException(
                    "User nicht gefunden. Neuer Benutzer "
                        + "konnte aber nicht angelegt werden.");
            }
        }
    }

    /**
     * Loads a <code>User</code> from the database with the corresponding nick
     * name.
     *
     * @param nick The nick name of the user.
     *
     * @return the user identified by nick with all his data.
     *
     * @throws NoSuchUserException if the user with the given id cannot
     *         be found
     */
    public static User getUserByNick(String nick) throws NoSuchUserException {
        try {
            Map params = new HashMap();
            params.put(FIELD_NICK, nick);
            return buildUser(db.executeQuery(TABLE_USER, params));
        } catch (SQLException e) {
            throw new NoSuchUserException(nick);
        }
    }

    private static User buildUser(ResultSet rs) throws SQLException {
        /*
                 CREATE TABLE "User" (
                 Nick          varchar(10) NOT NULL PRIMARY KEY,
                 Password      varchar(10) NOT NULL,
                 WinMsg        varchar(20) DEFAULT 'Gutes Spiel',
                 PublicInfo    text,
                 Location      varchar(10) DEFAULT 'AT',
                 Client        varchar(12) DEFAULT 'unbekannt' NOT NULL
                 );
         */
        User u = new User();
        rs.next();
        u.setNick(rs.getString(FIELD_NICK));
        u.setPwd(rs.getString("Password"));
        u.setWinmsg(rs.getString("WinMsg"));
        u.setInfo(rs.getString("PublicInfo"));
        u.setLocation(rs.getString("Location"));
        u.setClientmodel(rs.getString("Client"));
        return u;
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
     * @param clientmodel The clientmodel to set
     */
    public void setClientmodel(String clientmodel) {
        this.clientmodel = clientmodel;
    }

    /**
     * Sets the info.
     * @param info The info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Sets the lang.
     * @param lang The lang to set
     */
    public void setLang(byte lang) {
        this.lang = lang;
    }

    /**
     * Sets the location.
     * @param location The location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the nick.
     * @param nick The nick to set
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Sets the password.
     * @param password The password to set
     */
    public void setPwd(String password) {
        this.pwd = password;
    }

    /**
     * Sets the winmsg.
     * @param winmsg The winmsg to set
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
     * Inserts the current user into the database.
     *
     * @return how many rows were inserted.
     *
     * @throws SQLException if an error occured.
     */
    private int insert() throws SQLException {
        Map attrs = buildAttributes();
        return db.insert(TABLE_USER, attrs.keySet(), attrs.values());
    }

    /**
     * Inserts the updates the current user in the database.
     *
     * @return how many rows were updated.
     *
     * @throws SQLException if an error occured.
     */
    private int update() throws SQLException {
        Map attrs = buildAttributes();
        Map where = new HashMap();
        where.put(FIELD_NICK, getNick());
        return db.update(TABLE_USER, attrs, where);
    }

    private Map buildAttributes() {
        /*
                 CREATE TABLE "User" (
                 Nick          varchar(10) NOT NULL PRIMARY KEY,
                 Password      varchar(10) NOT NULL,
                 WinMsg        varchar(20) DEFAULT 'Gutes Spiel',
                 PublicInfo    text,
                 Location      varchar(10) DEFAULT 'AT',
                 Client        varchar(12) DEFAULT 'unbekannt' NOT NULL
                 );
         */
        Map m = new HashMap();
        m.put(FIELD_NICK, getNick());
        m.put("Password", getPwd());
        m.put("WinMsg", getWinmsg());
        m.put("PublicInfo", getInfo());
        m.put("Location", getLocation());
        m.put("Client", getClientmodel());
        for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            if (entry.getValue() == null) {
                i.remove();
            }
        }

        return m;
    }

    /**
     * Deletes the current user from the database.
     *
     * @return how many rows were deleted.
     *
     * @throws SQLException if an error occured.
     */
    public int delete() throws SQLException {
        return db.delete(TABLE_USER, "NICK", getNick());
    }

    /**
     * Saves the user to the database (insert or update).
     *
     * @return how many rows were saved/updated.
     */
    public int saveToDB() {
        try {
            return insert();
        } catch (SQLException e) {
            try {
                return update();
            } catch (SQLException e1) {
                e.printStackTrace();
                e1.printStackTrace();
                return -1;
            }
        }
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User u = (User) obj;
            boolean same =
                getNick().equals(u.getNick())
                    && getPwd().equals(u.getPwd())
                    && getLocation().equals(u.getLocation())
                    && getClientmodel().equals(u.getClientmodel())
                    && getInfo().equals(u.getInfo());
            return same;
        }
        return false;
    }

}
