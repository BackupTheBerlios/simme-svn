//----------------------------------------------------------------------------
//[Simme]
//    Java Source File: Game.java
//               $Date: 2004/09/16 18:07:59 $
//           $Revision: 1.12 $
//----------------------------------------------------------------------------
package at.einspiel.simme.client;

import java.util.Random;

import at.einspiel.simme.client.ui.IDynamicUI;
import at.einspiel.simme.nanoxml.XMLElement;

/**
 * Represents a single game with all its states. Provides methods to start, end,
 * pause a game and uses a <code>Canvas</code> to draw itself to the display.
 * 
 * @author kariem
 */
public abstract class Game {

	private static final Random RANDOM = new Random();

	/** Amount of nodes. */
	public static final byte NB_NODES = 6;
	/** Amount of edges. */
	protected static final byte NB_EDGES = 15;

	/** Identification for neutral (not player 1 and not player 2). */
	public static final byte NEUTRAL = 0;
	/** Identification for player 1. */
	public static final byte PLAYER1 = 1;
	/** Identification for player 2. */
	public static final byte PLAYER2 = 2;

	// playing field
	/** nodes */
	private final Node[] nodes;
	/** edges between nodes */
	private final byte[] edges;

	// game information
	/** Indicates the currently active node */
	byte activeNode;
	/** Indicates the currently active player: true=p1, false=p2 */
	private boolean currentPlayer;
	/** Indicates if the game is over */
	protected boolean gameOver;
	/** The move number */
	byte moveNr;

	// game meta information
	/** The game meta information */
	private final GameInfo gameInfo;

	// information for the user
	/** the move message */
	private String moveMessage;

	private IDynamicUI ui;

	/**
	 * Initializes a new game. The game has to be started manually.
	 * 
	 * @see #start()
	 */
	public Game() {
		this(null);
	}

	/**
	 * Initializes a new game with the values found in the given xml element.
	 * The game has to be started manually.
	 * 
	 * @param gameInfoXML
	 *            xml element that holds information about the game.
	 * @see #start()
	 */
	public Game(XMLElement gameInfoXML) {
		if (gameInfoXML == null) {
			this.gameInfo = new GameInfo();
		} else {
			this.gameInfo = new GameInfo(gameInfoXML);
		}

		// initialize playing field arrays
		nodes = new Node[NB_NODES];
		edges = new byte[NB_EDGES];
	}

	/**
	 * May be used to start/restart the game. Player 1 will be the first player.
	 * 
	 * @see #start(boolean)
	 * @see #startRandom()
	 */
	public void start() {
		start(true);
	}

	/**
	 * May be used to start/restart the game. Either player one or player two
	 * will be the first to make a move, the probability is the same.
	 * 
	 * @see #start(boolean)
	 * @see #start()
	 */
	public void startRandom() {
		start(RANDOM.nextInt() % 2 == 0);
	}

	/**
	 * Is used to start the game and determine the first player.
	 * 
	 * @param playerOneFirst
	 *            set this to <code>true</code> in order to start with player
	 *            one. If set to <code>false</code> player two will be the
	 *            first player.
	 */
	public void start(boolean playerOneFirst) {

		for (byte i = 0; i < NB_NODES; i++) {
			nodes[i] = new Node();
		}
		for (byte i = 0; i < NB_EDGES; i++) {
			edges[i] = 0;
		}

		activeNode = -1; // value, if there is no active node
		currentPlayer = playerOneFirst; // p1 is currentPlayer
		moveNr = 0; // first move
		gameOver = false;

		showCurrentPlayer();
	}

	/**
	 * Updates the move message to show the current player's turn. If the game
	 * is over, this method sets the move message to a text showing game's
	 * winner.
	 */
	protected final void showCurrentPlayer() {
		final String playerName = getPlayerName(currentPlayer);
		if (gameOver) {
			moveMessage = "Loser: " + playerName;
			/*
			 * if (currentPlayer) { moveMessage = "You lost."; } else {
			 * moveMessage = "You won this game!"; }
			 */
			return;
		}

		moveMessage = playerName + " ist am Zug";
	}

	/**
	 * Returns the player's name from the game information. If there is no game
	 * information, this method will return either <code>Player 1</code> or
	 * <code>Player 2</code>.
	 * 
	 * @param b
	 *            <code>true</code> to retrieve the name of player 1,
	 *            <code>false</code> to retrieve player 2's name.
	 * 
	 * @return the player name, found in the game information.
	 */
	private String getPlayerName(boolean b) {
		return b ? gameInfo.getP1Name() : gameInfo.getP2Name();
	}

