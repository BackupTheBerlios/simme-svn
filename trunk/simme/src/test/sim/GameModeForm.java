package test.sim;

import java.io.IOException;

import javax.microedition.lcdui.*;

import test.sim.net.LoginMessage;
import test.sim.net.LoginResult;
import test.sim.util.PersonalPrefs;
import test.sim.util.PrefsException;

/**
 * @author jorge
 */
public class GameModeForm extends List implements CommandListener {

   private static final String[] CHOICES =
      { "Internet Spiel", "Lokales Spiel" };

   private Zeichenblatt zeichenblatt;

   /**
    * Creates a new Form where the user can choose between several types of
    * games.
    */
   public GameModeForm() {
      super("Game Mode", List.IMPLICIT, CHOICES, null);
      addCommand(new Command("Back", Command.BACK, 0));
      addCommand(new Command("OK", Command.OK, 1));
      setCommandListener(this);
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable disp) {
      Display d = Sim.getDisplay();
      if (cmd.getCommandType() == Command.BACK) {
         d.setCurrent(Sim.getMainScreen());
      } else {

         switch (getSelectedIndex()) {
            case 0 : // Internet Spiel
               ConnectionAlert infoAlert = new ConnectionAlert("Verbinde");
               d.setCurrent(infoAlert);
               infoAlert.startConnection(d);

               break;

            case 1 : // Lokales Spiel
               zeichenblatt = new Zeichenblatt();
               d.setCurrent(zeichenblatt);

               break;
         }
      }
   }

   private class ConnectionAlert extends Alert {
      /**
       * Constructs an empty <code>InfoAlert</code> with the given title
       */
      public ConnectionAlert(String title) {
         super(title);
         setTimeout(LoginMessage.DEFAULT_TIMEOUT - 20); // 20 ms less
      }

      /**
       * Initializes the connection and shows its output.
       */
      public void startConnection(final Display d) {
         setString("Verbindung zum Server wird aufgebaut");

         // enter new thread, so that the user interface is updated correctly
         Thread t = new Thread() {
            /** @see java.lang.Thread#run() */
            public void run() {
               try {
                  PersonalPrefs prefs = PersonalPrefs.getInstance();
                  // load nick name, password and additional info
                  try {
                     prefs.open();
                     if (prefs.currentSize() == 0) {
                        // empty prefs => error message
                        StringBuffer buf = new StringBuffer();
                        buf.append("Nickname und Password müssen eingegeben ");
                        buf.append("werden. Gehen Sie bitte hierfür auf ");
                        buf.append("\"Einstellungen|Internet\" im Hauptmenü.");
                        throw new PrefsException(buf.toString());
                     }
                     prefs.load();
                  } catch (PrefsException pex) {
                     // error occurred while loading
                     Alert errorAlert =
                        new Alert(
                           "Fehler",
                           pex.getMessage(),
                           null,
                           AlertType.ERROR);
                     errorAlert.setTimeout(FOREVER);
                     d.setCurrent(errorAlert);
                     return;
                  }
                  // construct login message
                  String[] loginData = prefs.getSavedData();
                  String version = Sim.getProperty("MIDlet-Version");
                  LoginMessage loginMsg =
                     new LoginMessage(
                        loginData[0],
                        loginData[1],
                        loginData[3],
                        version);

                  //loginMsg.sendRequest("doLogin.jsp");
                  loginMsg.sendRequest("doLogin.jsp");

                  // get response
                  String response = new String(loginMsg.getResponse());

                  // use response to build result
                  LoginResult result = new LoginResult(response);

                  System.out.println("result: " + result);

                  if (result.isSucceed()) {
                     //DynamicUI dUI = new DynamicUI(result.getMessage());
                     //d.setCurrent(dUI.getDisplayable());
                     d.setCurrent(new OnlineForm());
                  } else {
                     // no success => show info
                     AlertType type =
                        result.isSucceed() ? AlertType.INFO : AlertType.ERROR;
                     Alert loginAlert =
                        new Alert(
                           "Fehler bei Login",
                           result.getMessage(),
                           null,
                           type);
                     loginAlert.setTimeout(FOREVER);
                     d.setCurrent(loginAlert);
                  }

               } catch (IOException ioex) {
                  String errorMsg = ioex.getMessage();
                  // if errorMsg doesn't contain any information, show some standard text
                  if ((errorMsg == null) || (errorMsg.length() == 0)) {
                     errorMsg = "Es konnte keine Verbindung hergestellt werden";
                  }
                  Alert errorAlert =
                     new Alert("Fehler", errorMsg, null, AlertType.ERROR);
                  errorAlert.setTimeout(FOREVER);
                  d.setCurrent(errorAlert);
               }
            }
         };
         t.start();
      }
   }

}