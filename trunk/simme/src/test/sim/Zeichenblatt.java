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
	private static final Command CMD_CANCEL =
		new Command("Cancel", Command.CANCEL, 1);

	static final byte diameter = 22;
	static final byte linewidth = 5;
	int node[][] = new int[6][2];
	int xoff, yoff;

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
			//System.out.println(key);
			game.selectNode((byte) key);
		}
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
		addCommand(CMD_CANCEL);
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
				DrawUtils.drawLineWithBorder(
					g,
					node[i][0],
					node[i][1],
					node[j][0],
					node[j][1],
					linewidth,
					c1,
					c2);
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
				node[i][0] - diameter / 2,
				node[i][1] - diameter / 2,
				diameter,
				diameter,
				0,
				360,
				c1,
				c2);
		}

		if (standardDisplay() == true) {
			DrawUtils.drawLineWithBorder(
				g,
				node[4][0] - xoff,
				node[4][1] + 2 * getHeightSpace(g, node[4][1]) / 5,
				node[4][0],
				node[4][1] + 2 * getHeightSpace(g, node[4][1]) / 5,
				11,
				p1c1,
				p1c1);

			g.setColor(0);
			g.drawString(
				game.getP1Name(),
				node[4][0] + 2 * xoff / 3,
				node[4][1] + 9 / 2 + 2 * getHeightSpace(g, node[4][1]) / 5,
				node[4][0]);

			DrawUtils.drawStringWithBorder(
				g,
				game.getP1Info(),
				node[4][0] + 2 * xoff / 3 + 5 * game.getP1Name().length(),
				node[4][1] + 9 / 2 + 2 * getHeightSpace(g, node[4][1]) / 5,
				node[4][0]);

			DrawUtils.drawLineWithBorder(
				g,
				node[4][0] - xoff,
				node[4][1] + 4 * getHeightSpace(g, node[4][1]) / 5,
				node[4][0],
				node[4][1] + 4 * getHeightSpace(g, node[4][1]) / 5,
				11,
				p2c1,
				p2c1);

			g.setColor(0);
			g.drawString(
				game.getP2Name(),
				node[4][0] + 2 * xoff / 3,
				node[4][1] + 9 / 2 + 4 * getHeightSpace(g, node[4][1]) / 5,
				node[4][0]);

			DrawUtils.drawStringWithBorder(
				g,
				game.getP2Info(),
				node[4][0] + 2 * xoff / 3 + 5 * game.getP1Name().length(),
				node[4][1] + 9 / 2 + 4 * getHeightSpace(g, node[4][1]) / 5,
				node[4][0]);

		} else {
			DrawUtils.drawLineWithBorder(
				g,
				node[1][0] + xoff + getWidthSpace(g, node[1][0] + xoff) / 4,
				node[1][1],
				node[1][0] + xoff + getWidthSpace(g, node[1][0] + xoff) / 2,
				node[1][1],
				15,
				p1c1,
				p1c1);
		}
		if (game.getWinner() == 1) {
			g.drawString(
				"WINNER",
				node[4][0] - xoff / 2,
				node[4][1] + getHeightSpace(g, node[4][1]) / 2,
				node[4][0]);

			g.drawString(
				"LOSER",
				node[4][0] - xoff / 2,
				node[4][1] + 7 * getHeightSpace(g, node[4][1]) / 8,
				node[4][0]);

		} else if (game.getWinner() == 2) {
			g.drawString(
				"LOSER",
				node[4][0] - xoff / 2,
				node[4][1] + getHeightSpace(g, node[4][1]) / 2,
				node[4][0]);

			g.drawString(
				"WINNER",
				node[4][0] - xoff / 2,
				node[4][1] + 7 * getHeightSpace(g, node[4][1]) / 8,
				node[4][0]);

		}

		//System.out.println(game.getWinner());
	}

	public void FixNodePosition() {

		int width = getWidth(), height = getHeight();
		int margin = ((width + height) / 2) / 5;
		if (standardDisplay() == true) {
			xoff = (width - margin) / 3;
			yoff = (width - margin) / 3;

			margin /= 2;
			node[0][0] = xoff + margin;
			node[0][1] = 2 * margin;
			node[1][0] = 2 * xoff + margin;
			node[1][1] = 2 * margin;
			node[2][0] = 3 * xoff + margin;
			node[2][1] = 2 * margin + yoff;
			node[3][0] = 2 * xoff + margin;
			node[3][1] = 2 * margin + 2 * yoff;
			node[4][0] = xoff + margin;
			node[4][1] = 2 * margin + 2 * yoff;
			node[5][0] = margin;
			node[5][1] = 2 * margin + yoff;

		} else {
			xoff = (width - 4 * margin) / 3;
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
		}

	}

	private int getHeightSpace(Graphics g, int y) {
		int height = getHeight();
		return - (y - height);

	}

	private int getWidthSpace(Graphics g, int x) {
		int width = getWidth();
		return - (x - width);

	}

	private boolean standardDisplay() {
		int width = getWidth(), height = getHeight();
		if (width <= (4 * height) / 3) {
			return true;
		} else {
			return false;
		}

	}

	public void commandAction(Command c, Displayable d) {
		if (c == CMD_CANCEL) {
			Display.getDisplay(sim).setCurrent(sim.getMainScreen());
		}
	}

}
