package at.einspiel.simme.client;

import java.util.Stack;

import at.einspiel.simme.nanoxml.XMLElement;

/**
 * Represents a single game with all its states. Provides methods to start,
 * end, pause a game and uses a <code>Canvas</code> to draw itself to the
 * display
 *
 * @author kariem
 */
public class Game {

   /** Number of Edges */
   static final byte NB_EDGES = 15;
   /** Number of Nodes */
   public static final byte NB_NODES = 6;

   /** No owner */
   public static final byte NEUTRAL = 0;
   /** Player 1 is owner */
   public static final byte PLAYER1 = 1;
   /** Player 2 is owner */
   public static final byte PLAYER2 = 2;

   /** nodes */
   private Node[] nodes;
   /** edges between nodes */
   private byte[] edges;
   /** for undoing edge operations*/
   private Stack undoStack;

   /** Indicates the currently active node */
   byte activeNode;
   /** Indicates the currently active player p1=true, p2=false */
   private boolean currentPlayer;
   /** the move number */
   private byte moveNr;

   /** Indicates if the game is over */
   protected boolean gameOver;

   // meta information
   private String p1Name;
   private String p2Name;
   private String p1Info;
   private String p2Info;
   /** the move message */
   private String moveMessage = "";

   private Move tmpMove;

   /**
    * Initializes a new game and starts it.
    *
    * @see #start()
    */
   public Game() {
      this(null);
   }

   /**
    * Initializes a new game with the values found in the given xml element.
    * 
    * @param gameInfo xml element that holds information about the game.
    */
   public Game(XMLElement gameInfo) {
      if (gameInfo != null) {
         String p1 = gameInfo.getAttribute("p1", "Player 1");
         String p2 = gameInfo.getAttribute("p2", "Player 2");
         String info1 = gameInfo.getAttribute("info1", "AT");
         String info2 = gameInfo.getAttribute("info2", "AT");

         setGameInfo(p1, p2, info1, info2);
      } else {
         // set to some default values
         setGameInfo("Player 1", "Player 2", "AT", "AT");
      }
      tmpMove = new Move((byte) 0);
   }

   /** May be used to start/restart the game. */
   public void start() {
      nodes = new Node[NB_NODES];

      for (byte i = 0; i < NB_NODES; i++) {
         nodes[i] = new Node();
      }

      edges = new byte[NB_EDGES];

      activeNode = -1; // value, if there is no active node
      currentPlayer = true; // p1 is currentPlayer
      moveNr = 0; // first move
      gameOver = false;

      undoStack = new Stack();
   }

   /** Sets the player information */
   void setGameInfo(String p1, String p2, String info1, String info2) {
      p1Name = p1;
      p1Info = info1;
      p2Name = p2;
      p2Info = info2;
   }

   /**
    * Selects a single node within the game. A node which is deactivated will
    * be activated. If the node is already activated, it will be deactivated.
    *
    * @param index Should be in range of 0-5 (including), or this method will
    *         throw an <code>ArrayIndexOutOfBoundsException</code>. It
    *         indicates the node to be selected. If <code>index</code> is 
    *         <code>-1</code>, the last move will be undone.
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

      if (index == -1) {
         return undo();
      } else if ((index < 0) || (index >= NB_NODES)) {
         moveMessage = "Enter 0-5!";
         return false;
      }

      Node nodeAtIndex = nodes[index];

      if (nodeAtIndex.disabled) {
         // not possible to select a disabled node
         moveMessage = "Node closed!";
         return false;
      }

      if (activeNode == -1) {
         // no node is currently activated
         activeNode = index;
         nodeAtIndex.activated = true;

         moveMessage = null;

         return true;
      } else if (activeNode == index) {
         // e.g. edge from Node 1 to Node 1
         // set node to inactive
         nodeAtIndex.activated = false;

         // set active node to nothing, and exit
         activeNode = -1;

         moveMessage = (index + 1) + " already chosen!";

         return false;
      } else {
         // select the second node
         return edgeTo(index);
      }
   }

   /**
    * Selects the edge.
    * @param edgeIndex the index of the edge to be selected.
    * @return whether the edge was successfully selected.
    */
   public boolean selectEdge(byte edgeIndex) {
      byte owner = getEdgeOwner(edgeIndex);
      if (owner != NEUTRAL) {
         moveMessage = "Edge selected!";
         return false;
      }

      // deselect nodes, if contained in edge
      byte[] edgeNodes = Move.getNodeIndices(edgeIndex);
      nodes[edgeNodes[0]].activated = false;
      nodes[edgeNodes[1]].activated = false;
      activeNode = -1;

      setEdgeOwner(edgeIndex);

      // switch players and see if someone has won
      tmpMove.setEdge(edgeIndex);
      endTurn(tmpMove);

      performComputerMove();

      // disable node to be disabled
      disableNodes();

      if (gameOver) {
         if (currentPlayer) {
            moveMessage = "I am sorry, you lost.";
         } else {
            moveMessage = "You win!";
         }
      } else { // reset move message
         moveMessage = null;
      }

      return true;

   }

