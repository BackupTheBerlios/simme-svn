package at.einspiel.simme.client.tests;

import at.einspiel.simme.client.Move;
import junit.framework.TestCase;

/**
 * Tests the move class.
 * @author kariem
 */
public class MoveTest extends TestCase {

    private static final byte MAX_NODE_INDEX = 5;
    private static final byte MAX_EDGE_INDEX = 14;

    /** Constructor */
    public MoveTest() {
        super("MoveTest");
    }

    /** Test for getEdgeIndex(int, int) */
    public final void testGetEdgeIndex() {
        byte edge;
        for (byte i = 0; i <= MAX_NODE_INDEX; i++) {
            for (byte j = 0; j <= MAX_NODE_INDEX; j++) {
                if (i != j) {
                    edge = Move.getEdgeIndex(i, j);
                    assertFalse(edge == -1);
                }
            }
        }
    }

    /** Test for getNodes() */
    public final void testGetNodes() {
        byte[] nodes;
        byte node1, node2;
        for (byte i = 0; i < MAX_EDGE_INDEX; i++) {
            nodes = Move.getNodeIndices(i);
            node1 = nodes[0];
            node2 = nodes[1];
            assertFalse(node1 == node2);
            assertTrue(node1 < node2);
        }
    }

    /** Test for move(byte, byte) */
    public final void testMove1() {
        Move testMove, testMove2;
        byte n1, n2, edge;
        for (byte i = 0; i <= MAX_NODE_INDEX; i++) {
            for (byte j = 0; j <= MAX_NODE_INDEX; j++) {
                try {
                    testMove = new Move(i, j);

                    // test edge
                    edge = testMove.getEdge();
                    assertEquals(Move.getEdgeIndex(i, j), edge);

                    // test nodes
                    n1 = testMove.getNode1();
                    n2 = testMove.getNode2();

                    if (i < j) {
                        assertEquals(n1, i);
                        assertEquals(n2, j);
                    } else {
                        assertEquals(n1, j);
                        assertEquals(n2, i);
                    }

                    // test for equality with move created via edge
                    testMove2 = new Move(edge);
                    assertEquals(testMove, testMove2);

                } catch (IllegalArgumentException e) {
                    assertNotNull(e);
                }
            }
        }
    }

    /** Test for move(byte) */
    public final void testMove2() {
        Move testMove, testMove2;
        byte n1, n2;
        byte[] nodes, nodes2;
        for (byte i = 0; i < MAX_EDGE_INDEX; i++) {
            testMove = new Move(i);

            // test nodes
            nodes = testMove.getNodes();
            nodes2 = Move.getNodeIndices(i);
            assertEquals(nodes[0], nodes2[0]);
            assertEquals(nodes[1], nodes2[1]);

            n1 = testMove.getNode1();
            n2 = testMove.getNode2();

            assertTrue(n1 < n2);

            // test for equality with move created via nodes
            testMove2 = new Move(n1, n2);
            assertEquals(testMove, testMove2);
        }
    }
}
