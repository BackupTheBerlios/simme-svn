package test.sim;

import javax.microedition.lcdui.Display;


/**
 * This class allows for easy color definitions and should contain methods to
 * efficiently display the game using a rich color set suitable for all
 * devices.
 *
 * @author kariem
 */
public class ColorMgmt {

   /** p1, p2, nc, nnc, nsc, ndc */
   private static final int[] COLORS_FULL =
   {
      0x00DD0000, 0x00FF0000, 0x0000DD00, 0x0000FF00, // p1, p2 
      0x00888888, 0, // neutral line
      0x00888888, 0, // neutral node
      0x00888888, 0x000000FF, // selected node
      0x00888888, 0x00777777, // deactivated node 
      0xA0AFFF // background
   };

   /** p1, p2, nc, nnc, nsc, ndc  - */
   private static final int[] COLORS_GREY16 =
   {
      0x00FFFFFF, 0x00111111, 0x00111111, 0x00222222, // p1, p2 
      0x00888888, 0, // neutral line
      0x00888888, 0, // neutral node
      0x00888888, 0x00AAAAAA, // selected node
      0x00888888, 0x00777777, // deactivated node 
      0xDDDDDD // background
   };

   /** p1, p2, nc, nnc, nsc, ndc  - */
   private static final int[] COLORS_GREY8 =
   {
      0x00FFFFFF, 0, 0x00111111, 0, 0x00888888, 0, 0x00888888, 0, 0x00AAAAAA, 0,
      0x00444444, 0x00888888, 0xDDDDDD
   };

   /** Inner and outer colors of player1's lines */
   public static int p1c1;
   /** Inner and outer colors of player1's lines */
   public static int p1c2;

   /** Inner and outer colors of player2's lines */
   public static int p2c1;
   /** Inner and outer colors of player2's lines */
   public static int p2c2;

   /** Inner and outer colors of neutral lines */
   public static int nc1;
   /** Inner and outer colors of neutral lines */
   public static int nc2;

   /** Inner and outer colors of a normal node */
   public static int nnc1;
   /** Inner and outer colors of a normal node */
   public static int nnc2;

   /** Inner and outer colors of a selected node */
   public static int nsc1;
   /** Inner and outer colors of a selected node */
   public static int nsc2;

   /** Inner and outer colors of a disabled node */
   public static int ndc1;
   /** Inner and outer colors of a disabled node */
   public static int ndc2;

   /** background */
   public static int bg;


   /**
    * Sets the color values according to properties of the given
    * <code>Display</code>
    *
    * @param d
    */
   public static void setDisplay(Display d) {
      if (d.isColor()) {
         if (d.numColors() >= 16) {
            setColors(COLORS_FULL);
            System.out.println("running with full colors");

            return;
         }
      } else {
         if (d.numColors() >= 16) {
            setColors(COLORS_GREY16);
            System.out.println("running with grey16");

            return;
         }
      }

      setColors(COLORS_GREY8);
      System.out.println("running with grey8");
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
