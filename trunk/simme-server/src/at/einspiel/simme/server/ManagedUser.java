package at.einspiel.simme.server;

import at.einspiel.base.Result;
import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.messaging.LoginResult;
import at.einspiel.messaging.Message;
import at.einspiel.simme.client.Move;

/**
 * A user that is currently online. In addition to the members found in
 * {@link User} this class also stores state and activity.
 * 
 * @author kariem
 */
public class ManagedUser extends User {

    private static final int ACCURACY = 1000;

    private UserState state;
    private long lastStatusUpdate;
    private ManagedGame game;
    private Message clientMessage;

    /**
     * Simple default constructor.
     */
    public ManagedUser() {
        super();
    }

    /**
     * Creates a new <code>ManagedUser</code> with the given properties.
     * @param u the user
     */
    public ManagedUser(User u) {
        super(u.getNick(), u.getPwd(), u.getWinmsg(), u.getLang(), u.getInfo(),
                u.getLocation(), u.getClientmodel());
        init();
    }

    /**
     * Creates a new <code>ManagedUser</code> by querying the database with
     * the nickname.
     * 
     * @param nick the user's nick name.
     * @return the user with the corresponding nick name.
     * 
     * @throws UserException if the user with the given id cannot
     *         be found
     */
    public static ManagedUser getManagedUserByNick(String nick)
            throws UserException {
        ManagedUser user = new ManagedUser(User.getUserByNick(nick));
        user.init();
        return user;
    }

    /**
     * Creates a new <code>ManagedUser</code> by querying the database with
     * the nickname.
     * 
     * @param nick the user's nick name.
     * @param pwd the user's passwort.
     * @return either an already existing user with the given nick/password
     *         combination, or a newly created user.
     * @throws UserException if the user with the given id cannot
     *         be found
     */
    public static ManagedUser getManagedUser(String nick, String pwd)
            throws UserException {
        ManagedUser user = new ManagedUser(User.getUser(nick, pwd));
        user.init();
        return user;
    }

    private void init() {
        state = new UserState(this);
        lastStatusUpdate = System.currentTimeMillis();
    }

    /**
     * Starts the login procedure. The result will be returned.
     * 
     * @param version the SimME version of the client in use.
     * @return the result of the login procedure.
     */
    public LoginResult login(String version) {
        return SessionManager.getInstance().addUser(this, version);
    }

    /** Starts a game against the first available opponent. */
    public void startGame() {
        // first set the user to waiting 
        this.state.setStateCategory(UserState.STATE_WAITING);
        // ... user manager will initialize the game, if possible

    }

    /**
     * Returns the current state of the user.
     * 
     * @return The current state (see {@link UserState#getState()}).
     */
    UserState getUserState() {
        return state;
    }

    /**
     * Returns the seconds since the last update of this user was performed.
     * 
     * @return The time in seconds since the last update.
     */
    int secondsSinceLastUpdate() {
        return (int) ((System.currentTimeMillis() - lastStatusUpdate) / ACCURACY);
    }

    /**
     * Updates the user, setting the time of last update to the time of
     * executing this method.
     */
    public void update() {
        lastStatusUpdate = System.currentTimeMillis();
    }

    /**
     * Returns the users state
     * @return the user's state
     */
    UserState getState() {
        return state;
    }

    /**
     * Sets the game for this user.
     * @param game a reference to the game.
     */
    public void setGame(ManagedGame game) {
        this.game = game;
    }
    
    /**
     * Returns the current game.
     * @return the current game. If this user currently does not participate in
     *          a game, <code>null</code> is returned.
     */
    public ManagedGame getGame() {
       return game;
    }
    

    /**
     * Selects an edge on the currently running game.
     * @param m the move to perform.
     * @return the result of calling play, or <code>null</code> if the game is
     *          not running.
     */
    public Result makeMove(Move m) {
        if (game != null) {
            if (game.isRunning()) {
                return game.makeMove(this, m);
            }
        }
        return null;
    }
    
    /**
     * Returns whether this user is currently on turn.
     * @return <code>true</code> if he participates in a game which is running,
     *          and is on turn; <code>false</code> otherwise.
     */
    public boolean isOnTurn() {
       if (game != null) {
          if (game.isRunning()) {
             return game.isOnTurn(this);
          }
       }
       return false;
    }
}