package at.einspiel.simme.client;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
import javax.microedition.midlet.MIDlet;

import at.einspiel.simme.client.ui.StartForm;

/**
 * Main class
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
  public void destroyApp(boolean b) {
      // TODO release network resources
  }

  /** @see MIDlet#pauseApp() */
  protected void pauseApp() {
      // TODO break current connection
      // post wait method on server
  }

  /**
   * Starts the application.
   * @see javax.microedition.midlet.MIDlet#startApp()
   */
  protected void startApp() {
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

  /**
   * Returns the MIDlets application property.
   * 
   * @param key the key.
   * @return the application property to the corresponding key.
   * @see MIDlet#getAppProperty(String)
   */
  public static String getProperty(String key) {
    return instance.getAppProperty(key);
  }
}
