/*
 * Created on 19.04.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;

import test.sim.net.XmlMessage;

/**
 * @author jorge
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StatusAlert extends XmlMessage {

   private XmlMessage StatusMsg;

   public StatusAlert(StatusMsg statusMsg, Display d) {

      Alert statusAlert = new Alert("Status");
      statusAlert.setTimeout(Alert.FOREVER);
      statusAlert.setString(statusMsg);
      d.setCurrent(statusAlert);
   }

}
