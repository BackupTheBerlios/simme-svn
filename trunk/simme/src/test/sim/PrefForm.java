package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

import test.sim.util.PersonalPrefs;
import test.sim.util.PrefsException;

/**
 * A List that gives the user choices to customize this application.
 *
 * @author kariem
 */
public class PrefForm extends List implements CommandListener {
  // list items
  private static final String[] MAIN = { "Internet", "Hilfe" };
  private List myself;

  /**
   * Creates a new PrefForm object.
   */
  public PrefForm() {
    super("Preferences", List.IMPLICIT, MAIN, null);
    init();
  }

  private void init() {
    addCommand(new Command("Zurück", Command.BACK, 0));
    addCommand(new Command("OK", Command.OK, 1));
    setCommandListener(this);
    myself = this;
  }

  private Screen makePersPrefs() {
    final Form frmPrefs = new Form("Einstellungen - Internet");
    final PersonalPrefs prefs = PersonalPrefs.getInstance();
    String[] data = new String[4];
    final boolean newRecord;

    try {
      prefs.open();
      System.out.println("PrefsSize: " + prefs.currentSize());

      if (prefs.currentSize() == 0) {
        newRecord = true;

        // Preferences are empty - set default values
        data[0] = "";
        data[1] = "";
        data[2] = "";
        data[3] = System.getProperty("microedition.platform");
      } else {
        newRecord = false;

        // load Preferences
        prefs.load();

        // set values from records
        data = prefs.getSavedData();
      }
    } catch (PrefsException e) {
      Alert a = new Alert("Error", e.getMessage(), null, AlertType.ERROR);

      return a;
    }

    // set for this Form accordingly
    final TextField tfNick = new TextField("Nick Name: ", data[0], 10, TextField.ANY);
    final TextField tfPass = new TextField("Passwort: ", data[1], 10, TextField.ANY);
    final TextField tfInfo = new TextField("Info (optional): ", data[2], 40, TextField.ANY);
    final StringItem siPlatform = new StringItem("Client Model: ", data[3]);

    // add items to form
    frmPrefs.append(tfNick);
    frmPrefs.append(tfPass);
    frmPrefs.append(tfInfo);
    frmPrefs.append(siPlatform);
    frmPrefs.append(new StringItem("Version: ", Sim.getProperty("MIDlet-Version")));

    // command handling
    frmPrefs.addCommand(new Command("Speichern", Command.BACK, 1));
    frmPrefs.addCommand(new Command("Abbrechen", Command.CANCEL, 0));

    frmPrefs.setCommandListener(new CommandListener() {
      public void commandAction(Command c, Displayable d) {
        if (c.getCommandType() == Command.BACK) {
          String[] newData =
            { tfNick.getString(), tfPass.getString(), tfInfo.getString(), siPlatform.getText()};
          prefs.setSavedData(newData);

          try {
            if (newRecord) {
              prefs.save();
            } else {
              prefs.update();
            }
          } catch (PrefsException e) {
            ; // ??
          }
        }

        try {
          prefs.close();
        } catch (PrefsException e) {
          ; // ??
        }

        // show last display
        Sim.getDisplay().setCurrent(myself);
      }
    });

    return frmPrefs;
  }

  /**
   * @see CommandListener#commandAction(Command, Displayable)
   */
  public void commandAction(Command cmd, Displayable disp) {
    Display d = Sim.getDisplay();

    if (cmd.getCommandType() == Command.BACK) {
      d.setCurrent(Sim.getMainScreen());
    }

    if ((cmd == List.SELECT_COMMAND) || (cmd.getCommandType() == Command.OK)) {
      Screen s = null;

      switch (getSelectedIndex()) {
        case 0 : // Personal
          s = makePersPrefs();

          break;

        case 1 : // Help
          break;
      }

      if (s != null) {
        d.setCurrent(s);
      }
    }
  }
}
