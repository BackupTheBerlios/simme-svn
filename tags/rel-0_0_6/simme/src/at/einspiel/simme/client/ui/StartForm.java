//----------------------------------------------------------------------------
//[Simme]
//  Java Source File: StartForm.java
//             $Date: 2004/09/13 15:26:53 $
//         $Revision: 1.4 $
//----------------------------------------------------------------------------
package at.einspiel.simme.client.ui;

import at.einspiel.simme.client.Sim;
import at.einspiel.simme.client.util.UIUtils;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * A Form that shows some options - main menu of the game.
 *
 * @author Jorge
 */
public class StartForm extends List implements CommandListener {
  private static final String[] CHOICES = { "Spielen", "Einstellungen", "Info", "About" };
  private Sim sim;
  private GameModeForm gamemode;

  /**
   * Creates a new StartForm object.
   *
   * @param s The main MIDlet
   */
  public StartForm(Sim s) {
    super("SimME", List.IMPLICIT, CHOICES, null);
    this.sim = s;
    addCommand(UIUtils.CMD_QUIT);
    setCommandListener(this);
  }

  /** @see CommandListener#commandAction(Command, Displayable) */
  public void commandAction(Command cmd, Displayable disp) {
    if (cmd.getCommandType() == Command.EXIT) {
      sim.destroyApp(false);
      sim.notifyDestroyed();
    } else {
      Display d = Sim.getDisplay();

      switch (getSelectedIndex()) {
        case 0 : // Neues Spiel
          gamemode = new GameModeForm();
          d.setCurrent(gamemode);
          break;

        case 1 : // Einstellungen
          d.setCurrent(new PrefForm());
          break;

        case 2 : // Info
          Info.showInfo(d);
          break;

        case 3 : // About
          Alert alert2 =
            new Alert(
              "About",
              "SimME\n by SPIESSEIN\n based on HEXI\n by Prof. Slany\n",
              null,
              AlertType.INFO);
          alert2.setTimeout(Alert.FOREVER);
          d.setCurrent(alert2, this);
          break;
      }
    }
  }
}
