package test.sim;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

class Zeichenblatt extends Canvas implements CommandListener {

  private static int diameter;
  private static byte linewidth;
  private static byte linewidththick;

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

  private String moveMessage;

  private Zeichenblatt zeichenblatt;

  int node[][] = new int[6][2];
  String nodeNumber[] = new String[6];

  private int xPInfo1, xPInfo2, xPInfo3, xPInfo4, xPInfo5, xPInfo6;
  private int yPInfo1, yPInfo1middle, yPInfo2, yPInfo2middle;

  private int width, height;

  private Font fntNodeLabel;
  private int heightFntNodeLabel;
  private Font fntPlayerInfo;
  private int heightFntPlayerInfo;

  Zeichenblatt() {
    ColorMgmt.setDisplay(Sim.getDisplay());

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

    width = getWidth();
    height = getHeight();
    System.out.println("pointer events :" + hasPointerEvents());
    setDisplayParameters();

  }

  /**
   * @see Canvas#keyPressed(int)
   */
  public void keyPressed(int keyCode) {
    if ((keyCode >= KEY_NUM1) && (keyCode <= KEY_NUM6) ||
        (keyCode == KEY_NUM0))
    {
      int key = keyCode - 49;

      //System.out.println(key);
      moveMessage = game.selectNode((byte) key);
    }
  }

  /**
   * @see Canvas#keyReleased(int)
   */
  public void keyReleased(int keyCode) {
    repaint();
  }

  /** @see Canvas#paint(Graphics) */
  protected void paint(Graphics g) {
    byte i = 0; // byte < int  (byte [127-128])
    byte j = 0; // byte < int  (byte [127-128])

    //fixNodePosition();
    g.setColor(bg);
    g.fillRect(0, 0, width, height);

    if (game.getWinner() != 0) {
      addCommand(CMD_NEWGAME);
    }

    // Kanten zeichnen
    int c1 = nc1, c2 = nc2;
     
    while (i < 6) {
      for (j = 0; j < 5; j++) {
        boolean dotted = false;
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
          dotted = true;
          break;
        }

        DrawUtils.drawLineWithBorder(g, node[i][0], node[i][1], node[j][0], node[j][1], linewidth, c1, c2,dotted);
      }

      i++;
    }

