package test.sim;

/**
 * Represents a single game with all its states. Provides methods to start, end,
 * pause a game and uses a <code>Canvas</code> to draw itself to the display
 *
 * @author kariem
 */
public class Game {

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

   /** Indicates the currently active node */
   byte activeNode;

   /** Indicates the currently active player p1=true, p2=false */
   private boolean currentPlayer;

   /** Indicates if the game is over */
   private boolean gameOver;

	private byte moveNr;

	private String p1Name, p2Name;
	private String p1Info, p2Info;

   /**
    * Initializes a new game and starts it.
    * @see #start()
    */
   public Game() {
      start();
   }

   /**
    * May be used to start/restart the game.
    */
   public void start() {
      nodes = new Node[6];
      for (byte i = 0; i < 6; i++) {
         nodes[i] = new Node();
      }

      edges = new byte[15];

      activeNode = -1; // value, if there is no active node
      currentPlayer = true; // p1 is currentPlayer
      moveNr = 1; // first move
      gameOver = false;
      
      
      // set to some default values - has to be changed.
      p1Name = "First Player";
		p1Info = "AT";
      p2Name = "Second One";
		p2Info = "DE";      
   }

   /**
    * Selects a single node within the game. A node which is deactivated will
    * be activated. If the node is already activated, it will be deactivated.
    * @param index Should be in range of 0-5 (including), or this method will
    * throw an <code>ArrayIndexOutOfBoundsException</code>. It indicates the
    * node to be selected.
    * @return <code>true</code>, if the operation suceeded (e.g. node was
    * activated/deactivated, edge was coloured). <code>false</code>, if no
    * changes have been made to the current game state.
    */
   public boolean selectNode(byte index) {
      if ((index < 0) || index > 5)
         return false;
      if (gameOver)
         return false;

      Node nodeAtIndex = nodes[index];

      if (activeNode == -1) { // activeNode is not set
         if (nodeAtIndex.disabled)
            return false;
         activeNode = index;
         nodeAtIndex.activated = true;
         return true;
      } else if (activeNode == index) { // activeNode is set, and selected
         // set node to inactive
         nodeAtIndex.activated = false;
         // set active node to nothing, and exit
         activeNode = -1;
         return true;
      } else { // a second node is activated
         // test if edge still not owner by P1 nor P2
         if (getEdgeOwner(activeNode, index) == NEUTRAL) {
            setEdgeOwner(activeNode, index, currentPlayer);

            // switch players and see if someone has won
            endTurn(activeNode, index);

            // deselect active node
            nodes[activeNode].activated = false;
            activeNode = -1;

            // disable node to be disabled
            disableNodes();

            return true;
         }
      }

      return false;
   }

   /**
    * Searches for nodes to be disabled and sets them accordingly.
    */
   private void disableNodes() {
      Node n;
      for (byte i = 0; i < 6; i++) {
         n = nodes[i];
         // look for not yet disabled nodes
         if (!n.disabled) {
            boolean possibleConnection = false;
            for (byte j = 0; j < 6; j++) {
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
    * @param a First node of last edge drawn.
    * @param b Second node of last edge drawn.
    */
   private void endTurn(byte a, byte b) {
      // only the player who just did his move may lose.
      byte player = currentPlayer ? PLAYER1 : PLAYER2;

      // if someone has lost, last edge drawn was deciding
      for (byte c = 0; c < 6; c++) {
         if ((c != a) && (c != b)) {
            // find triangle
            if ((getEdgeOwner(a, c) == player) && (getEdgeOwner(b, c) == player)) {
               gameOver = true;
               return;
            }
         }
      }
      
      currentPlayer = !currentPlayer;
   }
   
   public byte getPlayersTurn() {
   	return currentPlayer ? PLAYER1 : PLAYER2; 
   }

   public boolean isActivated(byte index) {
      if ((index < 0) || index > 5)
         return false;
      return nodes[index].activated;
   }

   public boolean isDisabled(byte index) {
      if ((index < 0) || index > 5)
         return false;
      return nodes[index].disabled;
   }

	public boolean isGameOver() {
		return gameOver;
	}

	public byte getWinner() {
		if (!gameOver) {
			return NEUTRAL;
		}
		return currentPlayer ? PLAYER2 : PLAYER1;
	}

   private void setEdgeOwner(byte nodeA, byte nodeB, boolean player) {
      byte index = nodeA < nodeB ? getIndex(nodeA, nodeB) : getIndex(nodeB, nodeA);
      edges[index] = player ? PLAYER1 : PLAYER2;
   }

   /**
    * Returns the owner of the edge between <code>nodeA</code> and
    * <code>nodeB</code>. 
    * @param nodeA The first node.
    * @param nodeB The second node.
    * @return The owner of the edge between <code>nodeA</code> and 
    * <code>nodeB</code> indicated by {@link #NEUTRAL}, {@link #PLAYER1}, or
    * {@link #PLAYER2}. 
    */
   public byte getEdgeOwner(byte nodeA, byte nodeB) {
      if (nodeA == nodeB)
         return NEUTRAL;
      byte owner = nodeA < nodeB ? edges[getIndex(nodeA, nodeB)] : edges[getIndex(nodeB, nodeA)];
      return owner;
   }

   /**
    * Returns the index within the edgelist containing the owner of the edge
    * between <code>smaller</code> and <code>bigger</code>.
    * @param smaller The smaller node index.
    * @param bigger The bigger node index.
    * @return The index of the edge within the edge list containing the edge
    * between <code>smaller</code> and <code>bigger</code>.
    */
   private byte getIndex(byte smaller, byte bigger) {
      byte add = 0;
      switch (smaller) {
         case 1 :
            add = 5;
            break;
         case 2 :
            add = 9;
            break;
         case 3 :
            add = 12;
            break;
         case 4 :
            add = 14;
            break;
      }
      return new Integer(add + bigger - smaller - 1).byteValue();
   }

   class Node {
      boolean activated;
      boolean disabled;
   }
   /**
    * @return byte
    */
   public byte getMoveNr() {
      return moveNr;
   }

   /**
    * @return String
    */
   public String getP1Info() {
      return p1Info;
   }

   /**
    * @return String
    */
   public String getP1Name() {
      return p1Name;
   }

   /**
    * @return String
    */
   public String getP2Info() {
      return p2Info;
   }

   /**
    * @return String
    */
   public String getP2Name() {
      return p2Name;
   }

}
