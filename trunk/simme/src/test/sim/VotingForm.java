package test.sim;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.ContentConnection;
import javax.microedition.lcdui.*;

/**
 * Provides mechanism to vote for simme (1-5).
 * 
 * @author kariem
 */
public class VotingForm extends List implements CommandListener {

   private Displayable caller;

   private static final String[] POINTS =
      { "1 - spitze !", "2 - gut", "3 - naja", "4 - mmh", "5 - sauerei" };

   private int proId = 6464;
   private int appId = 4526;
   private int voteId = 1037;

   /**
    * Simple constructor.
    * 
    * @param caller the fallback after launching a command.
    */
   public VotingForm(Displayable caller) {
      super("Bewerten Sie SimME", List.IMPLICIT, POINTS, null);
      addCommand(new Command("Cancel", Command.CANCEL, 0));
      setCommandListener(this);
      this.caller = caller;
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable arg1) {
      Display d = Sim.getDisplay();

      if (cmd.getCommandType() == Command.CANCEL) {
         d.setCurrent(caller);
      } else {

         // calculate points
         int score = getSelectedIndex() + 1;

         try {
            String result =
               gzApiSetHighscore(proId, appId, voteId, score).trim();
            System.out.println("result: " + result);
         } catch (IOException e) {
            e.printStackTrace();
         } finally {
            StringBuffer text = new StringBuffer();
            text.append("Sie haben SimME mit der Note ");
            text.append(score);
            text.append(" bewertet. Wir bedanken uns für die Teilnahme.");
            Alert alert =
               new Alert(
                  "Voting abgeschickt",
                  text.toString(),
                  null,
                  AlertType.INFO);
            d.setCurrent(alert, caller);
         }
      }
   }

   private String gzApiSetHighscore(
      int proId,
      int appId,
      int voteId,
      int score)
      throws IOException {
      StringBuffer buffer = new StringBuffer();
      String gzApiSetHighscore =
         "http://gamezone.xidris.com/cgi-bin/api/SetHighscore.cgi?";

      ContentConnection c = null;
      InputStream is = null;

      StringBuffer cmd = new StringBuffer();
      cmd.append(gzApiSetHighscore);
      cmd.append("gz_pro_id=");
      cmd.append(proId);
      cmd.append("&gz_app_id=");
      cmd.append(appId);
      cmd.append("&gz_score=");
      cmd.append(voteId);
      cmd.append(score);

      try {
         c = (ContentConnection) Connector.open(cmd.toString());
         is = c.openInputStream();
         buffer.setLength(0);

         int len = (int) c.getLength();

         if (len > 0) {
            byte[] data = new byte[len];
            //int actual = is.read(data);
            is.read(data);
            for (int i = 0; i < data.length; i++) {
               buffer.append((char) data[i]);
            }

         } else {
            int ch;
            while ((ch = is.read()) != -1) {
               buffer.append((char) ch);
            }
         }

      } finally {
         if (is != null) {
            is.close();
         }

         if (c != null) {
            c.close();
         }
      }

      return buffer.toString();
   }

}
