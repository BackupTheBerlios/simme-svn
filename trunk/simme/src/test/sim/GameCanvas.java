package test.sim;

/**
 * Intended to use for threading.
 *
 * @author jorge
 */
public class GameCanvas {

   private static final int DELAY = 100;
   private GameThread thread;
   private boolean running;

   /**
    * Creates a new GameCanvas object.
    */
   public GameCanvas() {
   }

   /**
    * Starts this thread.
    */
   public void start() {
      running = true;
      thread = new GameThread();
      thread.start();
   }

   /**
    * Stops the running thread.
    */
   public void stop() {
      running = false;
   }

   private class GameThread extends Thread {
      /** @see Thread#run() */
      public void run() {
         while (running) {
            long time = System.currentTimeMillis();

            //getSelectedNode();
            //repaint();
            time = System.currentTimeMillis() - time;

            try {
               if (time < DELAY) {
                  Thread.sleep(DELAY - (int) time);
               }
            } catch (Exception ex) {
               ; // ??
            }
         }
      }
   }
}
