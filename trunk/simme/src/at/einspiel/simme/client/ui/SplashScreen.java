package at.einspiel.simme.client.ui;

import at.einspiel.simme.client.Sim;

import java.io.IOException;

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

            Display d = Sim.getDisplay();
            d.setCurrent(Sim.getMainScreen());
        }
    }
}
