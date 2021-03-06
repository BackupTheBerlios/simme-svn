package test.sim;

/**
 * @author Jorge De Mar
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GameCanvas {

   private final int DELAY = 100;

   private GameThread thread;
   private boolean running;

   public GameCanvas() {
   }

   public void start() {
      running = true;
      thread = new GameThread();
      thread.start();
   }

   public void stop() {
      running = false;
   }

   public class GameThread extends Thread {

      public void run() {
         while (running) {

            long time = System.currentTimeMillis();
            //getSelectedNode();
            //repaint();
            time = System.currentTimeMillis() - time;
            try {
               if (time < DELAY)
                  Thread.sleep(DELAY - (int) time);
            } catch (Exception ex) {
            }

         }
      }
   }
}
