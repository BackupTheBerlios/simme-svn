package test.sim;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.ImageItem;

/**
 * @author Jorge De Mar
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SplashScreen extends Form {

	private Sim sim;
	private Form form;

	public SplashScreen(Sim sim, Form form) {
		super("");
		this.sim = sim;
		this.form = form;
		Image image = null;
		try {
			image = Image.createImage("/icons/Splash_k.png");
		} catch (Exception ex) {
		}

		ImageItem item =
			new ImageItem(null, image, ImageItem.LAYOUT_CENTER, null);
		append(item);
		Task task = new Task();
		Thread thread = new Thread(task);
		thread.start();
	}

	public class Task implements Runnable {

		private final int DELAY = 3000;

		public void run() {

			try {
				Thread.sleep(DELAY);
			} catch (Exception ex) {
			}
			
			sim.start();
		}
	}
}
