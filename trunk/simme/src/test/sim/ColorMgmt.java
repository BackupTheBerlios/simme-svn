package test.sim;

import javax.microedition.lcdui.Display;

/**
 * This class allows for easy color definitions and should contain methods
 * to efficiently display the game using a rich color set suitable for all
 * devices. 
 * @author kariem
 */
public class ColorMgmt {

   /** Inner and outer colors of player1's lines */
   public static int p1c1, p1c2;
   /** Inner and outer colors of player2's lines */
   public static int p2c1, p2c2;
   /** Inner and outer colors of neutral lines */
   public static int nc1, nc2;

   /** Inner and outer colors of a normal node */
   public static int nnc1, nnc2;
   /** Inner and outer colors of a selected node */
   public static int nsc1, nsc2;
   /** Inner and outer colors of a disabled node */
   public static int ndc1, ndc2;
   
   /** background */
   public static int bg;

   /**
    * Sets the color values according to properties of the given
    * <code>Display</code> 
    * @param d
    */
   public static void setDisplay(Display d) {
      /* commonly used colors: 
         COLOR_BACKGROUND,COLOR_FOREGROUND,
         COLOR_HIGHLIGHTED_BACKGROUND, COLOR_HIGHLIGHTED_FOREGROUND,
         COLOR_BORDER, or COLOR_HIGHLIGHTED_BORDER
      */

      p1c1 = 0x00FF0000; //d.getColor(Display.COLOR_HIGHLIGHTED_BACKGROUND);
      p1c2 = 0;          //d.getColor(Display.COLOR_FOREGROUND);

      p2c1 = 0x0000FF00; //d.getColor(Display.COLOR_BACKGROUND);
      p2c2 = 0;          //d.getColor(Display.COLOR_HIGHLIGHTED_FOREGROUND);

      nnc1 = nc1 = 0x00888888; //d.getColor(Display.COLOR_BACKGROUND);
      nnc2 = nc2 = 0;          //d.getColor(Display.COLOR_FOREGROUND);

      nsc1 = 0x0000FF00; //d.getColor(Display.COLOR_HIGHLIGHTED_BORDER);
      nsc2 = 0;          //d.getColor(Display.COLOR_BORDER);

      ndc1 = 0;          //d.getColor(Display.COLOR_FOREGROUND);
      ndc2 = 0x00888888; //d.getColor(Display.COLOR_BACKGROUND);

		bg = 0xA0AFFF;

		// TODO Herausfinden der nötigen Farben
		
      /* later
      if (d.isColor()) {
      	// having color display
      } else {
      	// no color display 
      }
      */
   }
}
