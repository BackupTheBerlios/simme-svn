package test.sim;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 * @author Jorge De Mar
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GameModeForm extends List implements CommandListener {

	private static final String[] CHOICES =
		{ "Lokales Spiel", "Internet Spiel" };

	private Sim sim;
	private StartForm startform;
	private Zeichenblatt zeichenblatt;

	public GameModeForm(Sim sim) {
		super("Game Mode", List.IMPLICIT, CHOICES, null);
		this.sim = sim;
		addCommand(new Command("Exit", Command.EXIT, 0));
		addCommand(new Command("OK", Command.OK, 1));
		setCommandListener(this);
	}

	/**
	    * DOCUMENT ME!
	    *
	    * @param cmd DOCUMENT ME!
	    * @param disp DOCUMENT ME!
	    */
	public void commandAction(Command cmd, Displayable disp) {
		if (cmd.getCommandType() == Command.EXIT) {
			sim.destroyApp(false);
			sim.notifyDestroyed();
		} else {
			Display d = Display.getDisplay(sim);

			switch (getSelectedIndex()) {
				case 0 : // Lokales Spiel
					zeichenblatt = new Zeichenblatt(sim);
					d.setCurrent(zeichenblatt);

					break;

				case 1 : // Internet Spiel
					d.setCurrent(new StartForm(sim));

					break;
			}
		}
	}
}