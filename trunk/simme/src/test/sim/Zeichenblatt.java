package test.sim;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

class Zeichenblatt extends Canvas implements CommandListener {

   private static final byte DIAMETER = 22;
   private static final byte LINEWIDTH = 5;
   private static final Command CMD_CANCEL = new Command("Cancel", Command.CANCEL, 1);
   private static final Command CMD_NEWGAME = new Command("New Game", Command.OK, 1);

   /** Inner and outer colors of player1's lines */
   private static int p1c1, p1c2;
   /** Inner and outer colors of player2's lines */
   private static int p2c1, p2c2;
   /** Inner and outer colors of neutral lines */
   private static int nc1, nc2;

   /** Inner and outer colors of a normal node */
   private static int nnc1, nnc2;
   /** Inner and outer colors of a selected node */
   private static int nsc1, nsc2;
   /** Inner and outer colors of a disabled node */
   private static int ndc1, ndc2;

   /** background */
   private static int bg;

   /** game */
   private Game game;

   private Zeichenblatt zeichenblatt;
   private final Sim sim;

   int node[][] = new int[6][2];
   int xoff, yoff;
   byte position[] = new byte[15];

   Zeichenblatt(Sim midlet) {
      this.sim = midlet;

      // set colors
      Display d = Display.getDisplay(sim);
      ColorMgmt.setDisplay(d);

      p1c1 = ColorMgmt.p1c1;
      p1c2 = ColorMgmt.p1c2;
      p2c1 = ColorMgmt.p2c1;
      p2c2 = ColorMgmt.p2c2;
      nc1 = ColorMgmt.nc1;
      nc2 = ColorMgmt.nc2;
      nnc1 = ColorMgmt.nnc1;
      nnc2 = ColorMgmt.nnc2;
      nsc1 = ColorMgmt.nsc1;
      nsc2 = ColorMgmt.nsc2;
      ndc1 = ColorMgmt.ndc1;
      ndc2 = ColorMgmt.ndc2;
      bg = ColorMgmt.bg;

      // start a new Game()
      game = new Game();
      
      // add cancel command
      addCommand(CMD_CANCEL);
      setCommandListener(this);
   }

   /**
    * @see Canvas#keyPressed(int)
    */
   public void keyPressed(int keyCode) {
      if ((keyCode >= KEY_NUM1) && (keyCode <= KEY_NUM6)) {
         int key = keyCode - 49;

         //System.out.println(key);
         game.selectNode((byte) key);
      }
   }

   /**
    * @see Canvas#keyReleased(int)
    */
   public void keyReleased(int keyCode) {
      repaint();
   }

