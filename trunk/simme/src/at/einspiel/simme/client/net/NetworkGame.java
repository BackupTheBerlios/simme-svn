// ----------------------------------------------------------------------------
// [Simme]
//       Java Source File: NetworkGame.java
//                  $Date: 2004/06/07 09:27:25 $
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package at.einspiel.simme.client.net;

import at.einspiel.simme.client.GameOnePlayer;
import at.einspiel.simme.client.Move;
import at.einspiel.simme.nanoxml.XMLElement;

/**
 * A networked game. After executing a move, the move is sent over the network.
 * The game does not allow playing, before the opponent's move is received.
 * 
 * @author kariem
 */
public class NetworkGame extends GameOnePlayer {

    PlayRequest pr;
    Move localMove;
    Move remoteMove;
    
    /**
     * Creates a new instance of <code>NetworkGame</code>.
     * @param xmlInfo the meta information for this game.
     * @param nick the nick name.
     * @param defaultUrl the url used to communicate game information if no
     *        information is contained in the xml info object.
     */
    public NetworkGame(XMLElement xmlInfo, String nick, String defaultUrl) {
        super(xmlInfo);
        final String url = xmlInfo.getAttribute("url", defaultUrl);
        pr = new PlayRequest(getId(), nick, url);
    }

    /** @see GameOnePlayer#doOtherPlayersMove() */
    protected void doOtherPlayersMove() {
        remoteMove = pr.receiveMove();
        setEdgeOwner(remoteMove.getEdge());
        endTurn(remoteMove);
    }
    
    /** @see at.einspiel.simme.client.GameOnePlayer#informOtherPlayer(byte) */
    protected boolean informOtherPlayer(byte edgeIndex) {
        // create move
        localMove.setEdge(edgeIndex);
        // send move to server by using the play request
        return pr.sendMove(localMove);
    }
}
