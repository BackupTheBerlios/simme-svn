package test.sim;

import javax.microedition.lcdui.Graphics;

/**
 * Has several methods that may be used to draw lines.
 * @author kariem
 */
public class DrawUtils {

   /**
    * Draws a line with appropriate thickness. End of lines are horizontal.
    * 
    * @param g Graphics context on which to draw.
    * @param xStart Starting x coordinate.
    * @param yStart Starting y coordinate.
    * @param xEnd End x coordinate.
    * @param yEnd End y coordinate.
    * @param maxX Maximum x coordinate.
    * @param maxY Maximum y coordinate.
    * @param thickness Thickness of the line. Should be greater than 1
    */
   public static void drawLine(Graphics g, int xStart, int yStart, int xEnd, int yEnd, int thickness) {

      if (thickness < 2) {
         return;
      }

      int x1, y1, x2, y2;

      // set x1 to lower, x2 to higher value
      if (xStart > xEnd) {
         x1 = xEnd;
         x2 = xStart;
      } else {
         x1 = xStart;
         x2 = xEnd;
      }

      // set y1 to lower, y2 to higher value
      if (yStart > yEnd) {
         y1 = yEnd;
         y2 = yStart;
      } else {
         y1 = yStart;
         y2 = yEnd;
      }

      if (drawAxisLine(g, x1, y1, x2, y2, thickness))
         return;

      // sonst ist es ein bisschen komplizierter
      boolean notSteep = y2 - y1 < x2 - x1;
      if (notSteep) {
         y1 = xStart;
         y2 = xEnd;
         xStart = yStart;
         xEnd = yEnd;
      } else {
         y1 = yStart;
         y2 = yEnd;
      }
      int add;
      for (int i = 0; i < thickness; i++) {
         // update line position
         add = i % 2 == 0 ? - ((i + 1) / 2) : (i + 1) / 2;
         x1 = xStart + add;
         x2 = xEnd + add;

         //System.out.println("g.drawLine(" + x1 + "," + y1 + "," + x2 + "," + y2 + ");");
         // draw single line
         if (notSteep) {
            g.drawLine(y1, x1, y2, x2);
         } else {
            g.drawLine(x1, y1, x2, y2);
         }
      }
   }

   public static void drawLineWithBorder(
      Graphics g,
      int xStart,
      int yStart,
      int xEnd,
      int yEnd,
      int thickness,
      int innerColor,
      int outerColor) {

      if (thickness < 2) {
         return;
      }

      int x1, y1, x2, y2;
      int r = thickness / 2;
      g.setColor(innerColor);

      // set x1 to lower, x2 to higher value
      if (xStart > xEnd) {
         x1 = xEnd;
         x2 = xStart;
      } else {
         x1 = xStart;
         x2 = xEnd;
      }

      // set y1 to lower, y2 to higher value
      if (yStart > yEnd) {
         y1 = yEnd;
         y2 = yStart;
      } else {
         y1 = yStart;
         y2 = yEnd;
      }

      if (drawAxisLine(g, x1, y1, x2, y2, thickness)) {
         int pos;
         if (x1 == x2) {
            g.setColor(outerColor);
            pos = x1 + r;
            g.drawLine(pos, y1, pos, y2);
            pos = x1 - r;
            g.drawLine(pos, y1, pos, y2);
         } else if (y1 == y1) {
            g.setColor(outerColor);
            pos = y1 + r;
            g.drawLine(x1, pos, x2, pos);
            pos = y1 - r;
            g.drawLine(x1, pos, x2, pos);
         }
         return;
      }

      // sonst ist es ein bisschen komplizierter
      boolean notSteep = y2 - y1 < x2 - x1;
      if (notSteep) {
         y1 = xStart;
         y2 = xEnd;
         xStart = yStart;
         xEnd = yEnd;
      } else {
         y1 = yStart;
         y2 = yEnd;
      }
      int add;
      for (int i = 0; i < thickness - 2; i++) {
         // update line position
         add = i % 2 == 0 ? - ((i + 1) / 2) : (i + 1) / 2;
         x1 = xStart + add;
         x2 = xEnd + add;

         //System.out.println("g.drawLine(" + x1 + "," + y1 + "," + x2 + "," + y2 + ");");
         // draw single line
         if (notSteep) {
            g.drawLine(y1, x1, y2, x2);
         } else {
            g.drawLine(x1, y1, x2, y2);
         }
      }
      g.setColor(outerColor);
      if (notSteep) {
         g.drawLine(y1, yStart + r, y2, yEnd + r);
         g.drawLine(y1, yStart - r, y2, yEnd - r);
      } else {
         g.drawLine(xStart + r, y1, xEnd + r, y2);
         g.drawLine(xStart - r, y1, xEnd - r, y2);
      }
   }

   public static void fillArcWithBorder(
      Graphics g,
      int x,
      int y,
      int width,
      int height,
      int startAngle,
      int arcAngle,
      int innerColor,
      int outerColor) {
      	
      	g.setColor(outerColor);
      	g.fillArc(x, y, width, height, startAngle, arcAngle);
			g.setColor(innerColor);
			g.fillArc(x + 2, y+2, width-4, height-4, startAngle, arcAngle);
   }
   

   /**
   	 * @param xStart
   	 * @param yStart
   	 * @param xEnd
   	 * @param yEnd
   	 */
   private static boolean drawAxisLine(Graphics g, int x1, int y1, int x2, int y2, int thickness) {

      int l;

      // wenn x Koordinaten, oder y Koordinaten gleich, dann einfach
      // Rechtecke zeichnen.
      if (x1 == x2) {
         l = y2 - y1;
         g.fillRect(x1 - (thickness / 2), y1, thickness - 1, l);
         return true;
      } else if (y1 == y2) {
         l = x2 - x1;
         g.fillRect(x1, y1 - (thickness / 2), l, thickness - 1);
         return true;
      }
      return false;
   }

   /* not needed currently
   private static int correct(int value, int min, int max) {
   	if (value < min) {
   		return min;
   	} else if (value > max) {
   		return max;
   	}
   	return value;
   }
   
   private static int dif(int a, int b) {
      return a > b ? a - b : b - a;
   }
   */
}
