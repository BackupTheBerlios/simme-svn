// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ManagedUser.java
//                  $Date: 2004/09/02 10:25:10 $
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import at.einspiel.base.Result;
import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.db.UserDB;
import at.einspiel.messaging.LoginMessage;
import at.einspiel.messaging.Message;
import at.einspiel.mgmt.IChangeSupport;
import at.einspiel.mgmt.StateListener;
import at.einspiel.simme.client.Move;

/**
 * <p>A user that is currently online. In addition to the members found in
 * {@link User} this class also stores state and activity. The state uses the
 * class {@link UserState}. This class provides an interface ({@linkplain 
 * IChangeSupport} to update listeners of state changes.</p>
 * 
 * @author kariem
 */
public class ManagedUser extends User implements IChangeSupport {

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
        init();
    }

    /**
     * Simple constructor with nick name.
     * @param nick
     */
    ManagedUser(String nick) {
        super();
        setNick(nick);
        init();
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
    public static ManagedUser getManagedUserByNick(String nick) throws UserException {
        ManagedUser user = new ManagedUser(UserDB.getUserByNick(nick));
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
    public static ManagedUser getManagedUser(String nick, String pwd) throws UserException {
        final User user = UserDB.getUser(nick, pwd);
        ManagedUser managedUser = new ManagedUser(user);
        return managedUser;
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
    public LoginMessage login(String version) {
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
     * @return The current state.
     */
    UserState getUserState() {
        return state;
    }

    /**
     * Returns the seconds since the last update of this user was performed.
     * 
     * @return The time in seconds since the last update.
     */
    public int secondsSinceLastUpdate() {
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
    public UserState getState() {
        return state;
    }

    /**
     * Returns whether the user is playing.
     * @return <code>true</code> if the user is playing, <code>false</code>
     *          otherwise.
     */
    public boolean isPlaying() {
        return state.isPlaying();
    }

    /**
     * Return whether the user is waiting.
     * 
     * @return <code>true</code> if the user is waiting, <code>false</code>
     *          otherwise.
     */
    public boolean isWaiting() {
        return state.getStateCategory() == UserState.STATE_WAITING;
    }

    /**
     * <p>Sets this user into "waiting" mode. If the user is managed by a user
     * manager the user's "waiting" mode will be detected by the manager and the
     * user will be added to game, if possible.</p>
     * 
     * <p>If the user currently participates in a game, this method returns
     * without any changes.</p>. 
     */
    public void waitForGame() {
        state.waitForGame();
    }

    /**
     * Sets the game for this user.
     * @param game a reference to the game.
     */
    public void setGame(ManagedGame game) {
        this.game = game;
        addStateListener(game);
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
     * @return the result of calling make move on the game. This method returns
     *          <code>null</code> if the game is <code>null</code> or not
     *          running.
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
                return game.isPlayerOnTurn(this);
            }
        }
        return false;
    }

    /** @see IChangeSupport#addStateListener(StateListener) */
    public void addStateListener(StateListener listener) {
        state.addStateListener(listener);
    }

    /** @see IChangeSupport#removeStateListener(StateListener) */
    public void removeStateListener(StateListener listener) {
        state.removeStateListener(listener);
    }

    /**
     * Returns the message for the client.
     * @return the client message.
     */
    public Message getClientMessage() {
        return clientMessage;
    }

    /**
     * Sets the client message for this user.
     * @param msg the message to set.
     */
    public void setClientMessage(Message msg) {
        this.clientMessage = msg;
    }
}