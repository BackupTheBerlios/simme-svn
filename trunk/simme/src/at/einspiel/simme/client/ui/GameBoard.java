//----------------------------------------------------------------------------
//[Simme]
//    Java Source File: GameBoard.java
//               $Date: 2004/09/22 18:25:42 $
//           $Revision: 1.2 $
//----------------------------------------------------------------------------
package at.einspiel.simme.client.ui;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.*;

import at.einspiel.logging.Logger;
import at.einspiel.midp.ui.*;
import at.einspiel.simme.client.*;
import at.einspiel.simme.client.util.DrawUtils;

/**
 * The visual component for the interaction with the game.
 * 
 * @author kariem
 */
class GameBoard extends Canvas implements IDynamicUI {

	private static final byte NB_NODES = Game.NB_NODES;
	private static final Command CMD_NEWGAME = new Command("Neu", Command.OK, 1);
	static final String[] NODE_LABELS = {"1", "2", "3", "4", "5", "6"};

	private static int diameter;
	private static byte linewidth;

	private final ICommandManager cmdMgr;
	private Vector endGameActions;

	/** game */
	private Game game;
	int[][] node = new int[NB_NODES][2];

	private final int width;
	private final int height;
	private Font fntLabel;
	private int heightFntLabel;
	private Font fntPlayerInfo;

	private Graphics graphics;
	boolean single = false;

	/**
	 * Creates a new screen to play.
	 * 
	 * @param g
	 *            the game to be displayed on this game board.
	 */
	GameBoard(Game g) {
		ColorMgmt.setDisplay(Sim.getDisplay());
		cmdMgr = new SimpleCommandManager(this);

		setGame(g);

		// always start randomly
		game.startRandom();

		width = getWidth();
		height = getHeight();
		Logger.debug(getClass(), "pointer events: " + hasPointerEvents());
		setDisplayParameters(false);
	}

	/**
	 * Sets the game for this zeichenblatt.
	 * 
	 * @param g
	 *            the game.
	 */
	public void setGame(Game g) {
		this.game = g;
		g.setDynamicUI(this);
	}

	/**
	 * Returns the game.
	 * @return the game.
	 */
	public Game getGame() {
		return game;
	}

	/** @see Canvas#keyPressed(int) */
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
		byte i = 0; // byte < int (byte [127-128])
		byte j = 0; // byte < int (byte [127-128])

		this.graphics = g;
		//fixNodePosition();
		graphics.setColor(ColorMgmt.bg);
		graphics.fillRect(0, 0, width, height);

		if (game.getWinner() != 0) {
			showEndGameActions();
		}

		int c1 = ColorMgmt.nc1;
		int c2 = ColorMgmt.nc2;

		// draw edges
		for (i = 0; i < NB_NODES; i++) {
			for (j = 0; j < NB_NODES - 1; j++) {

				switch (game.getEdgeOwner(i, j)) {
					case Game.NEUTRAL :
						c1 = ColorMgmt.nc1;
						c2 = ColorMgmt.nc2;
						break;

					case Game.PLAYER1 :
						c1 = ColorMgmt.p1c1;
						c2 = ColorMgmt.p1c2;
						break;

					case Game.PLAYER2 :
						c1 = ColorMgmt.p2c1;
						c2 = ColorMgmt.p2c2;
						break;
				}

				DrawUtils.drawLineWithBorder(g, node[i][0], node[i][1], node[j][0], node[j][1],
						linewidth, c1, c2, false);
			}
		}

		// draw corners
		for (i = 0; i < NB_NODES; i++) {
			if (game.isDisabled(i)) {
				c1 = ColorMgmt.ndc1;
				c2 = ColorMgmt.ndc2;
			} else if (game.isActivated(i)) {
				c1 = ColorMgmt.nsc1;
				c2 = ColorMgmt.nsc2;
			} else {
				c1 = ColorMgmt.nnc1;
				c2 = ColorMgmt.nnc2;
			}

			// draw circles
			DrawUtils.fillCircleWithBorder(g, node[i][0] - (diameter / 2), node[i][1]
					- (diameter / 2), diameter, diameter, c1, c2);

			// draw labels
			graphics.setFont(fntLabel);
			graphics.setColor(0);
			graphics.drawString(NODE_LABELS[i], node[i][0], node[i][1] - (heightFntLabel / 2),
					Graphics.HCENTER | Graphics.TOP);
		}

