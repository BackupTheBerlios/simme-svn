package at.einspiel.simme.client;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Has several methods that may be used to draw lines.
 *
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
   * @param thickness Thickness of the line. Should be greater than 1
   */
  public static void drawLine(
    Graphics g,
    int xStart,
    int yStart,
    int xEnd,
    int yEnd,
    int thickness) {
    if (thickness < 2) {
      return;
    }

    int x1;
    int y1;
    int x2;
    int y2;

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
      return;
    }

    // sonst ist es ein bisschen komplizierter
    boolean notSteep = (y2 - y1) < (x2 - x1);

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
      add = ((i % 2) == 0) ? ((i + 1) / -2) : ((i + 1) / 2);
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

  /**
   * Draws a line with border.
   *
   * @param g Graphics context on which to draw.
   * @param xStart Starting x coordinate.
   * @param yStart Starting y coordinate.
   * @param xEnd End x coordinate.
   * @param yEnd End y coordinate.
   * @param thickness Thickness of the line. Should be greater than 2
   * @param innerColor Color for inner part of line.
   * @param outerColor Color for line border.
   * @param dotted whether to draw the line dotted.
   */
  public static void drawLineWithBorder(
    Graphics g,
    int xStart,
    int yStart,
    int xEnd,
    int yEnd,
    int thickness,
    int innerColor,
    int outerColor,
    boolean dotted) {
    if (thickness < 2) {
      return;
    }

    int x1;
    int y1;
    int x2;
    int y2;
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

    /*if (drawAxisLine(g, x1, y1, x2, y2, thickness)) {
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
    }*/

    // sonst ist es ein bisschen komplizierter
    boolean notSteep = (y2 - y1) < (x2 - x1);

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

    if (dotted) {
      g.setStrokeStyle(Graphics.DOTTED);
    }

    for (int i = 0; i < (thickness - 2); i++) {
      // update line position
      add = ((i % 2) == 0) ? ((i + 1) / -2) : ((i + 1) / 2);
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

    g.setStrokeStyle(Graphics.SOLID);
  }

  /**
   * Fills a circle and draws a border around the outside.
   * 
   * @param g Graphics context on which to draw.
   * @param x the <i>x</i> coordinate of the upper-left corner of the arc to
   *         be filled.
   * @param y the <i>y</i>  coordinate of the upper-left corner of the arc to
   *         be filled.
   * @param width the width of the arc to be filled.
   * @param height the height of the arc to be filled.
   * @param innerColor Color for inner part of line.
   * @param outerColor Color for line border.
   * 
   * @see #fillArcWithBorder(Graphics, int, int, int, int, int, int, int, int)
   */
  public static void fillCircleWithBorder(
    Graphics g,
    int x,
    int y,
    int width,
    int height,
    int innerColor,
    int outerColor) {
    fillArcWithBorder(g, x, y, width, height, 0, 360, innerColor, outerColor);
  }

  /**
   * Fills an arc and draws a border around the outside
   *
   * @param g Graphics context on which to draw.
   * @param x the <i>x</i> coordinate of the upper-left corner of the arc to
   *         be filled.
   * @param y the <i>y</i>  coordinate of the upper-left corner of the arc to
   *         be filled.
   * @param width the width of the arc to be filled.
   * @param height the height of the arc to be filled.
   * @param startAngle the beginning angle.
   * @param arcAngle the angular extent of the arc, relative to the start
   *         angle.
   * @param innerColor Color for inner part of line.
   * @param outerColor Color for line border.
   * @see Graphics#fillArc(int, int, int, int, int, int)
   */
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
    g.fillArc(x + 2, y + 2, width - 4, height - 4, startAngle, arcAngle);
  }

  /**
   * Draws a line along x or y axis.
   *
   * @param g Graphics context on which to draw.
   * @param x1 Starting x coordinate.
   * @param y1 Starting y coordinate.
   * @param x2 End x coordinate.
   * @param y2 End y coordinate.
   * @param thickness Thickness of the line.
   *
   * @return <code>true</code> if the line was drawn, <code>false</code>
   * otherwise.
   */
  private static boolean drawAxisLine(
    Graphics g,
    int x1,
    int y1,
    int x2,
    int y2,
    int thickness) {
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

  /**
   * Draws a String with border.
   *
   * @param g Graphics context on which to draw.
   * @param str The String to draw.
   * @param x x coordinate.
   * @param y y coordinate
   * @param anchor ...
   */
  public static void drawStringWithBorder(Graphics g, String str, int x, int y, int anchor) {
    g.drawString(str, x, y, anchor);

    Font f = g.getFont();
    g.drawRect(x - 2, y, f.stringWidth(str) + 4, f.getHeight());
  }
}
