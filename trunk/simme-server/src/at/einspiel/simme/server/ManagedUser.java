// ----------------------------------------------------------------------------
// [Simme-Server]
//       Java Source File: ManagedUser.java
//                  $Date: 2004/09/13 23:43:48 $
//              $Revision: 1.10 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.server;

import at.einspiel.base.Result;
import at.einspiel.base.User;
import at.einspiel.base.UserException;
import at.einspiel.db.UserDB;
import at.einspiel.messaging.LoginMessage;
import at.einspiel.messaging.IMessage;
import at.einspiel.mgmt.*;
import at.einspiel.simme.client.Move;

/**
 * <p>
 * A user that is currently online. In addition to the members found in
 * {@link User}this class also stores state and activity. The state uses the
 * class {@link UserState}. This class provides an interface ({@linkplain 
 * IChangeSupport} to update listeners of state changes.
 * </p>
 * 
 * @author kariem
 */
public class ManagedUser extends User implements IChangeSupport, IStateListener {

	private static final int ACCURACY = 1000;

	private final UserState state;
	private final ChangeSupport changeSupport;

	private long lastStatusUpdate;
	private ManagedGame game;
	private IMessage clientMessage;

	/**
	 * Creates a new instance of <code>ManagedUser</code>.
	 * @see User#User(String, String, String, byte, String, String, String)
	 */
	public ManagedUser(String name, String passwd, String winmessage, byte language,
			String information, String loc, String client) {
		super(name, passwd, winmessage, language, information, loc, client);

		state = new UserState();
		state.addStateListener(this);
		lastStatusUpdate = System.currentTimeMillis();
		changeSupport = new ChangeSupport();
	}

	/**
	 * Creates a new <code>ManagedUser</code> with the given properties.
	 * @param u
	 *            the user
	 */
	public ManagedUser(User u) {
		this(u.getNick(), u.getPwd(), u.getWinmsg(), u.getLang(), u.getInfo(), u.getLocation(),
				u.getClientmodel());
	}

	/**
	 * Simple default constructor.
	 */
	public ManagedUser() {
		this("");
	}

	/**
	 * Simple constructor with nick name.
	 * @param nick
	 */
	ManagedUser(String nick) {
		this(nick, null, null, LANG_DE, null, null, null);
	}

	/**
	 * Creates a new <code>ManagedUser</code> by querying the database with
	 * the nickname.
	 * 
	 * @param nick
	 *            the user's nick name.
	 * @return the user with the corresponding nick name.
	 * 
	 * @throws UserException
	 *             if the user with the given id cannot be found
	 */
	public static ManagedUser getManagedUserByNick(String nick) throws UserException {
		ManagedUser user = new ManagedUser(UserDB.getUserByNick(nick));
		return user;
	}

	/**
	 * Creates a new <code>ManagedUser</code> by querying the database with
	 * the nickname.
	 * 
	 * @param nick
	 *            the user's nick name.
	 * @param pwd
	 *            the user's passwort.
	 * @return either an already existing user with the given nick/password
	 *         combination, or a newly created user.
	 * @throws UserException
	 *             if the user with the given id cannot be found
	 */
	public static ManagedUser getManagedUser(String nick, String pwd) throws UserException {
		final User user = UserDB.getUser(nick, pwd);
		ManagedUser managedUser = new ManagedUser(user);
		return managedUser;
	}

	/**
	 * Starts the login procedure. The result will be returned.
	 * 
	 * @param version
	 *            the SimME version of the client in use.
	 * @return the result of the login procedure.
	 */
	public LoginMessage login(String version) {
		return SessionManager.getInstance().addUser(this, version);
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
	public void updateStatus() {
		lastStatusUpdate = System.currentTimeMillis();
		// TODO log this
		// System.out.println(lastStatusUpdate + " " + this + " updating
		// status");
	}

	/**
	 * Returns the users state
	 * @return the user's state
	 */
	public UserState getState() {
		return state;
	}

	/** Makes the user start playing a game. */
	public void startPlaying() {
		if (game != null) {
			state.startPlaying();
		}
	}
	
	/**
	 * Returns whether the user is playing.
	 * @return <code>true</code> if the user is playing, <code>false</code>
	 *         otherwise.
	 */
	public boolean isPlaying() {
		return state.isPlaying();
	}

	/**
	 * Return whether the user is waiting.
	 * 
	 * @return <code>true</code> if the user is waiting, <code>false</code>
	 *         otherwise.
	 */
	public boolean isWaiting() {
		return state.getStateCategory() == UserState.STATE_WAITING;
	}

	/**
	 * <p>
	 * Sets this user into "waiting" mode. If the user is managed by a user
	 * manager the user's "waiting" mode will be detected by the manager and the
	 * user will be added to game, if possible.
	 * </p>
	 * 
	 * <p>
	 * If the user currently participates in a game, this method returns without
	 * any changes.
	 * </p>
	 */
	public void waitForGame() {
		state.waitForGame();
	}

	/**
	 * Sets the game for this user.
	 * @param game
	 *            a reference to the game.
	 */
	public void setGame(ManagedGame game) {
		this.game = game;
		addStateListener(game);
	}

	/**
	 * Returns the current game.
	 * @return the current game. If this user currently does not participate in
	 *         a game, <code>null</code> is returned.
	 */
	public ManagedGame getGame() {
		return game;
	}

	/**
	 * Selects an edge on the currently running game. The game is started, if
	 * not set to running yet.
	 * @param m
	 *            the move to perform.
	 * @return the result of calling make move on the game. This method returns
	 *         <code>null</code> if the game is <code>null</code>.
	 */
	public Result makeMove(Move m) {
		updateStatus();
		if (game != null) {
			if (!game.isRunning()) {
				game.startGame();
			}
			return game.makeMove(this, m);
		}
		return null;
	}

	/**
	 * Returns whether this user is currently on turn.
	 * @return <code>true</code> if he participates in a game which is
	 *         running, and is on turn; <code>false</code> otherwise.
	 */
	public boolean isOnTurn() {
		if (game != null) {
			if (game.isRunning()) {
				return game.isPlayerOnTurn(this);
			}
		}
		return false;
	}

	/** @see IChangeSupport#addStateListener(IStateListener) */
	public void addStateListener(IStateListener listener) {
		changeSupport.addStateListener(listener);
	}

	/** @see IChangeSupport#removeStateListener(IStateListener) */
	public void removeStateListener(IStateListener listener) {
		changeSupport.removeStateListener(listener);
	}

	/** @see at.einspiel.mgmt.IStateListener#updateState(at.einspiel.mgmt.StateEvent) */
	public void updateState(StateEvent event) {
		event.setSource(this);
		changeSupport.fireStateEvent(event);
	}

	/**
	 * Returns the message for the client.
	 * @return the client message.
	 */
	public IMessage getClientMessage() {
		return clientMessage;
	}

	/**
	 * Sets the client message for this user.
	 * @param msg
	 *            the message to set.
	 */
	public void setClientMessage(IMessage msg) {
		this.clientMessage = msg;
	}
}