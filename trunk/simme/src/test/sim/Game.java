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
   private byte[][] edges;

   /** Indicates the currently active node */
   byte activeNode;

   /** Indicates the currently active player */
   private byte currentPlayer;

   public Game() {
      nodes = new Node[6];
      for (byte i = 0; i < 6; i++) {
         nodes[i] = new Node();
      }

      edges = new byte[6][6];
      // initializing loop not necessary - values
      /*
      for (int i = 0; i < 6; i++) {
         for (int j = 0; j < 6; j++) {
            edges[i][j] = NEUTRAL; 
         }
      }
      */

      activeNode = -1; // value, if there is no active node
      currentPlayer = PLAYER1;
   }

   /**
    * Selects a single node within the game. A node which is deactivated will
    * be activated. If the node is already activated, it will be deactivated.
    * @param index Should be in range of 0-5 (including). Indicates the node
    * to be selected.
    * @return <code>true</code>, if the operation suceeded (e.g. node was
    * activated/deactivated, edge was coloured). <code>false</code>, if no
    * changes have been made to the current game state.
    */
   public boolean selectNode(byte index) {
      if ((index < 0) || index > 5)
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
            edges[activeNode][index] = currentPlayer;
            edges[index][activeNode] = currentPlayer;

            // deselect active node
				nodes[activeNode].activated = false;
            activeNode = -1;

				// disable node to be disabled
            disableNodes();
            
            // switch players
            endTurn();
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
    * Ends a turn for this game. The other player may make his move.
    */
   private void endTurn() {
      currentPlayer = currentPlayer == PLAYER1 ? PLAYER2 : PLAYER1;
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
      return edges[nodeA][nodeB];
   }

	class Node {
		boolean activated;
		boolean disabled;
	}
}
