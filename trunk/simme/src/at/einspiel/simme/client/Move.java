package at.einspiel.simme.client;

/**
 * Represents a single move as part of the game.
 * @author kariem
 */
public class Move {

    /** maximum index for nodes */
    public static final byte MAX_NODE_INDEX = 5;
    /** maximum index for edges */
    public static final byte MAX_EDGE_INDEX = 14;

    // used to calculate from edges to nodes and vice versa
    private static final int PART_1 = 5;
    private static final int PART_2 = 9;
    private static final int PART_3 = 12;


    byte n1;
    byte n2;
    byte edge;

    /**
     * Creates a new move.
     * @param node1 the first node.
     * @param node2 the second node.
     */
    public Move(byte node1, byte node2) {
        if (node1 == node2) {
            throw new IllegalArgumentException("Node1 and node2 must not be equal");
        }
        if (node1 > MAX_NODE_INDEX || node2 > MAX_NODE_INDEX) {
            throw new IllegalArgumentException(
                "Node1 and node2 must not be greater than " + MAX_NODE_INDEX);
        }
        if (node1 < 0 || node2 < 0) {
            throw new IllegalArgumentException("Node1 and node2 must not be less than 0");
        }

        // set n1 to the value of the smaller node
        if (node1 < node2) {
            n1 = node1;
            n2 = node2;
        } else {
            n1 = node2;
            n2 = node1;
        }

        edge = calcEdge(n1, n2);
    }

    /**
     * Creates a new move.
     * @param edge the edge index.
     */
    public Move(byte edge) {
        setEdge(edge);
    }

    /**
     * Sets the edge
     * @param edge the edge
     */
    public void setEdge(byte edge) {
        if (edge < 0 || edge > MAX_EDGE_INDEX) {
            throw new IllegalArgumentException(
                "Edge must be greater than 0 and less than " + MAX_EDGE_INDEX);
        }
        this.edge = edge;
        byte[] b = getNodeIndices(edge);
        n1 = b[0];
        n2 = b[1];
    }

    /**
     * Returns this move's edge.
     * @return the edge.
     */
    public byte getEdge() {
        return edge;
    }

    /**
     * Returns this move's smaller node.
     * @return this move's smaller node.
     */
    public byte getNode1() {
        return n1;
    }

    /**
     * Returns this move's bigger node.
     * @return this move's bigger node.
     */
    public byte getNode2() {
        return n2;
    }

    /**
     * Returns this move's nodes.
     * @return the nodes as an array. The first node will be the smaller node.
     */
    public byte[] getNodes() {
        return new byte[] { n1, n2 };
    }

    /**
     * Returns the index within the edgelist containing the owner of the edge
     * between two node indices.
     *
     * @param nodeA The first node's index.
     * @param nodeB The second node's index.
     *
     * @return The index of the edge within the edge list containing the edge
     *          between <code>nodeA</code> and <code>nodeB</code>.
     */
    public static byte getEdgeIndex(byte nodeA, byte nodeB) {
        if (nodeA == nodeB) {
            return -1;
        }
        return (nodeA < nodeB) ? calcEdge(nodeA, nodeB) : calcEdge(nodeB, nodeA);
    }

    /**
     * Returns the index within the edgelist containing the owner of the edge
     * between <code>smaller</code> and <code>bigger</code>.
     *
     * @param smaller The smaller node index.
     * @param bigger The bigger node index.
     *
     * @return The index of the edge within the edge list containing the edge
     *          between <code>smaller</code> and <code>bigger</code>.
     */
    static byte calcEdge(byte smaller, byte bigger) {
        byte add = 0;

        switch (smaller) {
            case 1 :
                add = PART_1;
                break;

            case 2 :
                add = PART_2;
                break;

            case 3 :
                add = PART_3;
                break;

            case 4 :
                return MAX_EDGE_INDEX;
        }

        return (byte) (add + bigger - smaller - 1);
    }

    /**
     * Returns the node indices for a given edge.
     * 
     * @param edge the edge.
     * @return the nodes that hold the edge with the given index.
     */
    public static byte[] getNodeIndices(byte edge) {
        if (edge < 0 || edge > MAX_EDGE_INDEX) {
            throw new IllegalArgumentException(
                "Edge must be greater than 0 and less than " + MAX_EDGE_INDEX);
        }
        if (edge < PART_1) {
            return new byte[] { 0, (byte) (edge + 1)};
        } else if (edge < PART_2) {
            return new byte[] { 1, (byte) (edge - 3)};
        } else if (edge < PART_3) {
            return new byte[] { 2, (byte) (edge - 6)};
        } else if (edge < MAX_EDGE_INDEX) {
            return new byte[] { 3, (byte) (edge - 8)};
        } else {
            return new byte[] { 4, PART_1 };
        }
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    public boolean equals(Object obj) {

        return equals((Move) obj);
    }

    /**
     * Returns whether this object is equal to another <code>Move</code>.
     * @param m the other move object.
     * @return <code>true</code> if both objects are equal, <code>false</code>
     *          otherwise.
     * 
     * @see #equals(Object)
     */
    public boolean equals(Move m) {
        return n1 == m.n1 && n2 == m.n2 && edge == m.edge;
    }
}