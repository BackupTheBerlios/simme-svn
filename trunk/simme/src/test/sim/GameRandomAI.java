package test.sim;

import java.util.Random;

/**
 * Game class with random AI.
 * 
 * @author kariem
 */
public class GameRandomAI extends Game {

  /** @see test.sim.Game#performComputerMove() */
  protected void performComputerMove() {
    Random random = new Random();
    byte firstNode = (byte) java.lang.Math.abs((random.nextInt() % NB_NODES));
    byte secondNode = (byte) java.lang.Math.abs((random.nextInt() % NB_NODES));
    if (!gameOver) {
      while ((getEdgeOwner(firstNode, secondNode) != NEUTRAL) || (firstNode == secondNode)) {
        firstNode = (byte) java.lang.Math.abs((random.nextInt() % NB_NODES));
        secondNode = (byte) java.lang.Math.abs((random.nextInt() % NB_NODES));
      }

      setEdgeOwner(firstNode, secondNode);

      // switch players and see if someone has won
      endTurn(firstNode, secondNode);
    }
  }

}
