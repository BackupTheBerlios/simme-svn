package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;

/**
 * Shows information on this game.
 *
 * @author jorge
 */
public class Info {

   private static final String howto =
      "Das Spiel besteht aus 6 Knoten die jeder mit jedem "
         + "verbunden sind. Der Spieler der an der Reihe ist w�hlt "
         + "eine Verbindung zwischen 2 Knoten aus. Diese wird dann in "
         + "seiner Farbe angezeigt. Dann w�hlt der 2. Spieler eine "
         + "Verbindung und markiert sie in seiner Farbe usw. "
         + "Der erste Spieler der in seiner Farbe ein Dreieck formt "
         + "hat verloren. "
         + "Ziel ist es also zu vermeiden ein Dreieck zu formen und "
         + "den Gegenspieler zum Dreieck zu dr�ngen."
         + "\n \n \n"
         + "Um eine Verbindung auszuw�hlen dr�ckt man auf eine "
         + "Nummer zwischen 1 und 6 auf dem Telefon um den ersten "
         + "Knoten zu markieren und das gleiche nochmals um den "
         + "zweiten Knoten und somit eine Verbindung zu markieren, "
         + "sollte man sich nach der Wahl des ersten Knotens "
         + "umentscheiden kann man durch nochmaliges klicken auf die "
         + "selbe Taste den Knoten wieder deaktivieren. ";

   private Info() {
   }

   /**
    * Shows the information
    *
    * @param display The display, where the information is to be shown.
    */
   public static void showInfo(Display display) {
      Alert alert = new Alert("SimME HowTo");
      alert.setTimeout(Alert.FOREVER);
      alert.setString(howto);

      display.setCurrent(alert);
   }
}
