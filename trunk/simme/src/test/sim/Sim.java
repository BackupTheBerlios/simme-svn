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

   static List main;
   static Sim instance;

   /**
    * Creates a new Sim object.
    */
   public Sim() {
      instance = this;
   }

   /** @see MIDlet#destroyApp(boolean) */
   protected void destroyApp(boolean arg0) {
   }

   /** @see MIDlet#pauseApp() */
   protected void pauseApp() {
   }

   /**
    * Starts this application
    * @throws MIDletStateChangeException is thrown if the MIDlet cannot start 
    *         now but might be able to start at a later time.
   
    */
   protected void startApp() throws MIDletStateChangeException {
      Display d = Display.getDisplay(this);
      main = new StartForm(this);

      //SplashScreen splashScreen = new SplashScreen(this);
      d.setCurrent(main);
   }

   /**
    * Returns the main screen of this application.
    *
    * @return the main screen.
    */
   public static Screen getMainScreen() {
      return main;
   }

   /**
    * @return the single instance of this midlet
    */
   public static Sim getInstance() {
      return instance;
   }
   
   /**
    * Returns the current <code>Display</code> of this MIDlet.
    *  
    * @return the <code>Display</code> of this MIDlet.
    */
   public static Display getDisplay() {
      return Display.getDisplay(instance);
   }

}
