package test.sim;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.List;

/**
 * @author jorge
 *
 */
public class SplashScreen extends Form {

   private Sim sim;
   private List list;

   public SplashScreen(Sim sim, List list) {
      super("");
      this.sim = sim;
      this.list = list;
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

      private final int DELAY = 1000;

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
