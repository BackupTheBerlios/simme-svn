package at.einspiel.simme.client;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * This is the first form that is seen, when the user comes online.
 *
 * @author kariem
 */
public class OnlineForm extends List implements CommandListener {
   /**
    * Creates a new main menu for when the user is online.
    */
   public OnlineForm() {
      super("SimME -Online-", List.IMPLICIT);
      append("Play immediately", null);
      append("Info", null);
      append("Exit", null);
      setCommandListener(this);
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable arg1) {
      Display d = Sim.getDisplay();

      switch (getSelectedIndex()) {
         case 0 : // Immediately

            // TODO initialize game
            break;

         case 1 : // Info

            StringBuffer buf = new StringBuffer();
            buf.append("You are currently in the main menu of SimME -Online-. ");
            buf.append("Currently you only have the choice to play a game ");
            buf.append("immediately. Future versions will permit to choose your ");
            buf.append("opponent");
            
            Alert info = new Alert("Info", buf.toString(), null, AlertType.INFO);
            d.setCurrent(info);

            break;

         case 2 : // Exit
            d.setCurrent(Sim.getMainScreen());

            break;
      }
   }
}
