package at.einspiel.simme.client.ui;

import at.einspiel.simme.client.Game;
import at.einspiel.simme.client.GameRandomAI;
import at.einspiel.simme.client.Sim;
import at.einspiel.simme.client.util.DrawUtils;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

class Zeichenblatt extends Canvas implements CommandListener {

    private static final byte NB_NODES = Game.NB_NODES;

    private static int diameter;
    private static byte linewidth;
    private static final Command CMD_CANCEL = new Command("Cancel", Command.CANCEL, 1);
    private static final Command CMD_NEWGAME = new Command("New Game", Command.OK, 1);
    private static final Command CMD_NEWGAME2 = new Command("New Game", Command.OK, 1);

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
    int[][] node = new int[NB_NODES][2];

    static final String[] NODE_LABELS = { "1", "2", "3", "4", "5", "6" };

    private int width;
    private int height;
    private Font fntLabel;
    private int heightFntLabel;
    private Font fntPlayerInfo;

    private Graphics graphics;
    boolean single = false;

    /**
     * Creates a new screen to play.
     * 
     * @param singlePlayer <code>true</code> to play against the computer,
     *         <code>false</code> to play against a human opponent.
     */
    Zeichenblatt(boolean singlePlayer) {
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
        if (singlePlayer) {
           setGame(new GameRandomAI());
        } else {
           setGame(new Game());
            single = true;
        }

        // add cancel command
        addCommand(CMD_CANCEL);
        setCommandListener(this);

        width = getWidth();
        height = getHeight();
        System.out.println("pointer events :" + hasPointerEvents());
        setDisplayParameters(false);
    }

    /** Creates a new screen to play. Two human players. */
    Zeichenblatt() {
        this(false);
    }
    
    /**
     * Sets the game for this zeichenblatt.
     * @param g the game.
     */
    public void setGame(Game g) {
       this.game = g;
       game.start();
    }
    

    /**
     * @see Canvas#keyPressed(int)
     */
    public void keyPressed(int keyCode) {
        if (((keyCode >= KEY_NUM1) && (keyCode <= KEY_NUM6)) || (keyCode == KEY_NUM0)) {
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

    /** @see Canvas#paint(Graphics) */
    protected void paint(Graphics g) {
        byte i = 0; // byte < int  (byte [127-128])
        byte j = 0; // byte < int  (byte [127-128])

        this.graphics = g;
        //fixNodePosition();
        graphics.setColor(bg);
        graphics.fillRect(0, 0, width, height);

        if (game.getWinner() != 0) {
            if (!single) {
                addCommand(CMD_NEWGAME2);
            } else {
                addCommand(CMD_NEWGAME);
            }
        }

        int c1 = nc1;
        int c2 = nc2;

        // Kanten zeichnen
        for (i = 0; i < NB_NODES; i++) {
            for (j = 0; j < NB_NODES - 1; j++) {
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

                DrawUtils.drawLineWithBorder(
                    g,
                    node[i][0],
                    node[i][1],
                    node[j][0],
                    node[j][1],
                    linewidth,
                    c1,
                    c2,
                    dotted);
            }
        }

        // Punkte zeichnen
        for (i = 0; i < NB_NODES; i++) {
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

            // draw circles
            DrawUtils.fillCircleWithBorder(
                g,
                node[i][0] - (diameter / 2),
                node[i][1] - (diameter / 2),
                diameter,
                diameter,
                c1,
                c2);

            // draw labels
            graphics.setFont(fntLabel);
            graphics.setColor(0);
            graphics.drawString(
                NODE_LABELS[i],
                node[i][0],
                node[i][1] - (heightFntLabel / 2),
                Graphics.HCENTER | Graphics.TOP);
        }

        graphics.setFont(fntPlayerInfo);
        drawInfo(game.getMoveMessage());
        drawWinner(game.getWinner());
    }

    private void drawInfo(String s, boolean winMsg) {
        if ((s != null) && (s.length() != 0)) {
            if (winMsg) {
                graphics.drawString(
                    s,
                    width / 2,
                    height - (height / 6),
                    Graphics.HCENTER | Graphics.TOP);
            } else {
                graphics.drawString(
                    s,
                    width / 2,
                    height - (19 * height / 20),
                    Graphics.HCENTER | Graphics.TOP);
            }
        }
    }

    private void drawInfo(String s) {
        drawInfo(s, false);
    }

    private void drawWinner(byte winner) {
        if (winner == Game.PLAYER1) {
            drawInfo("Red wins", true);
        } else if (winner == Game.PLAYER2) {
            drawInfo("Green wins", true);
        }
    }

    /** Looks at current width and height and sets drawing parameters accordingly. */
    private void setDisplayParameters(boolean portrait) {
        diameter = height / 10;
        int size =
            (diameter < 16)
                ? Font.SIZE_SMALL
                : ((diameter < 22) ? Font.SIZE_MEDIUM : Font.SIZE_LARGE);

        fntLabel = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, size);
        heightFntLabel = fntLabel.getHeight();
        if (diameter < heightFntLabel) {
            diameter = heightFntLabel + 1;
        }
        diameter += (diameter % 2);
        fntPlayerInfo = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, size);

        linewidth = new Integer(diameter / 4).byteValue();
        linewidth += (linewidth % 3);

        if (portrait) {

            int row1 = height / 6;
            int row2 = 2 * row1;
            int row3 = 2 * row2;
            int row4 = row3 + row1;

            int col2 = width / 2;
            int col1 = col2 - ((height / 3) - (diameter / 2));
            int col3 = (col2 + col2) - col1;

            node[0] = new int[] { col1, row2 };
            node[1] = new int[] { col2, row1 };
            node[2] = new int[] { col3, row2 };
            node[3] = new int[] { col3, row3 };
            node[4] = new int[] { col2, row4 };
            node[5] = new int[] { col1, row3 };

        } else { // landscape
            int col1 = width / 6;
            int col2 = 2 * col1;
            int col3 = 2 * col2;
            int col4 = col3 + col1;

            int row2 = height / 2;
            int row1 = row2 - ((height / 3) - (diameter / 2));
            int row3 = (row2 + row2) - row1;

            node[0] = new int[] { col2, row1 };
            node[1] = new int[] { col3, row1 };
            node[2] = new int[] { col4, row2 };
            node[3] = new int[] { col3, row3 };
            node[4] = new int[] { col2, row3 };
            node[5] = new int[] { col1, row2 };
        }

        System.out.println("Diameter: " + diameter);

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
        } else if (c == CMD_NEWGAME2) {
            zeichenblatt = new Zeichenblatt(true);
            display.setCurrent(zeichenblatt);
        }
    }
}