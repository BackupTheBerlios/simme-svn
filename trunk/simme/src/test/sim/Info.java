package test.sim;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;

/**
 * Shows information on this game.
 *
 * @author jorge
 */
public class Info {
   
   private static final StringBuffer BUF = new StringBuffer();

   static {
      BUF.append("Das Spiel besteht aus 6 Knoten die jeder mit jedem ");
      BUF.append("verbunden sind. Der Spieler der an der Reihe ist wählt ");
      BUF.append("eine Verbindung zwischen 2 Knoten aus. Diese wird dann in ");
      BUF.append("seiner Farbe angezeigt. Dann wählt der 2. Spieler eine ");
      BUF.append("Verbindung und markiert sie in seiner Farbe usw. ");
      BUF.append("Der erste Spieler der in seiner Farbe ein Dreieck formt ");
      BUF.append("hat verloren. ");
      BUF.append("Ziel ist es also zu vermeiden ein Dreieck zu formen und ");
      BUF.append("den Gegenspieler zum Dreieck zu drängen.");
      BUF.append("\n \n \n");
      BUF.append("Um eine Verbindung auszuwählen drückt man auf eine ");
      BUF.append("Nummer zwischen 1 und 6 auf dem Telefon um den ersten ");
      BUF.append("Knoten zu markieren und das gleiche nochmals um den ");
      BUF.append("zweiten Knoten und somit eine Verbindung zu markieren, ");
      BUF.append("sollte man sich nach der Wahl des ersten Knotens ");
      BUF.append("umentscheiden kann man durch nochmaliges klicken auf die ");
      BUF.append("selbe Taste den Knoten wieder deaktivieren.");
      
   }

   private static final String HOWTO = BUF.toString();
      

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
      alert.setString(HOWTO);

      display.setCurrent(alert);
   }
}
