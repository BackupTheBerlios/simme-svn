package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Screen;
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
   private static final String[] MAIN =
   { "Personal", "Colors", "Options", "Help" };

   // user prefs
   private static final String[] LOCATION =
   {
      "AT", "DE", "CH", "ES", "FR", "UK", "other EU", "other Europe", "US", "CA",
      "Central America", "South America", "North Africa", "Central Africa",
      "Southern Africa", "Middle East", "Central Asia", "Far East",
      "Australia & Oceania"
   };
   private static final String[] LANGUAGE =
   { "Deutsch", "English", "Français", "Español" };


   private Sim parent;
   private List myself;


   /**
    * Creates a new PrefForm object.
    *
    * @param parent The parent of this form.
    */
   public PrefForm(Sim parent) {
      super("Preferences", List.IMPLICIT, MAIN, null);
      this.parent = parent;
      init();
   }


   private void init() {
      addCommand(new Command("Back", Command.BACK, 0));
      addCommand(new Command("OK", Command.OK, 1));
      setCommandListener(this);
      myself = this;
   }

   private Screen makePersPrefs() {
      final Form frmPrefs = new Form("Personal Preferences");
      final PersonalPrefs prefs = PersonalPrefs.getInstance();
      String[] data = new String[7];
      final boolean newRecord;

      try {
         prefs.open();
         System.out.println("PrefsSize: " + prefs.currentSize());

         if (prefs.currentSize() == 0) {
            newRecord = true;

            // Preferences are empty - set default values
            data[0] = "";
            data[1] = "";
            data[2] = "Good Game!";
            data[3] = "Deutsch";
            data[4] = "";
            data[5] = "AT";
            data[6] = System.getProperty("microedition.platform");
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
      final TextField tfNick =
         new TextField(
            "Nick", data[0], 8, TextField.ANY | TextField.NON_PREDICTIVE);
      final TextField tfPass =
         new TextField(
            "Pass", data[1], 8,
            TextField.ANY | TextField.SENSITIVE | TextField.NON_PREDICTIVE);
      final TextField tfWin =
         new TextField(
            "Win-Msg", data[2], 20, TextField.ANY | TextField.NON_PREDICTIVE);

      final ChoiceGroup cLang;

      if (data[3].equals("Deutsch")) {
         cLang = new ChoiceGroup("Sprache", ChoiceGroup.POPUP, LANGUAGE, null);
      } else {
         String[] lang = new String[LANGUAGE.length + 1];
         lang[0] = data[3];
         System.arraycopy(LANGUAGE, 0, lang, 1, LANGUAGE.length);
         cLang = new ChoiceGroup("Sprache", ChoiceGroup.POPUP, lang, null);
      }

      final TextField tfInfo =
         new TextField("Info (opt.)", data[4], 8, TextField.ANY);

      final ChoiceGroup cLoc;

      if (data[5].equals("AT")) {
         cLoc =
            new ChoiceGroup(
               "Location (opt.)", ChoiceGroup.POPUP, LOCATION, null);
      } else {
         String[] location = new String[LOCATION.length + 1];
         location[0] = data[5];
         System.arraycopy(LOCATION, 0, location, 1, LOCATION.length);
         cLoc =
            new ChoiceGroup(
               "Location (opt.)", ChoiceGroup.POPUP, location, null);
      }

      final TextField tfPlatform =
         new TextField(
            "Client Model", data[6], data[6].length(),
            TextField.ANY | TextField.UNEDITABLE);

      // add items to form
      frmPrefs.append(tfNick);
      frmPrefs.append(tfPass);
      frmPrefs.append(tfWin);
      frmPrefs.append(cLang);
      frmPrefs.append(tfInfo);
      frmPrefs.append(cLoc);
      frmPrefs.append(tfPlatform);

      // command handling
      frmPrefs.addCommand(new Command("Save & Back", Command.BACK, 0));
      frmPrefs.addCommand(new Command("Cancel", Command.CANCEL, 0));

      frmPrefs.setCommandListener(
         new CommandListener() {
            public void commandAction(Command c, Displayable d) {
               if (c.getCommandType() == Command.BACK) {
                  String[] newData =
                  {
                     tfNick.getString(), tfPass.getString(), tfWin.getString(),
                     cLang.getString(cLang.getSelectedIndex()),
                     tfInfo.getString(), cLoc.getString(
                        cLoc.getSelectedIndex()), tfPlatform.getString()
                  };
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
               Display.getDisplay(parent).setCurrent(myself);
            }
         });

      return frmPrefs;
   }

   /**
    * @see CommandListener#commandAction(Command, Displayable)
    */
   public void commandAction(Command cmd, Displayable disp) {
      Display d = Display.getDisplay(parent);

      if (cmd.getCommandType() == Command.BACK) {
         d.setCurrent(parent.getMainScreen());
      }

      if ((cmd == List.SELECT_COMMAND) || (cmd.getCommandType() == Command.OK)) {
         Screen s = null;

         switch (getSelectedIndex()) {
            case 0 : // Personal
               s = makePersPrefs();

               break;

            case 1 : // Colors
               break;

            case 2 : // Options
               break;

            case 3 : // Help
               break;
         }

         if (s != null) {
            d.setCurrent(s);
         }
      }
   }
}