		graphics.setFont(fntPlayerInfo);
		drawInfo(game.getMoveMessage());
		drawWinner(game.getWinner());
	}

	/**
	 * Adds an action that will be visible to the user at the end of the game.
	 * 
	 * @param acEndGame the action command to be added.
	 */
	public void addEndGameAction(ActionCommand acEndGame) {
		if (endGameActions == null) {
			endGameActions = new Vector(1);
		}
		endGameActions.addElement(acEndGame);
	}

	/**
	 * Shows the end game actions.
	 */
	private void showEndGameActions() {
		if (game instanceof GameRandomAI) {
			addNewGameAction(new GameRandomAI());
		} else if (game instanceof GameUndoable) {
			addNewGameAction(new GameUndoable());
		}
		// see if there are actions for the end of the game
		if (endGameActions != null && !endGameActions.isEmpty()) {
			Enumeration enum = endGameActions.elements();
			while (enum.hasMoreElements()) {
				ActionCommand ac = (ActionCommand) enum.nextElement();
				cmdMgr.addActionCommand(ac);
			}
		}
	}

	/**
	 * Adds an action for the new game command.
	 * @param g
	 *            the game to be started.
	 */
	private void addNewGameAction(final Game g) {
		Action a = new Action() {
			/** @see Action#execute(Displayable) */
			public void execute(Displayable d) {
				GameBoard board = new GameBoard(g);
				Sim.setDisplay(board);
				g.startRandom();
			}
		};
		cmdMgr.addCommand(CMD_NEWGAME, a);
	}

	private void drawInfo(String s, boolean winMsg) {
		if ((s != null) && (s.length() != 0)) {
			if (winMsg) {
				graphics.drawString(s, width / 2, height - (height / 6), Graphics.HCENTER
						| Graphics.TOP);
			} else {
				graphics.drawString(s, width / 2, height - (19 * height / 20), Graphics.HCENTER
						| Graphics.TOP);
			}
		}
	}

	private void drawInfo(String s) {
		drawInfo(s, false);
	}

	private void drawWinner(byte winner) {
		if (winner == Game.NEUTRAL) {
			// no winner yet
			return;
		}

		drawInfo(game.getPlayerName(winner) + " hat gewonnen", true);
	}

	/**
	 * Looks at current width and height and sets drawing parameters
	 * accordingly.
	 * 
	 * @param portrait
	 *            whether to use portrait. If set to <code>false</code>,
	 *            landscape layout is used.
	 */
	private void setDisplayParameters(boolean portrait) {
		diameter = height / 10;
		int size = (diameter < 16) ? Font.SIZE_SMALL : ((diameter < 22)
				? Font.SIZE_MEDIUM
				: Font.SIZE_LARGE);

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

			node[0] = new int[]{col1, row2};
			node[1] = new int[]{col2, row1};
			node[2] = new int[]{col3, row2};
			node[3] = new int[]{col3, row3};
			node[4] = new int[]{col2, row4};
			node[5] = new int[]{col1, row3};

		} else { // landscape
			int col1 = width / 6;
			int col2 = 2 * col1;
			int col3 = 2 * col2;
			int col4 = col3 + col1;

			int row2 = height / 2;
			int row1 = row2 - ((height / 3) - (diameter / 2));
			int row3 = (row2 + row2) - row1;

			node[0] = new int[]{col2, row1};
			node[1] = new int[]{col3, row1};
			node[2] = new int[]{col4, row2};
			node[3] = new int[]{col3, row3};
			node[4] = new int[]{col2, row3};
			node[5] = new int[]{col1, row2};
		}

		Logger.debug(getClass(), "Diameter: " + diameter);

	}

	/**
	 * Returns the command manager.
	 * @return Returns the command manager.
	 */
	public ICommandManager getCommandManager() {
		return cmdMgr;
	}

	/**
	 * This implementation directly calls {@link Canvas#repaint()}.
	 * 
	 * @see at.einspiel.midp.ui.IDynamicUI#updateDisplay()
	 */
	public void updateDisplay() {
		repaint();
	}
}