   protected void paint(Graphics g) {
      int width = getWidth();
      int height = getHeight();
      byte i = 0; // byte < int  (byte [127-128]) 
      byte j = 0; // byte < int  (byte [127-128]) 

      fixNodePosition();
      g.setColor(bg);
      g.fillRect(0, 0, width, height);

      if (game.getWinner() != 0) {
         addCommand(CMD_NEWGAME);
      }


      // Kanten zeichnen
      int c1 = nc1, c2 = nc2;

      while (i < 6) {
         for (j = 0; j < 5; j++) {
            switch (game.getEdgeOwner(i, j)) {
               case Game.NEUTRAL :
                  c1 = nc1;
                  c2 = nc2;
                  break;

               case Game.PLAYER1 :
                  c1 = p1c1;
                  c2 = p1c2;
                  break;

               case Game.PLAYER2 :
                  c1 = p2c1;
                  c2 = p2c2;
                  break;
            }

            DrawUtils.drawLineWithBorder(g, node[i][0], node[i][1], node[j][0], node[j][1], LINEWIDTH, c1, c2);
         }

         i++;
      }

      // Punkte zeichnen
      for (i = 0; i < 6; i++) {
         if (game.isDisabled(i)) {
            c1 = ndc1;
            c2 = ndc2;
         } else if (game.isActivated(i)) {
            c1 = nsc1;
            c2 = nsc2;
         } else {
            c1 = nnc1;
            c2 = nnc2;
         }

         DrawUtils.fillArcWithBorder(
            g,
            node[i][0] - (DIAMETER / 2),
            node[i][1] - (DIAMETER / 2),
            DIAMETER,
            DIAMETER,
            0,
            360,
            c1,
            c2);
      }


      // Spielerbeschreibung zeichnen
      if (standardDisplay() == true) {
         DrawUtils.drawLineWithBorder(
            g,
            node[4][0] - xoff,
            node[4][1] + ((2 * getHeightSpace(g, node[4][1])) / 5),
            node[4][0],
            node[4][1] + ((2 * getHeightSpace(g, node[4][1])) / 5),
            11,
            p1c1,
            p1c1);

         g.setColor(0);
         g.drawString(
            game.getP1Name(),
            node[4][0] + ((2 * xoff) / 3),
            node[4][1] + (9 / 2) + ((2 * getHeightSpace(g, node[4][1])) / 5),
            node[4][0]);

         DrawUtils.drawStringWithBorder(
            g,
            game.getP1Info(),
            node[4][0] + ((2 * xoff) / 3) + (5 * game.getP1Name().length()),
            node[4][1] + (9 / 2) + ((2 * getHeightSpace(g, node[4][1])) / 5),
            node[4][0]);

         DrawUtils.drawLineWithBorder(
            g,
            node[4][0] - xoff,
            node[4][1] + ((4 * getHeightSpace(g, node[4][1])) / 5),
            node[4][0],
            node[4][1] + ((4 * getHeightSpace(g, node[4][1])) / 5),
            11,
            p2c1,
            p2c1);

         g.setColor(0);
         g.drawString(
            game.getP2Name(),
            node[4][0] + ((2 * xoff) / 3),
            node[4][1] + (9 / 2) + ((4 * getHeightSpace(g, node[4][1])) / 5),
            node[4][0]);

         DrawUtils.drawStringWithBorder(
            g,
            game.getP2Info(),
            node[4][0] + ((2 * xoff) / 3) + (5 * game.getP1Name().length()),
            node[4][1] + (9 / 2) + ((4 * getHeightSpace(g, node[4][1])) / 5),
            node[4][0]);

         if (game.getPlayersTurn() == 1) {
            g.setColor(0);
            g.fillArc(node[4][0] - 5 * xoff / 4, node[4][1] + 2 * getHeightSpace(g, node[4][1]) / 5 - 4, 8, 8, 0, 360);
         } else {
            g.setColor(0);
            g.fillArc(node[4][0] - 5 * xoff / 4, node[4][1] + 4 * getHeightSpace(g, node[4][1]) / 5 - 4, 8, 8, 0, 360);
         }

         if (game.isGameOver()) {
            drawWinner(g);
         }

      } else {
         DrawUtils.drawLineWithBorder(
            g,
            node[1][0] + xoff + (getWidthSpace(g, node[1][0] + xoff) / 4),
            node[1][1] + (yoff / 2),
            node[1][0] + xoff + (getWidthSpace(g, node[1][0] + xoff) / 2),
            node[1][1] + (yoff / 2),
            15,
            p1c1,
            p1c1);

         DrawUtils.drawLineWithBorder(
            g,
            node[1][0] + xoff + (getWidthSpace(g, node[1][0] + xoff) / 4),
            node[4][1] - (yoff / 2),
            node[1][0] + xoff + (getWidthSpace(g, node[1][0] + xoff) / 2),
            node[4][1] - (yoff / 2),
            15,
            p2c1,
            p2c1);
      }

      //System.out.println(game.getWinner());
   }

   /**
    * Draws "Winner" or "Loser" onto graphics context
    */
   private void drawWinner(Graphics g) {
      if (game.getWinner() == Game.PLAYER1) {
         g.drawString("WINNER", node[4][0] - (xoff / 2), node[4][1] + (getHeightSpace(g, node[4][1]) / 2), node[4][0]);

         g.drawString(
            "LOSER",
            node[4][0] - (xoff / 2),
            node[4][1] + ((7 * getHeightSpace(g, node[4][1])) / 8),
            node[4][0]);
      } else if (game.getWinner() == Game.PLAYER2) {
         g.drawString("LOSER", node[4][0] - (xoff / 2), node[4][1] + (getHeightSpace(g, node[4][1]) / 2), node[4][0]);

         g.drawString(
            "WINNER",
            node[4][0] - (xoff / 2),
            node[4][1] + ((7 * getHeightSpace(g, node[4][1])) / 8),
            node[4][0]);
      }
   }

