package test.sim;

/**
 * DOCUMENT ME!
 *
 * @author Jorge De Mar To change this generated comment edit the template
 *         variable "typecomment": Window>Preferences>Java>Templates. To
 *         enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
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
    * DOCUMENT ME!
    */
   public void start() {
      running = true;
      thread = new GameThread();
      thread.start();
   }

   /**
    * DOCUMENT ME!
    */
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
               if (time < DELAY) {
                  Thread.sleep(DELAY - (int) time);
               }
            } catch (Exception ex) {
            }
         }
      }
   }
}