	/**
	 * Returns the name of the player indicated by <code>b</code>. The
	 * information will be retrieved from the game's meta information.
	 * 
	 * @param b
	 *            one of {@link #PLAYER1}or {@link #PLAYER2}
	 * @return the name of the player.
	 */
	public String getPlayerName(byte b) {
		return getPlayerName(b == PLAYER1);
	}

	/**
	 * Selects a single node within the game. A node which is deactivated will
	 * be activated. If the node is already activated, it will be deactivated.
	 * 
	 * @param index
	 *            Should be in range of 0-5 (including), or this method will
	 *            throw an <code>ArrayIndexOutOfBoundsException</code>. It
	 *            indicates the node to be selected. If <code>index</code> is
	 *            <code>-1</code>, the last move will be undone.
	 * 
	 * @return <code>true</code>, if the operation suceeded (e.g. node was
	 *         activated/deactivated, edge was coloured). <code>false</code>,
	 *         if no changes have been made to the current game state.
	 */
	public boolean selectNode(byte index) {
		if (gameOver) {
			moveMessage = "Start new game!";

			return false;
		}

		if ((index < 0) || (index >= NB_NODES)) {
			moveMessage = "Wähle einen Knoten von 1 bis 6!";
			return false;
		}

		Node nodeAtIndex = nodes[index];

		if (nodeAtIndex.disabled) {
			// not possible to select a disabled node
			moveMessage = "Knoten " + (index + 1) + " kann nicht gewählt werden";
			return false;
		}

		// check state of active node

		if (activeNode == -1) { // no node is currently activated
			activeNode = index;
			nodeAtIndex.activated = true;

			moveMessage = null;

			return true;
		} else if (activeNode == index) { // active node is same as index
			// e.g. edge from Node 1 to Node 1
			// set node to inactive
			nodeAtIndex.activated = false;

			// set active node to nothing, and exit
			activeNode = -1;

			moveMessage = "Knoten " + (index + 1) + " bereits gewählt";

			return false;
		} else { // active node is different from selected index
			// select the second node
			return edgeTo(index);
		}
	}

	/**
	 * Selects the edge.
	 * 
	 * @param edgeIndex
	 *            the index of the edge to be selected.
	 * @return whether the edge was successfully selected.
	 */
	public final boolean selectEdge(byte edgeIndex) {
		byte owner = getEdgeOwner(edgeIndex);
		if (owner != NEUTRAL) {
			byte[] nodeIdxs = Move.getNodeIndices(edgeIndex);
			moveMessage = "Kante " + (nodeIdxs[0] + 1) + "-" + (nodeIdxs[1] + 1)
					+ " bereits gewählt!";
			return false;
		}

		doMove(edgeIndex);

		return true;
	}

	/**
	 * Performs the move for the specified edge. The owner of the edge will be
	 * the currenlty active player. Additionally the two nodes of the edge will
	 * be set to deactivated and the active node is reset.
	 * 
	 * @param edgeIndex
	 *            the index
	 */
	protected void doMove(byte edgeIndex) {
		// deselect nodes, if contained in edge
		byte[] edgeNodes = Move.getNodeIndices(edgeIndex);
		nodes[edgeNodes[0]].activated = false;
		nodes[edgeNodes[1]].activated = false;
		activeNode = -1;

		setEdgeOwner(edgeIndex);

		// switch players and see if someone has won
		Move m = new Move(edgeIndex);
		endTurn(m);
	}

	private boolean edgeTo(byte secondNode) {
		return selectEdge(Move.getEdgeIndex(activeNode, secondNode));
	}

	/** Searches for nodes to be disabled and sets them accordingly. */
	private void disableNodes() {
		Node n;

		for (byte i = 0; i < NB_NODES; i++) {
			n = nodes[i];

			// look for not yet disabled nodes
			if (!n.disabled) {
				boolean possibleConnection = false;

				for (byte j = 0; j < NB_NODES; j++) {
					if (i != j) {
						if (getEdgeOwner(i, j) == NEUTRAL) {
							possibleConnection = true;

							break;
						}
					}
				}

				if (!possibleConnection) {
					n.disabled = true;
				}
			}
		}
	}

