package at.einspiel.simme.client.ui;

import javax.microedition.lcdui.Display;

/**
 * This class allows for easy color definitions and should contain methods to
 * efficiently display the game using a rich color set suitable for all
 * devices.
 *
 * @author kariem
 */
public class ColorMgmt {
   
   private static final int BLACK_VERY_LIGHT = 0x00222222;
   private static final int BLACK_LIGHT = 0x00111111;
   private static final int BLUE = 0x000000FF;
   private static final int GREY_DARKER = 0x00444444;
   private static final int GREY_DARK = 0x00777777;
   private static final int GREY = 0x00888888;
   private static final int GREY_LIGHT = 0x00AAAAAA;
   private static final int GREY_VERY_LIGHT = 0xDDDDDD;
   private static final int GREEN_LIGHT = 0x0000DD00;
   private static final int GREEN = 0x0000FF00;
   private static final int RED = 0x00FF0000;
   private static final int RED_DARK = 0x00DD0000;
   private static final int WHITE = 0x00FFFFFF;

   /** p1, p2, nc, nnc, nsc, ndc */
   private static final int[] COLORS_FULL = { // full colors
      RED_DARK, RED, GREEN_LIGHT, GREEN, // p1, p2 
      GREY, 0, // neutral line
      GREY, 0, // neutral node
      GREY, BLUE, // selected node
      GREY, GREY_DARK, // deactivated node 
      WHITE // background
   };

   /** p1, p2, nc, nnc, nsc, ndc  - */
   private static final int[] COLORS_GREY16 = { // grey, 16 bit
      WHITE, BLACK_LIGHT, BLACK_LIGHT, BLACK_VERY_LIGHT, // p1, p2
      GREY, 0, // neutral line
      GREY, 0, // neutral node
      GREY, GREY_LIGHT, // selected node
      GREY, GREY_DARK, // deactivated node 
      GREY_VERY_LIGHT // background
   };

   /** p1, p2, nc, nnc, nsc, ndc  - */
   private static final int[] COLORS_GREY8 = { // grey 8 bit
      WHITE, 0, BLACK_LIGHT, 0, // p1, p2
      GREY, 0, // neutral line
      GREY, 0, // neutral node
      GREY_LIGHT, 0, // selected node
      GREY_DARKER, GREY, // deactivated node 
      GREY_VERY_LIGHT // background
   };

   /** Inner and outer colors of player1's lines */
   protected static int p1c1;

   /** Inner and outer colors of player1's lines */
   protected static int p1c2;

   /** Inner and outer colors of player2's lines */
   protected static int p2c1;

   /** Inner and outer colors of player2's lines */
   protected static int p2c2;

   /** Inner and outer colors of neutral lines */
   protected static int nc1;

   /** Inner and outer colors of neutral lines */
   protected static int nc2;

   /** Inner and outer colors of a normal node */
   protected static int nnc1;

   /** Inner and outer colors of a normal node */
   protected static int nnc2;

   /** Inner and outer colors of a selected node */
   protected static int nsc1;

   /** Inner and outer colors of a selected node */
   protected static int nsc2;

   /** Inner and outer colors of a disabled node */
   protected static int ndc1;

   /** Inner and outer colors of a disabled node */
   protected static int ndc2;

   /** background */
   protected static int bg;

   /**
    * Sets the color values according to properties of the given
    * <code>Display</code>
    *
    * @param d the display for which the colors are to be set.
    */
   public static void setDisplay(Display d) {
      if (d.isColor()) {
         if (d.numColors() >= 16) {
            setColors(COLORS_FULL);

            return;
         }
      } else {
         if (d.numColors() >= 16) {
            setColors(COLORS_GREY16);

            return;
         }
      }

      setColors(COLORS_GREY8);
   }

   private static void setColors(int[] a) {
      p1c1 = a[0];
      p1c2 = a[1];

      p2c1 = a[2];
      p2c2 = a[3];

      nc1 = a[4];
      nc2 = a[5];

      nnc1 = a[6];
      nnc2 = a[7];

      nsc1 = a[8];
      nsc2 = a[9];

      ndc1 = a[10];
      ndc2 = a[11];

      bg = a[12];
   }
}
