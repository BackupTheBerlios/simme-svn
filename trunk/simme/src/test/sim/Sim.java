package test.sim;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * Class 
 * @author georg
 */
public class Sim extends MIDlet implements CommandListener {

   Zeichenblatt c;
   List main;

   private static final String[] CHOICES = { "Neues Spiel", "Einstellungen", "Info" };

   public Sim() {
      main = new List("Welcome to SimME", List.IMPLICIT, CHOICES, null);

      main.addCommand(new Command("Exit", Command.EXIT, 0));
      main.setCommandListener(this);

      c = new Zeichenblatt(this);
   }

   protected void destroyApp(boolean arg0) {
   }

   protected void pauseApp() {
   }

   /* (non-Javadoc)
    * @see javax.microedition.midlet.MIDlet#startApp()
    */
   protected void startApp() throws MIDletStateChangeException {
      Display d = Display.getDisplay(this);
      d.setCurrent(main);
   }

   /* (non-Javadoc)
    * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
    */
   public void commandAction(Command cmd, Displayable disp) {
      if (cmd.getCommandType() == Command.EXIT) {
         destroyApp(false);
         notifyDestroyed();
      } else {
         Display d = Display.getDisplay(this);
         switch (main.getSelectedIndex()) {
            case 0 : // Neues Spiel
               d.setCurrent(c);
               break;
            case 1 : // Einstellungen
               d.setCurrent(new PrefForm(this));
               break;
            case 2 : // Info
               break;
         }
      }
   }
   
   public Screen getMainScreen() {
   	return main;
   }

}