    g.setFont(fntNodeLabel);
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
        node[i][0] - (diameter / 2),
        node[i][1] - (diameter / 2),
        diameter,
        diameter,
        0,
        360,
        c1,
        c2);

      g.setColor(0);
      g.drawString(nodeNumber[i], node[i][0] - diameter / 6, node[i][1] - heightFntNodeLabel / 2, 0);
    }

    g.setFont(fntPlayerInfo);

    /*
    // Spielerbalken zeichnen
    g.setColor(p1c1);
    DrawUtils.drawLine(g, xPInfo1, yPInfo1middle, xPInfo2, yPInfo1middle, linewidththick);
    g.setColor(p2c1);
    DrawUtils.drawLine(g, xPInfo1, yPInfo2middle, xPInfo2, yPInfo2middle, linewidththick);

    // Spielernamen zeichnen
    g.setColor(0);
    g.drawString(game.getP1Name(), xPInfo3, yPInfo1 + linewidththick / 2, 0);
    g.drawString(game.getP2Name(), xPInfo3, yPInfo2 + linewidththick / 2, 0);

    // Spielerinfo zeichnen
    DrawUtils.drawStringWithBorder(g, game.getP1Info(), xPInfo5, yPInfo1 + linewidththick / 2, 0);
    DrawUtils.drawStringWithBorder(g, game.getP2Info(), xPInfo5, yPInfo2 + linewidththick / 2, 0);

    // derzeitigen Spieler kennzeichnen
    g.setColor(0);
    if (game.getPlayersTurn() == Game.PLAYER1) {
    g.fillArc(
    xPInfo1 - linewidththick,
    yPInfo1 + linewidththick / 2 + 1,
    linewidththick - 2,
    linewidththick - 2,
    0,
    360);
    } else {
    g.fillArc(
    xPInfo1 - linewidththick,
    yPInfo2 + linewidththick / 2 + 1,
    linewidththick - 2,
    linewidththick - 2,
    0,
    360);
    }

    // am Ende den jeweiligen Gewinner anzeigen
    if (game.isGameOver()) {
    drawWinner(g);
    } */

    if (moveMessage != null && moveMessage != "")
    {
      g.drawString(moveMessage, width / 3, height - height / 6, 0);
      return;
    }

    if (game.getWinner() == Game.PLAYER1)
    {
      g.drawString("Red wins", width / 3, height - height / 6, 0);
    } 
    else if (game.getWinner() == Game.PLAYER2)
    {
      g.drawString("Green wins", width / 3, height - height / 6, 0);
    }
    
  }

  /**
   * Draws "Winner" or "Loser" onto graphics context
   */

  /* private void drawWinner(Graphics g) {
     if (game.getWinner() == Game.PLAYER1) {
     g.drawString("WINNER", xPInfo1 + 2, yPInfo1 + linewidththick / 2, 0);
     g.drawString("LOSER", xPInfo1 + 2, yPInfo2 + linewidththick / 2, 0);
     } else if (game.getWinner() == Game.PLAYER2) {
     g.drawString("LOSER", xPInfo1 + 2, yPInfo1 + linewidththick / 2, 0);
     g.drawString("WINNER", xPInfo1 + 2, yPInfo2 + linewidththick / 2, 0);
     }
     } */

  /**
   * Looks at current width and height and sets drawing parameters accordingly.
   */

  private void setDisplayParameters() {
    // portrait configuration
    diameter = height / 10;
    diameter += diameter % 2;
    linewidth = new Integer(diameter / 4).byteValue();
    linewidth += linewidth % 3;
    linewidththick = new Integer(linewidth * 3).byteValue();

    int col1 = width / 6;
    int col2 = 2 * col1;
    int col3 = 2 * col2;
    int col4 = col3 + col1;

    int row2 = height / 2;
    int row1 = row2 - (height / 4 - diameter / 2);
    int row3 = row2 + row2 - row1;

    node[0] = new int[] { col2, row1 };
    node[1] = new int[] { col3, row1 };
    node[2] = new int[] { col4, row2 };
    node[3] = new int[] { col3, row3 };
    node[4] = new int[] { col2, row3 };
    node[5] = new int[] { col1, row2 };

    nodeNumber[0] = new String("1");
    nodeNumber[1] = new String("2");
    nodeNumber[2] = new String("3");
    nodeNumber[3] = new String("4");
    nodeNumber[4] = new String("5");
    nodeNumber[5] = new String("6");

    xPInfo1 = col1 - diameter / 2;
    xPInfo2 = col2 + diameter / 2;
    xPInfo3 = xPInfo2 + 5;
    xPInfo4 = col3;
    xPInfo5 = xPInfo4 + 5;
    xPInfo6 = col4;

    yPInfo1 = height - (height / 3) + 3;
    yPInfo2 = yPInfo1 + (height / 6) - 3;

    yPInfo1middle = yPInfo1 + (linewidththick);
    yPInfo2middle = yPInfo2 + (linewidththick);

    System.out.println("Diameter: " + diameter);

    int size = diameter < 16 ? Font.SIZE_SMALL : diameter < 22 ? Font.SIZE_MEDIUM : Font.SIZE_LARGE;

    fntNodeLabel = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, size);
    heightFntNodeLabel = fntNodeLabel.getHeight();
    fntPlayerInfo = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, size);
    heightFntPlayerInfo = fntPlayerInfo.getHeight();

  }

  /**
   * @see CommandListener#commandAction(Command, Displayable)
   */
  public void commandAction(Command c, Displayable d) {
    Display display = Sim.getDisplay();
    if (c == CMD_CANCEL) {
      display.setCurrent(Sim.getMainScreen());
    } else if (c == CMD_NEWGAME) {
      zeichenblatt = new Zeichenblatt();
      display.setCurrent(zeichenblatt);
    }
  }
}
