package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * @author Jorge De Mar
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Info {

	private static String howto =
		"Das Spiel besteht aus 6 Knoten die jeder mit jedem "
			+ "verbunden sind. Der Spieler der an der Reihe ist wählt "
			+ "eine Verbindung zwischen 2 Knoten aus. Diese wird dann in "
			+ "seiner Farbe angezeigt. Dann wählt der 2. Spieler eine "
			+ "Verbindung und markiert sie in seiner Farbe usw. "
			+ "Der erste Spieler der in seiner Farbe ein Dreieck formt "
			+ "hat verloren. "
			+ "Ziel ist es also zu vermeiden ein Dreieck zu formen und "
			+ "den Gegenspieler zum Dreieck zu drängen."
			+ "\n \n \n"
			+ "Um eine Verbindung auszuwählen drückt man auf eine "
			+ "Nummer zwischen 1 und 6 auf dem Telefon um den ersten "
			+ "Knoten zu markieren und das gleiche nochmals um den "
			+ "zweiten Knoten und somit eine Verbindung zu markieren, "
			+ "sollte man sich nach der Wahl des ersten Knotens "
			+ "umentscheiden kann man durch nochmaliges klicken auf die "
			+ "selbe Taste den Knoten wieder deaktivieren. ";

	private Displayable previous;

	private Info() {
	}

	public static void showInfo(Display display) {

		Alert alert = new Alert("About MIDP");
		alert.setTimeout(Alert.FOREVER);
		alert.setString(howto);

		display.setCurrent(alert);
	}
}