	/**
	 * Ends a turn for this game. Connected edges are searched for a win
	 * condition. Afterwards the other player may make his move.
	 * 
	 * @param m
	 *            the move.
	 */
	protected final void endTurn(Move m) {
		// disable nodes to be disabled
		disableNodes();

		// only the player who just did his move may lose.
		byte player = currentPlayer ? PLAYER1 : PLAYER2;

		moveNr++;

		// if someone has lost, last edge drawn was deciding
		for (byte c = 0; c < NB_NODES; c++) {
			if ((c != m.n1) && (c != m.n2)) {
				// find triangle
				if ((getEdgeOwner(m.n1, c) == player) && (getEdgeOwner(m.n2, c) == player)) {
					gameOver = true;

					showCurrentPlayer();
					return;
				}
			}
		}

		switchPlayers();
		showCurrentPlayer();
		updateUI();
	}

	/**
	 * Switches from player 1 to player 2 and vice versa.
	 */
	protected final void switchPlayers() {
		currentPlayer = !currentPlayer;
	}

	/**
	 * Returns the current player.
	 * 
	 * @return the current player.
	 */
	public byte getPlayersTurn() {
		if (!gameOver) {
			return currentPlayer ? PLAYER1 : PLAYER2;
		}

		return NEUTRAL;
	}

	/**
	 * Shows if a single node is activated.
	 * 
	 * @param index
	 *            The index of the node.
	 * 
	 * @return <code>true</code> if the node at <code>index</code> is
	 *         activated, <code>false</code> if the node at <code>index</code>
	 *         is not activated or an invalid index (index&lt;0 or index&gt;5)
	 *         was supplied.
	 */
	public boolean isActivated(byte index) {
		if ((index < 0) || (index >= NB_NODES)) {
			return false;
		}

		return nodes[index].activated;
	}

	/**
	 * Shows if a single node is disabled.
	 * 
	 * @param index
	 *            The index of the node. currentPlayer = !currentPlayer;
	 * @return <code>true</code> if the node at <code>index</code> is
	 *         disabled, <code>false</code> if the node at <code>index</code>
	 *         is not disabled or an invalid index (index&lt;0 or index&gt;5)
	 *         was supplied.
	 */
	public boolean isDisabled(byte index) {
		if ((index < 0) || (index >= NB_NODES)) {
			return false;
		}

		return nodes[index].disabled;
	}

	/**
	 * Shows if the game is over.
	 * 
	 * @return <code>true</code> if this game is over, <code>false</code>
	 *         otherwise.
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * Returns the winner of this game.
	 * 
	 * @return {@link #PLAYER1}, if the first player has won this game,
	 *         {@link #PLAYER2}, if the second player has won this game, and
	 *         {@link #NEUTRAL}, if the game is not over yet.
	 */
	public byte getWinner() {
		if (!gameOver) {
			return NEUTRAL;
		}

		return currentPlayer ? PLAYER2 : PLAYER1;
	}

	/**
	 * Returns the game's meta information.
	 * 
	 * @return the game's meta information.
	 */
	public GameInfo getGameInfo() {
		return gameInfo;
	}

	/**
	 * Sets the edge owner for the edge index <code>index</code> to the
	 * current player. Undo information is added at this point.
	 * 
	 * @param index
	 *            the edge index.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the index is out of range (0-14).
	 */
	protected void setEdgeOwner(byte index) throws ArrayIndexOutOfBoundsException {
		setEdgeOwner(index, currentPlayer ? PLAYER1 : PLAYER2);
	}

	/**
	 * Sets the owner for the edge at the specified index to the byte value.
	 * 
	 * @param index
	 *            the edge index.
	 * @param color
	 *            one of {@linkplain #NEUTRAL},{@linkplain #PLAYER1},
	 *            {@linkplain #PLAYER2}.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             if the index is out of range (0-14).
	 */
	protected void setEdgeOwner(byte index, byte color) throws ArrayIndexOutOfBoundsException {
		edges[index] = color;
	}

	/**
	 * Returns the owner of the edge between <code>nodeA</code> and
	 * <code>nodeB</code>.
	 * 
	 * @param nodeA
	 *            The first node.
	 * @param nodeB
	 *            The second node.
	 * @return The owner of the edge between <code>nodeA</code> and
	 *         <code>nodeB</code> indicated by {@link #NEUTRAL},{@link
	 *         #PLAYER1}, or {@link #PLAYER2}.
	 * 
	 * @see #getEdgeOwner(byte)
	 */
	public byte getEdgeOwner(byte nodeA, byte nodeB) {
		if (nodeA == nodeB) {
			return NEUTRAL;
		}

		return getEdgeOwner(Move.getEdgeIndex(nodeA, nodeB));
	}

