package test.sim;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


/**
 * Class
 *
 * @author georg
 */
public class Sim extends MIDlet {

   List main;


   /**
    * Creates a new Sim object.
    */
   public Sim() {
   }


   protected void destroyApp(boolean arg0) {
   }

   protected void pauseApp() {
   }

   /* (non-Javadoc)
    * @see javax.microedition.midlet.MIDlet#startApp()
    */
   protected void startApp() throws MIDletStateChangeException {
      /*
      Display d = Display.getDisplay(this);
      d.setCurrent(main);
      */
      Display d = Display.getDisplay(this);
      main = new StartForm(this);

      SplashScreen splashScreen = new SplashScreen(this, main);
      d.setCurrent(splashScreen);
   }

   /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
   public Screen getMainScreen() {
      return main;
   }
}
