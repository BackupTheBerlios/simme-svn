package test.sim;

import javax.microedition.lcdui.*;

/**
 * This is the first form that is seen, when the user comes online. 
 * 
 * @author kariem
 */
public class OnlineForm extends List implements CommandListener {

   /**
    * @param arg0
    * @param arg1
    */
   public OnlineForm() {
      super("SimME Online", List.IMPLICIT);
      append("Sofort beginnen", null);
      append("Voting", null);
      addCommand(new Command("Ausloggen", Command.EXIT, 0));
      setCommandListener(this);
   }

   /** @see CommandListener#commandAction(Command, Displayable) */
   public void commandAction(Command cmd, Displayable arg1) {
      Display d = Sim.getDisplay();
      if (cmd.getCommandType() == Command.EXIT) {
         d.setCurrent(Sim.getMainScreen());
      } else {
         switch (getSelectedIndex()) {
            case 0 : // Sofort

               // TODO initialize game

               break;

            case 1 : // Voting
               VotingForm f = new VotingForm(this);
               d.setCurrent(f);

               break;
         }
      }
   }

}