	/**
	 * Returns the owner of the edge identified by <code>edgeIndex</code>.
	 * 
	 * @param edgeIndex
	 *            the index.
	 * @return the owner of the edge indicated by {@link #NEUTRAL},{@link
	 *         #PLAYER1}, or {@link #PLAYER2}.
	 * 
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>edgeIndex</code> is not between 0 and 14
	 *             (including).
	 * @see #getEdgeOwner(byte, byte)
	 */
	public byte getEdgeOwner(byte edgeIndex) throws ArrayIndexOutOfBoundsException {
		return edges[edgeIndex];
	}

	/**
	 * Enables the nodes at the given edge index.
	 * 
	 * @param edgeIdx
	 *            the edge index.
	 * @param enable
	 *            whether to enable the nodes
	 */
	protected void enableEdge(byte edgeIdx, boolean enable) {
		byte[] edgeNodes = Move.getNodeIndices(edgeIdx);
		nodes[edgeNodes[0]].disabled = !enable;
		nodes[edgeNodes[1]].disabled = !enable;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return gameInfo.getId();
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.gameInfo.setId(id);
	}

	/**
	 * Sets the dynamic UI.
	 * @param ui
	 *            the dynamic user interface to set.
	 */
	public void setDynamicUI(IDynamicUI ui) {
		this.ui = ui;
	}

	/** Updates the user interface, if it is available. */
	private void updateUI() {
		if (ui != null) {
			ui.updateDisplay();
		}
	}

	/**
	 * Returns the number of moves that were executed since the start of the
	 * game.
	 * 
	 * @return The number of moves.
	 */
	public byte getMoveNr() {
		return moveNr;
	}

	/**
	 * Returns the move message
	 * 
	 * @return the move message.
	 */
	public String getMoveMessage() {
		return moveMessage;
	}

	/**
	 * Sets the move information.
	 * 
	 * @param msg
	 *            the new information.
	 */
	protected void setMoveMessage(String msg) {
		this.moveMessage = msg;
	}

	//
	// overridden java.lang.Object methods
	//

	/** @see Object#toString() */
	public String toString() {
		StringBuffer buf = new StringBuffer("Game:\n");
		for (int i = 0; i < nodes.length; i++) {
			buf.append("\t[" + i + "]:");
			buf.append(nodes[i]);
			buf.append("\n");

		}
		return buf.toString();
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	public boolean equals(Object obj) {
		if (obj instanceof Game) {
			return equals((Game) obj);
		}
		return false;
	}

	/**
	 * Shows whether this game is equal to another game.
	 * @param g
	 *            the game, this game is compared to.
	 * @return <code>true</code>, if both games have the same number of
	 *         moves, and the game informations are equal.
	 */
	public boolean equals(Game g) {
		return g.moveNr == this.moveNr && selectionIsEqual(g)
				&& g.gameInfo.equals(this.gameInfo);
	}

	/**
	 * Indicates whether the selection is equal to the selection on some other
	 * game.
	 * 
	 * @param g
	 *            the other game.
	 * @return if all nodes are in the same state of selection, and all edges
	 *         show the same selection.
	 */
	private boolean selectionIsEqual(Game g) {
		// compare nodes
		Node[] otherNodes = g.nodes;
		if (otherNodes != nodes) {
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i] == null && otherNodes[i] == null) {
					continue;
				}
				if (!nodes[i].equals(otherNodes[i])) {
					return false;
				}
			}
		}

		// compare edges
		byte[] otherEdges = g.edges;
		if (otherEdges != edges) {
			for (int i = 0; i < edges.length; i++) {
				if (edges[i] != otherEdges[i]) {
					return false;
				}
			}
		}
		return true;
	}

	//
	// internal classes
	//

	class Node {
		boolean activated = false;
		boolean disabled = false;

		/** @see Object#toString() */
		public String toString() {
			return "Node[act=" + activated + ", dis=" + disabled + "]";
		}

		/** @see java.lang.Object#equals(java.lang.Object) */
		public boolean equals(Object obj) {
			if (obj instanceof Node) {
				Node other = (Node) obj;
				return other.activated == activated && other.disabled == disabled;
			}
			return false;
		}
	}

}