   private boolean edgeTo(byte secondNode) {
      return selectEdge(Move.getEdgeIndex(activeNode, secondNode));
   }

   /** 
    * Performs a computer move. This method has to be implemented in subclasses
    * in order to perform any computer move.
    */
   protected void performComputerMove() {
      // implement this in subclasses
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
    * condition. The other player may make his move.
    *
    * @param m the move.
    */
   void endTurn(Move m) {
      // only the player who just did his move may lose.
      byte player = currentPlayer ? PLAYER1 : PLAYER2;

      moveNr++;

      // if someone has lost, last edge drawn was deciding
      for (byte c = 0; c < NB_NODES; c++) {
         if ((c != m.n1) && (c != m.n2)) {
            // find triangle
            if ((getEdgeOwner(m.n1, c) == player)
                  && (getEdgeOwner(m.n2, c) == player)) {
               gameOver = true;

               return;
            }
         }
      }

      currentPlayer = !currentPlayer;
   }

   private boolean undo() {
      // undo
      if (gameOver) {
         currentPlayer = !currentPlayer;
         gameOver = false;
      }

      if (undoTurn()) {
         moveMessage = "Undo successful!";

         return true;
      } else {
         moveMessage = "Undo not possible!";

         return false;
      }
   }

   /**
    * Undos last edge operation.
    * @return whether undo has been performed.
    */
   private boolean undoTurn() {
      byte edgeIdx = -1;

      if (!undoStack.empty()) {
         moveNr--;

         edgeIdx = ((Byte) undoStack.pop()).byteValue();
         edges[edgeIdx] = NEUTRAL;

         currentPlayer = !currentPlayer;

         return true;
      } else {
         return false;
      }
   }

   /**
    * Returns the current player.
    *
    * @return the current player.
    */
   public byte getPlayersTurn() {
      if (!gameOver) {
         return currentPlayer ? PLAYER1 : PLAYER2;
      } else {
         return NEUTRAL;
      }
   }

   /**
    * Shows if a single node is activated.
    *
    * @param index The index of the node.
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
    * @param index The index of the node.
    * currentPlayer = !currentPlayer;
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

   void setEdgeOwner(byte index) {
      edges[index] = currentPlayer ? PLAYER1 : PLAYER2;
      undoStack.push(new Byte(index));
   }

   /**
    * Returns the owner of the edge between <code>nodeA</code> and
    * <code>nodeB</code>.
    *
    * @param nodeA The first node.
    * @param nodeB The second node.
    * @return The owner of the edge between <code>nodeA</code> and
    *          <code>nodeB</code> indicated by {@link #NEUTRAL}, {@link
    *          #PLAYER1}, or {@link #PLAYER2}.
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
    * @param edgeIndex the index.
    * @return the owner of the edge indicated by {@link #NEUTRAL}, {@link
    *          #PLAYER1}, or {@link #PLAYER2}. 
    * 
    * @throws ArrayIndexOutOfBoundsException if <code>edgeIndex</code> is not
    *          between 0 and 14 (including).
    * @see #getEdgeOwner(byte, byte)
    */
   public byte getEdgeOwner(byte edgeIndex)
         throws ArrayIndexOutOfBoundsException {
      return edges[edgeIndex];
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
    * Shows information on player1.
    *
    * @return a String showing information on player1.
    */
   public String getP1Info() {
      return p1Info;
   }

   /**
    * Shows the name of player1.
    *
    * @return a String showing the name of player1.
    */
   public String getP1Name() {
      return p1Name;
   }

   /**
    * Shows information on player2.
    *
    * @return a String showing information on player2.
    */
   public String getP2Info() {
      return p2Info;
   }

   /**
    * Shows the name of player2.
    *
    * @return a String showing the name of player2.
    */
   public String getP2Name() {
      return p2Name;
   }

   /**
    * Returns the move message
    * @return the move message.
    */
   public String getMoveMessage() {
      return moveMessage;
   }

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

   class Node {
      boolean activated = false;
      boolean disabled = false;

      /** @see Object#toString() */
      public String toString() {
         return "Node[act=" + activated + ", dis=" + disabled + "]";
      }
   }
}