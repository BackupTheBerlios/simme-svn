package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;


/**
 * DOCUMENT ME!
 *
 * @author Jorge
 */
public class StartForm extends List implements CommandListener {

   private static final String[] CHOICES =
   { "Spielen", "Einstellungen", "Info", "About" };


   private Sim sim;
   private Zeichenblatt zeichenblatt;

   /**
    * Creates a new StartForm object.
    *
    * @param sim DOCUMENT ME!
    */
   public StartForm(Sim sim) {
      super("SimME", List.IMPLICIT, CHOICES, null);
      this.sim = sim;
      addCommand(new Command("Exit", Command.EXIT, 0));
      addCommand(new Command("OK", Command.OK, 1));
      setCommandListener(this);
   }


   /**
    * DOCUMENT ME!
    *
    * @param cmd DOCUMENT ME!
    * @param disp DOCUMENT ME!
    */
   public void commandAction(Command cmd, Displayable disp) {
      if (cmd.getCommandType() == Command.EXIT) {
         sim.destroyApp(false);
         sim.notifyDestroyed();
      } else {
         Display d = Display.getDisplay(sim);

         switch (getSelectedIndex()) {
            case 0 : // Neues Spiel
               zeichenblatt = new Zeichenblatt(sim);
               d.setCurrent(zeichenblatt);

               break;

            case 1 : // Einstellungen
               d.setCurrent(new PrefForm(sim));

               break;

            case 2 : // Info

               /*Alert alert = new Alert("Not Available", "Under Construction", null, AlertType.INFO);
               alert.setTimeout(Alert.FOREVER);
               Display.getDisplay(sim).setCurrent(alert, this);*/
               Info.showInfo(Display.getDisplay(sim));

               break;

            case 3 : // About

               Alert alert2 =
                  new Alert(
                     "About",
                     "SimME\n by SPIESSEIN\n based on HEXI\n by Prof. Slany\n",
                     null, AlertType.INFO);
               alert2.setTimeout(5000);
               Display.getDisplay(sim).setCurrent(alert2, this);

               break;
         }
      }
   }
}
