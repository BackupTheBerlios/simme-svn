/************************************************************************
** PROJECT:   Simme-Server
** $Workfile: ManagedGame.java $
** $Revision: 1.1 $
** LANGUAGE:  Java, JDK version 1.4
**
** COPYRIGHT: Frequentis Nachrichtentechnik Ges.m.b.H.
**            Spittelbreitengasse 34
**            A-1120 VIENNA
**            AUSTRIA
**            tel +43 1 811 50-0
**
** The copyright to the computer program(s) herein
** is the property of Frequentis Nachrichtentechnik
** Ges.m.b.H., Austria.
** The program(s) shall not be used and/or copied without
** the written permission of Frequentis Nachrichtentechnik.
**
** $Log: ManagedGame.java,v $
** Revision 1.1  2003/09/19 14:35:17  kariem
** user management
**
**
************************************************************************/

package at.einspiel.simme.server.management;

import at.einspiel.simme.server.base.ServerGame;
import at.einspiel.simme.server.base.User;

/**
 * Represents a game managed by the server.
 * 
 * @author kariem
 */
public class ManagedGame extends ServerGame {

    /**
     * Creates a new <code>ManagedGame</code> with the given managed users.
     * 
     * @param p1 the first user.
     * @param p2 the second user.
     * @throws Exception if both users are the same.
     */
    public ManagedGame(ManagedUser p1, ManagedUser p2) throws Exception {
        super(p1, p2);
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