   private void fixNodePosition() {
      int width = getWidth();
      int height = getHeight();
      int margin = ((width + height) / 2) / 5;

      if (standardDisplay() == true) {
         xoff = (width - margin) / 3;
         yoff = (width - margin) / 3;

         margin /= 2;
         node[0][0] = xoff + margin;
         node[0][1] = 2 * margin;
         node[1][0] = (2 * xoff) + margin;
         node[1][1] = 2 * margin;
         node[2][0] = (3 * xoff) + margin;
         node[2][1] = (2 * margin) + yoff;
         node[3][0] = (2 * xoff) + margin;
         node[3][1] = (2 * margin) + (2 * yoff);
         node[4][0] = xoff + margin;
         node[4][1] = (2 * margin) + (2 * yoff);
         node[5][0] = margin;
         node[5][1] = (2 * margin) + yoff;
      } else {
         xoff = (width - (4 * margin)) / 3;
         yoff = (height - margin) / 2;

         margin /= 2;
         node[0][0] = xoff + margin;
         node[0][1] = margin;
         node[1][0] = (2 * xoff) + margin;
         node[1][1] = margin;
         node[2][0] = (3 * xoff) + margin;
         node[2][1] = margin + yoff;
         node[3][0] = (2 * xoff) + margin;
         node[3][1] = margin + (2 * yoff);
         node[4][0] = xoff + margin;
         node[4][1] = margin + (2 * yoff);
         node[5][0] = margin;
         node[5][1] = margin + yoff;
      }
   }

   /*
   public void shaking() {
      int n1, n2;
      double xx1, yy1, xx2, yy2, dx, dy;
      byte weight[] = new byte[6];
      weight = game.weigh(position);
      do {
         n1 = (int) (Math.random() * 6);
         n2 = (int) (Math.random() * 6);
      } while (n1 == n2 || weight[n1] == weight[n2]);
      int x1 = node[n1][0];
      int y1 = node[n1][1];
      int x2 = node[n2][0];
      int y2 = node[n2][1];
      xx1 = x1;
      yy1 = y1;
      xx2 = x2;
      yy2 = y2;
      dx = (x2 - x1) / 20.0;
      dy = (y2 - y1) / 20.0;
      for (int i = 1; i < 20; i++) {
         long startTime = System.currentTimeMillis();
         xx1 += dx;
         yy1 += dy;
         xx2 -= dx;
         yy2 -= dy;
         node[n1][0] = (int) xx1;
         node[n1][1] = (int) yy1;
         node[n2][0] = (int) xx2;
         node[n2][1] = (int) yy2;
         repaint(); //Delay depending on how far we are behind
         try {
            startTime += 10 * Math.abs(10 - i);
            Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
         } catch (InterruptedException e) {
            break;
         }
      }
      node[n1][0] = x2;
      node[n1][1] = y2;
      node[n2][0] = x1;
      node[n2][1] = y1;
      repaint();
   }
   */

   private int getHeightSpace(Graphics g, int y) {
      int height = getHeight();

      return height - y;
   }

   private int getWidthSpace(Graphics g, int x) {
      int width = getWidth();

      return width - x;
   }

   private boolean standardDisplay() {
      int width = getWidth();
      int height = getHeight();

      if (width <= ((4 * height) / 3)) {
         return true;
      } else {
         return false;
      }
   }

   /**
    * @see CommandListener#commandAction(Command, Displayable)
    */
   public void commandAction(Command c, Displayable d) {
      if (c == CMD_CANCEL) {
         Display.getDisplay(sim).setCurrent(sim.getMainScreen());
      } else if (c == CMD_NEWGAME) {
         zeichenblatt = new Zeichenblatt(sim);
         Display.getDisplay(sim).setCurrent(zeichenblatt);
      }
   }
}
