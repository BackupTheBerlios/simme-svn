package test.sim;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;

/**
 * @author kariem
 */
public class PrefForm extends List implements CommandListener {

   private Sim parent;
   private List moi;

   // list items
   private static final String[] MAIN = { "Personal", "Colors", "Options", "Help" };

   // user prefs
   private static final String[] LOCATION =
      {
         "AT",
         "DE",
         "CH",
         "ES",
         "FR",
         "UK",
         "other EU",
         "other Europe",
         "US",
         "CA",
         "Central America",
         "South America",
         "North Africa",
         "Central Africa",
         "Southern Africa",
         "Middle East",
         "Central Asia",
         "Far East",
         "Australia & Oceania" };

   private static final String[] LANGUAGE = { "Deutsch", "English", "Français", "Español" };

   public PrefForm(Sim parent) {
      super("Preferences", List.IMPLICIT, MAIN, null);
      this.parent = parent;
      init();
   }

   private void init() {
   	addCommand(new Command("Back", Command.BACK, 0));
      setCommandListener(this);
      moi = this;
   }

   private Form makePersPrefs() {
      Form pPrefs = new Form("Personal Preferences");
      pPrefs.append(new TextField("Nick", "", 8, TextField.ANY | TextField.NON_PREDICTIVE));
      pPrefs.append(new TextField("Pass", "", 8, TextField.ANY | TextField.SENSITIVE | TextField.NON_PREDICTIVE));
      pPrefs.append(new TextField("Win-Msg", "Good Game", 20, TextField.ANY | TextField.NON_PREDICTIVE));
      pPrefs.append(new ChoiceGroup("Sprache", ChoiceGroup.POPUP, LANGUAGE, null));
      pPrefs.append(new TextField("Info (opt.)", "", 8, TextField.ANY));
      pPrefs.append(new ChoiceGroup("Location (opt.)", ChoiceGroup.POPUP, LOCATION, null));
      String platform = System.getProperty("microedition.platform");
      pPrefs.append(new TextField("Client Model", platform, platform.length(), TextField.ANY | TextField.UNEDITABLE));
      pPrefs.addCommand(new Command("Back", Command.BACK, 0));
      pPrefs.setCommandListener(new CommandListener() {
         public void commandAction(Command arg0, Displayable arg1) {
         	Display.getDisplay(parent).setCurrent(moi);         	
         }
      });
      return pPrefs;
   }

   /* (non-Javadoc)
    * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
    */
   public void commandAction(Command cmd, Displayable disp) {
   	Display d = Display.getDisplay(parent);
   	if (cmd.getCommandType() == Command.BACK) {
   		d.setCurrent(parent.getMainScreen());
   	}
      if (cmd == List.SELECT_COMMAND) {
         Form f = null;
         switch (getSelectedIndex()) {
            case 0 : // Personal
               f = makePersPrefs();
               break;
            case 1 : // Colors
               break;
            case 2 : // Options
               break;
            case 3 : // Help
               break;
         }
         if (f != null) {
            d.setCurrent(f);
         }
      }
   }

}
