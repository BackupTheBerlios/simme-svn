package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

/**
 * @author Jorge De Mar
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class StartForm extends Form implements CommandListener {

	private Sim sim;
	private Command playCommand;
	private Command preferenceCommand;
	private Command infoCommand;
	private Command aboutCommand;
	private Command exitCommand;
	
	
	public StartForm(Sim sim) {
		super("SimME");
		this.sim = sim;
		StringItem item = new StringItem (
		"Welcome to SimME !", null);
		append(item);
		playCommand = new Command("Play", Command.SCREEN, 1);
		preferenceCommand = new Command("Preferences", Command.SCREEN, 2);
		infoCommand = new Command("Info", Command.SCREEN, 3);
		aboutCommand = new Command("About", Command.SCREEN, 4);
		exitCommand = new Command("Exit", Command.SCREEN, 5);
		
		addCommand(playCommand);
		addCommand(preferenceCommand);
		addCommand(infoCommand);
		addCommand(aboutCommand);
		addCommand(exitCommand);
		setCommandListener(this);
	}
	
	public void commandAction(Command c, Displayable s) {
		
		if (c.equals(playCommand)) {
			sim.play();
		}
		else if (c.equals(preferenceCommand)) {
			StringBuffer buf = new StringBuffer("");
			buf.append("Under Construction");
			
			Alert alert = new Alert("Preferences", buf.toString(), null, AlertType.INFO);
			alert.setTimeout(Alert.FOREVER);
			sim.getDisplay().setCurrent(alert, this);
		}
		else if (c.equals(infoCommand)) {
			StringBuffer buf2 = new StringBuffer("");
			buf2.append("Under Construction");
			
			Alert alert2 = new Alert("Info", buf2.toString(), null, AlertType.INFO);
			alert2.setTimeout(Alert.FOREVER);
			sim.getDisplay().setCurrent(alert2, this);
		}
		else if (c.equals(aboutCommand)) {
			StringBuffer buf3 = new StringBuffer("");
			buf3.append("SimME");
			buf3.append("by SPIESSEIN\n");
			buf3.append("based on HEXI\n");
			buf3.append("by Prof. Slany\n");
			
			Alert alert3 = new Alert("About", buf3.toString(), null, AlertType.INFO);
			alert3.setTimeout(5000);
			sim.getDisplay().setCurrent(alert3, this);
		}
		else if (c.equals(exitCommand)) {
			sim.destroyApp(false);
         	sim.notifyDestroyed();
		}
	}
}
