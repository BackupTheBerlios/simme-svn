package at.einspiel.simme.client.ui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

/**
 * Shows information on this game.
 *
 * @author jorge
 */
public class Info {
  private static final StringBuffer BUF = new StringBuffer();

  static {
    BUF.append("Das Spiel besteht aus 6 Knoten von denen jeder mit jedem ");
    BUF.append("verbunden ist. Der Spieler der an der Reihe ist wählt ");
    BUF.append("eine Verbindung zwischen 2 Knoten aus. Diese wird dann in ");
    BUF.append("seiner Farbe angezeigt. Dann wählt der 2. Spieler eine ");
    BUF.append("Verbindung und markiert sie in seiner Farbe usw.\n\n");
    BUF.append("Der erste Spieler der in seiner Farbe ein Dreieck formt ");
    BUF.append("hat verloren. Ziel ist es also zu vermeiden, ein Dreieck zu");
    BUF.append("formen und den Gegenspieler zum \"Dreiecken\" zu drängen.");
    BUF.append("\n\n");
    BUF.append("Um eine Verbindung auszuwählen, drückt man auf eine ");
    BUF.append("Zahl zwischen 1 und 6 um den ersten Knoten zu markieren ");
    BUF.append("und das gleiche nochmals um den zweiten Knoten und somit ");
    BUF.append("eine Verbindung zu markieren.\n\n");
    BUF.append("Sollte man sich nach der Wahl des ersten Knotens anders ");
    BUF.append("entschieden haben, kann man durch nochmaliges Klicken auf ");
    BUF.append("dieselbe Taste den Knoten wieder deaktivieren.\n\n");
    BUF.append("Mit der Taste 0 ist jederzeit ein Undo möglich!\n\n");
    BUF.append("Viel Spass!");
  }

  private static final String HOWTO = BUF.toString();

  private Info() {
      // private constructor for utility class
  }

  /**
   * Shows the information
   *
   * @param display The display, where the information is to be shown.
   */
  public static void showInfo(Display display) {
    Alert alert = new Alert("SimME HowTo", HOWTO, null, AlertType.INFO);
    alert.setTimeout(Alert.FOREVER);

    display.setCurrent(alert);
  }
}
