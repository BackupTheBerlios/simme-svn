package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

import test.sim.net.StatusMessage;

/**
 * This alert is intended to show a certain message to the user by displaying
 * an alert. 
 * 
 * @author jorge
 */
public class StatusAlert extends Alert {

   /**
    * Creates a new status alert showing the message contained in the
    * <code>statusMsg</code>.
    *  
    * @param statusMsg the status message containing information.
    * @param d <code>Display</code> to use for showing this alert.
    */
   public StatusAlert(StatusMessage statusMsg, Display d) {
      super("Info", statusMsg.getString(), null, AlertType.INFO);
      setTimeout(3000);
      d.setCurrent(this);
   }

}
