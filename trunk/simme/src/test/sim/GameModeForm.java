package test.sim;

import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import test.sim.net.LoginMessage;

/**
 * @author jorge
 */
public class GameModeForm extends List implements CommandListener {

   private static final String[] CHOICES = { "Internet Spiel", "Lokales Spiel" };

   private Sim sim;
   private Zeichenblatt zeichenblatt;

   /**
    * Creates a new Form where the user can choose between several types of
    * games.
    * 
    * @param sim main midlet.
    */
   public GameModeForm(Sim sim) {
      super("Game Mode", List.IMPLICIT, CHOICES, null);
      this.sim = sim;
      addCommand(new Command("Back", Command.BACK, 0));
      addCommand(new Command("OK", Command.OK, 1));
      setCommandListener(this);
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable disp) {
      Display d = Display.getDisplay(sim);
      if (cmd.getCommandType() == Command.BACK) {
         d.setCurrent(Sim.getMainScreen());
      } else {
         d = Display.getDisplay(sim);

         switch (getSelectedIndex()) {
            case 0 : // Internet Spiel
               //d.setCurrent(sim.getMainScreen());
               try {
                  LoginMessage loginMsg = new LoginMessage("firstplayer", "pass", "j2me", "1.0");
                  loginMsg.sendRequest("http://localhost:8080/simme/", "doLogin.jsp");
               } catch (IOException e) {
                  String errorMsg = e.getMessage();
                  if ((errorMsg == null) || (errorMsg.length() == 0)) {
                     errorMsg = "Es konnte keine Verbindung hergestellt werden";
                  }
                  Alert errorAlert = new Alert("Fehler", errorMsg, null, AlertType.ERROR);
                  errorAlert.setTimeout(Alert.FOREVER);
                  d.setCurrent(errorAlert);
               }

               break;

            case 1 : // Lokales Spiel
               zeichenblatt = new Zeichenblatt(sim);
               d.setCurrent(zeichenblatt);

               break;
         }
      }
   }
}