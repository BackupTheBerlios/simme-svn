//----------------------------------------------------------------------------
//[Simme]
//Java Source File: SplashScreen.java
//           $Date: 2004/09/22 18:23:58 $
//       $Revision: 1.3 $
//----------------------------------------------------------------------------
package at.einspiel.simme.client.ui;

import java.io.IOException;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;

import at.einspiel.simme.client.Sim;

/**
 * Splashscreen that runs before midlet.
 *
 * @author jorge
 */
public class SplashScreen extends Form {

    /** Creates a new SplashScreen object. */
    public SplashScreen() {
        super("");

        Image image = null;

        try {
            image = Image.createImage("/icons/Splash_k.png");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        ImageItem item = new ImageItem(null, image, ImageItem.LAYOUT_CENTER, null);
        append(item);

        Task task = new Task();
        Thread thread = new Thread(task);
        thread.start();
    }

    private class Task implements Runnable {
        private static final int DELAY = 1000;

        /** @see java.lang.Runnable#run() */
        public void run() {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }

            Sim.setDisplay(Sim.getMainScreen());
        }
    }
}
