package at.einspiel.simme.server;

import at.einspiel.base.User;

/**
 * Represents a game that is managed by the server.
 * 
 * @author kariem
 */
public class ManagedGame extends ServerGame {

    /**
     * Creates a new <code>ManagedGame</code> with the given managed users.
     * 
     * @param p1 the first user.
     * @param p2 the second user.
     * @throws RuntimeException if both users are the same.
     */
    public ManagedGame(ManagedUser p1, ManagedUser p2) throws RuntimeException {
        super(p1, p2);
        p1.setGame(this);
        p2.setGame(this);
        startGame();
    }

    /**
     * Creates a new <code>ManagedGame</code> with the given users.
     * @param p1 the first user.
     * @param p2 the second user.
     * @throws Exception if both users are the same.
     * 
     * @see #ManagedGame(ManagedUser, ManagedUser)
     */
    public ManagedGame(User p1, User p2) throws Exception {
        this(new ManagedUser(p1), new ManagedUser(p2));
    }

    ManagedUser getPlayer1() {
        return (ManagedUser) player1;
    }

    ManagedUser getPlayer2() {
        return (ManagedUser) player2;
    }
}