package test.sim;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

class Zeichenblatt extends Canvas implements CommandListener {

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

   private final Sim sim;
   private final static Command CMD_EXIT = new Command("Exit", Command.EXIT, 1);
   static final int diameter = 15;
   int node[][] = new int[6][2];
   int xoff, yoff;
   
   /*
   private boolean selected = false;
   private boolean side = false;

   private int moves = 0;
   private int key = 7;
   private int key1;
   private int key2;
   */

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
   }

   public void keyPressed(int keyCode) {
      if ((keyCode >= KEY_NUM1) && (keyCode <= KEY_NUM6)) {
         int key = keyCode - 49;
         System.out.println(key);
         game.selectNode((byte) key);
      }

      /*
      if (selected == false) {
      	if ((keyCode >= KEY_NUM1) && (keyCode <= KEY_NUM6)) {
      		//key = getGameAction(keyCode);
      		key = keyCode - 49;
      		key1 = keyCode - 49;
      		selected = true;
      		side = false;
      		System.out.println(key);
      		System.out.println(keyCode);
      		//System.out.println(getGameAction(keyCode));
      	} else {
      		key = 7;
      	}
      } else if (selected == true && keyCode >= KEY_NUM1 && (keyCode <= KEY_NUM6)) {
      	key2 = keyCode - 49;
      	if (key2 == key || key2 == -1) {
      		key = 7;
      		selected = false;
      	} else {
      
      		key = keyCode - 49;
      		moves++;
      		side = true;
      		selected = false;
      		System.out.println(moves);
      
      	}
      }*/
   }

   public void keyReleased(int keyCode) {
      repaint();
   }

   protected void paint(Graphics g) {
      int width = getWidth(), height = getHeight();
      byte i = 0, j = 0; // byte < int  (byte [127-128]) 

      FixNodePosition();
      g.setColor(bg);
      g.fillRect(0, 0, width, height);
      addCommand(CMD_EXIT);
      setCommandListener(this);

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
            DrawUtils.drawLineWithBorder(g, node[i][0], node[i][1], node[j][0], node[j][1], 5, c1, c2);
         }
         i++;
      }

      /*
      // Verbindungen zeichnen
      while (j < 6) {
      	for (i = 0; i < 5; i++)
      		DrawUtils.drawLineWithBorder(
      			g,
      			node[i][0],
      			node[i][1],
      			node[j][0],
      			node[j][1],
      			5,
      			nc1,
      			nc2);
      	j++;
      }
      
      //ausgewählte Kante einzeichnen
      if (moves % 2 == 0 && moves < 16 && side == true) {
      	DrawUtils.drawLineWithBorder(
      		g,
      		node[key1][0],
      		node[key1][1],
      		node[key2][0],
      		node[key2][1],
      		5,
      		p1c1,
      		p1c2);
      		repaint();
      
      } else if (moves % 2 == 1 && moves < 16 && side == true) {
      	DrawUtils.drawLineWithBorder(
      		g,
      		node[key1][0],
      		node[key1][1],
      		node[key2][0],
      		node[key2][1],
      		5,
      		p2c1,
      		p2c2);
      		repaint();
      }
      */

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
            node[i][0] - diameter / 2,
            node[i][1] - diameter / 2,
            diameter,
            diameter,
            0,
            360,
            c1,
            c2);
      }

      /*
      for (i = 0; i < 6; i++) {
      	DrawUtils.fillArcWithBorder(
      		g,
      		node[i][0] - diameter / 2,
      		node[i][1] - diameter / 2,
      		diameter,
      		diameter,
      		0,
      		360,
      		nnc1,
      		nnc2);
      }
      
      // selektierte Punkte zeichnen
      if (key < 6 && side == false) {
      	DrawUtils.fillArcWithBorder(
      		g,
      		node[key][0] - diameter / 2,
      		node[key][1] - diameter / 2,
      		diameter,
      		diameter,
      		0,
      		360,
      		nsc1,
      		nsc2);
      	repaint();
      } else if (key == 7) {
      	repaint();
      }
      */
   }

   public void FixNodePosition() {

      int width = getWidth(), height = getHeight();
      int margin = ((width + height) / 2) / 5;
      if (width <= (4 * height) / 3) {
         xoff = (width - margin) / 3;
         yoff = (height - margin) / 2;

         margin /= 2;
         node[0][0] = xoff + margin;
         node[0][1] = margin;
         node[1][0] = 2 * xoff + margin;
         node[1][1] = margin;
         node[2][0] = 3 * xoff + margin;
         node[2][1] = margin + yoff;
         node[3][0] = 2 * xoff + margin;
         node[3][1] = margin + 2 * yoff;
         node[4][0] = xoff + margin;
         node[4][1] = margin + 2 * yoff;
         node[5][0] = margin;
         node[5][1] = margin + yoff;
      } else {
         xoff = (width - 4 * margin) / 3;
         yoff = (height - margin) / 2;

         margin /= 2;
         node[0][0] = xoff + 4 * margin;
         node[0][1] = margin;
         node[1][0] = 2 * xoff + 4 * margin;
         node[1][1] = margin;
         node[2][0] = 3 * xoff + 4 * margin;
         node[2][1] = margin + yoff;
         node[3][0] = 2 * xoff + 4 * margin;
         node[3][1] = margin + 2 * yoff;
         node[4][0] = xoff + 4 * margin;
         node[4][1] = margin + 2 * yoff;
         node[5][0] = 4 * margin;
         node[5][1] = margin + yoff;
      }

   }

   public void commandAction(Command c, Displayable d) {
      // hier sollten eigentlich Befehle entgegengenommen werden.
      /*	switch (c.getCommandType()) {
      		case Command.EXIT :
               // einziger Befehl: aussteigen
      			sim.notifyDestroyed();
      			break;
      	} */
      if (c == CMD_EXIT) {
         sim.destroyApp(false);
         sim.notifyDestroyed();
      }
   }

}
