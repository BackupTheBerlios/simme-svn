package test.sim;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;

/**
 * Splashscreen that runs before midlet.
 *
 * @author jorge
 */
public class SplashScreen extends Form {

   private Sim sim;

   /**
    * Creates a new SplashScreen object.
    *
    * @param sim Midlet which the splash screen is intended for.
    */
   public SplashScreen(Sim sim) {
      super("");
      this.sim = sim;

      Image image = null;

      try {
         image = Image.createImage("/icons/Splash_k.png");
      } catch (Exception ex) {
      }

      ImageItem item = new ImageItem(null, image, ImageItem.LAYOUT_CENTER, null);
      append(item);

      Task task = new Task();
      Thread thread = new Thread(task);
      thread.start();
   }

   public class Task implements Runnable {
      private static final int DELAY = 1000;

      public void run() {
         try {
            Thread.sleep(DELAY);
         } catch (Exception ex) {
         }

         Display d = Display.getDisplay(sim);
         d.setCurrent(sim.getMainScreen());
      }
   }
}
