package test.sim;

import javax.microedition.lcdui.*;

/**
 * This is the first form that is seen, when the user comes online. 
 * 
 * @author kariem
 */
public class OnlineForm extends List implements CommandListener {

   /**
    * Creates a new main menu for when the user is online.
    */
   public OnlineForm() {
      super("SimME Online", List.IMPLICIT);
      append("Sofort spielen", null);
      append("Voting", null);
      append("Info", null);
      append("Ende", null);
      setCommandListener(this);
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable arg1) {
      Display d = Sim.getDisplay();
      switch (getSelectedIndex()) {
         case 0 : // Sofort

            // TODO initialize game

            break;

         case 1 : // Voting
            VotingForm f = new VotingForm(this);
            d.setCurrent(f);

            break;

         case 2 : // Info
            StringBuffer buf = new StringBuffer();
            buf.append("Sie befinden sich im Hauptmenü von SimME online. ");
            buf.append("Hier können Sie entweder ein neues Spiel beginnen ");
            buf.append("oder für SimME auf der A1-Gaming-Zone voten.");
            Alert info =
               new Alert("Info", buf.toString(), null, AlertType.INFO);
            d.setCurrent(info);

            break;

         case 3 : // Ende
            d.setCurrent(Sim.getMainScreen());

            break;
      }
   }

}
