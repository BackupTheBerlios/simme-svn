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

               // get preferences
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
                  Alert error =
                     new Alert(
                        "Fehler",
                        pex.getMessage(),
                        null,
                        AlertType.ERROR);
                  error.setTimeout(Alert.FOREVER);
                  d.setCurrent(error, this);
                  return;
               }

               ConnectionAlert infoAlert =
                  new ConnectionAlert(prefs.getSavedData());
               d.setCurrent(infoAlert);
               infoAlert.startConnection(d);

               break;

            case 1 : // Lokales Spiel
               Zeichenblatt zeichenblatt = new Zeichenblatt();
               d.setCurrent(zeichenblatt);

               break;
         }
      }
   }

   private class ConnectionAlert extends Alert {
      private String[] loginData;

      /**
       * Constructs an empty <code>InfoAlert</code> with the given title
       */
      public ConnectionAlert(String[] data) {
         super("Verbinde");
         setString("Verbindung zum Server wird hergestellt.");
         setTimeout(LoginMessage.DEFAULT_TIMEOUT - 20); // 20 ms less
         loginData = data;
         for (int i = 0; i < data.length; i++) {
            if (data[i] != null) {
               System.out.println(i + ":" + data[i]);
            }
         }
         System.out.println("connectionalert created");
      }

      /**
       * Initializes the connection and shows its output.
       */
      public void startConnection(final Display d) {
         // enter new thread, so that the user interface is updated correctly
         Thread t = new Thread() {
            /** @see java.lang.Thread#run() */
            public void run() {
               try {
                  System.out.println("constructing message");

                  // construct login message
                  String version = Sim.getProperty("MIDlet-Version");
                  System.out.println(version);
                  LoginMessage loginMsg = null;
                  try {
                     loginMsg =
                        new LoginMessage(
                           loginData[0],
                           loginData[1],
                           loginData[3],
                           version);
                  } catch (NullPointerException pppe) {
                     pppe.printStackTrace();
                  }

                  System.out.println("sending message");
                  loginMsg.sendRequest("doLogin");

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
                     // no success => show cause
                     AlertType type =
                        result.isSucceed() ? AlertType.INFO : AlertType.ERROR;
                     Alert loginAlert =
                        new Alert(
                           "Fehler bei Login",
                           result.getMessage(),
                           null,
                           type);
                     loginAlert.setTimeout(FOREVER);
                     System.out.println("login error");
                     d.setCurrent(loginAlert);
                  }

               } catch (IOException ioex) {
                  System.out.println("connection error");
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