package test.sim;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * @author jorge
 */
public class GameModeForm extends List implements CommandListener {

   private static final String[] CHOICES = { "Internet Spiel", "Lokales Spiel" };

   private Sim sim;
   private StartForm startform;
   private Zeichenblatt zeichenblatt;

   public GameModeForm(Sim sim) {
      super("Game Mode", List.IMPLICIT, CHOICES, null);
      this.sim = sim;
      addCommand(new Command("Back", Command.BACK, 0));
      addCommand(new Command("OK", Command.OK, 1));
      setCommandListener(this);
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable disp) {
      if (cmd.getCommandType() == Command.EXIT) {
         sim.destroyApp(false);
         sim.notifyDestroyed();
      } else {
         Display d = Display.getDisplay(sim);

         switch (getSelectedIndex()) {
            case 0 : // Internet Spiel
               d.setCurrent(sim.getMainScreen());

               break;

            case 1 : // Lokales Spiel
               zeichenblatt = new Zeichenblatt(sim);
               d.setCurrent(zeichenblatt);

               break;
         }
      }
   }
}