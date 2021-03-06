package de.null_pointer.navigation.test;

import de.null_pointer.navigation.map.Navigation;

public class NavSimulationHandler {

	private GuiNavigation gui = null;
	private CoordinateDialog dialog = null;
	private Navigation navi = null;

	private MyTimer timer = null;
	private FileHandler fileHandler = null;

	private int sizeMapX = -1;
	private int sizeMapY = -1;

	/**
	 * -1 = wall 0 = clear Tile 1 = current position 2 = visited Tile
	 */
	private int[][] values = null;

	private int currentX = -1;
	private int currentY = -1;
	private int lastX = -1;
	private int lastY = -1;
	private int heading = 0;

	private boolean BlackTileRetreat = false;

	public NavSimulationHandler(int sizeMapY, int sizeMapX, boolean withGui) {
		if (withGui) {
			this.gui = new GuiNavigation(this);
		}
		fileHandler = new FileHandler(this);
		this.sizeMapX = sizeMapX;
		this.sizeMapY = sizeMapY;
		initValues();
	}

	/**
	 * @param sizeMapY
	 * @param sizeMapX
	 */
	public NavSimulationHandler(int sizeMapY, int sizeMapX) {
		this(sizeMapX, sizeMapX, false);
	}

	/**
	 * overwrites the values array and paints the map accordingly
	 * 
	 * @param values
	 */
	public void setValues(int[][] values) {
		this.values = values;
		if (gui != null) {
			for (int i = 0; i < sizeMapY; i++) {
				for (int j = 0; j < sizeMapX; j++) {
					gui.setColor(i, j, values[i][j]);
				}
			}
		}
	}

	/**
	 * 
	 * @return returns current state of the map
	 */
	public int[][] getValues() {
		return values;
	}

	/**
	 * used to to set the value of a specific tile to a value depending on the
	 * current phase of the program (maze creation or simulation) and on which
	 * button was pressed
	 * 
	 * @param row
	 *            row of the tile
	 * @param col
	 *            column of the tile
	 * @param phase
	 *            phase of the program. (0 = maze creation; 1 = simulation)
	 * @param button
	 *            pressed button (0 = left mouse button; 1 = right mouse button)
	 */
	public void setValue(int row, int col, int button) {
		int value = 0;
		if (button == 0) {
			if (values[row][col] == -1) {
				value = 0;
			} else {
				value = -1;
			}
		} else {
			if (values[row][col] == -2) {
				value = 0;
			} else {
				value = -2;
			}
		}

		values[row][col] = value;
		if (gui != null) {
			gui.setColor(row, col, value);
		}
	}

	/**
	 * initializes the Values Array, which holds information about the maze map
	 */
	private void initValues() {
		currentX = sizeMapX / 2;
		currentY = sizeMapY / 2;
		lastX = currentX;
		lastY = currentY;
		navi = new Navigation(currentX, currentY);
		values = new int[sizeMapY][sizeMapX];
		for (int y = 0; y < sizeMapY; y++) {
			for (int x = 0; x < sizeMapX; x++) {
				if (y % 2 == 0
						&& x % 2 == 0
						|| (y == 0 || y == sizeMapY - 1 || x == 0 || x == sizeMapX - 1)) {
					values[y][x] = -1;
					if (gui != null) {
						gui.setColor(y, x, -1);
					}
				} else {
					values[y][x] = 0;
					if (gui != null) {
						gui.setColor(y, x, 0);
					}
				}
			}
		}
		values[currentY][currentX] = 1;
		if (gui != null) {
			gui.setColor(currentY, currentX, 1);
			gui.repaint();
		}
	}

	/**
	 * resets the map to either the current simulated maze or to new initialized
	 * map
	 * 
	 * @param fullReset
	 *            true to initialize the map new
	 */
	public void reset(boolean fullReset) {
		if (fullReset) {
			heading = 0;
			initValues();
		} else {
			heading = 0;
			currentX = sizeMapX / 2;
			currentY = sizeMapY / 2;
			lastX = currentX;
			lastY = currentY;
			navi = new Navigation(currentY, currentX);

			for (int y = 0; y < sizeMapY; y++) {
				for (int x = 0; x < sizeMapX; x++) {
					if (y % 2 == 1 && x % 2 == 1 && values[y][x] != -2) {
						values[y][x] = 0;
						if (gui != null) {
							gui.setColor(y, x, 0);
						}
					}
				}
			}
			if (gui != null) {
				gui.setColor(currentY, currentX, 1);
				gui.repaint();
			}
		}
	}

	/**
	 * simulates the movement of the robot inside the maze; has to be called
	 * continuesly
	 * 
	 * @return returns false if simulation is either finished or an error
	 *         ocurred
	 */
	public boolean simulate() {

		if (values[currentY][currentX] == -2) {

			int invHeading = invertHeading(heading);

			switchTableTile(invHeading);
			navi.switchTile(invHeading);
			navi.disconnectTile(navi.getNeighbor(heading));

			BlackTileRetreat = true;
			return true;
		}

		int[] tremauxCounter = navi.getTremauxCounter();
		StringBuffer debugMessage = new StringBuffer("");

		debugMessage.append("tremaux a: " + tremauxCounter[0]
				+ tremauxCounter[1] + tremauxCounter[2] + tremauxCounter[3]
				+ "\n");

		if (values[lastY][lastX] < 2) {
			values[lastY][lastX] = 2;
			if (gui != null) {
				gui.setColor(lastY, lastX, 2);
			}
		} else if (values[lastY][lastX] == 2) {
			values[lastY][lastX] = 3;
			if (gui != null) {
				gui.setColor(lastY, lastX, 3);
			}
		} else {
			values[lastY][lastX] = 4;
			if (gui != null) {
				gui.setColor(lastY, lastX, 4);
			}
		}

		if (gui != null) {
			gui.setColor(currentY, currentX, 1);
		}

		if (values[currentY - 1][currentX] == -1) {
			debugMessage.append("l0 ");
			navi.removeNeighbor(0);
		}
		if (values[currentY][currentX + 1] == -1) {
			debugMessage.append("l1 ");
			navi.removeNeighbor(1);
		}
		if (values[currentY + 1][currentX] == -1) {
			debugMessage.append("l2 ");
			navi.removeNeighbor(2);
		}
		if (values[currentY][currentX - 1] == -1) {
			debugMessage.append("l3 ");
			navi.removeNeighbor(3);
		}
		debugMessage.append("\n");

		debugMessage.append("tremaux b: " + tremauxCounter[0]
				+ tremauxCounter[1] + tremauxCounter[2] + tremauxCounter[3]
				+ "\n");

		heading = navi.tremauxAlgorithm(heading, BlackTileRetreat);
		if (heading < 0) {
			return false;
		}

		debugMessage.append("tremaux c: " + tremauxCounter[0]
				+ tremauxCounter[1] + tremauxCounter[2] + tremauxCounter[3]
				+ "\n");

		debugMessage.append("x: " + currentX + " y: " + currentY
				+ "\nheading: " + heading);
		if (dialog != null) {
			dialog.updateOrientationAreas(navi.getCurrentTile());
			dialog.setTextDebugArea(debugMessage.toString());
		}

		lastX = currentX;
		lastY = currentY;

		navi.switchTile(heading);
		switchTableTile(heading);

		if (BlackTileRetreat) {
			BlackTileRetreat = false;
		}

		return true;
	}

	private int invertHeading(int heading) {
		int calculated_orientation = 0;
		if (heading <= 1) {
			calculated_orientation = heading + 2;
		} else if (heading >= 2) {
			calculated_orientation = heading - 2;
		}
		return calculated_orientation;
	}

	private void switchTableTile(int heading) {
		switch (heading) {
		case 0: {
			currentY -= 2;
			break;
		}
		case 1: {
			currentX += 2;
			break;
		}
		case 2: {
			currentY += 2;
			break;
		}
		case 3: {
			currentX -= 2;
		}
		}
	}

	public void startTimer() {
		timer.start();
	}

	public void stopTimer() {
		timer.stop();
	}

	public void save() {
		fileHandler.saveFile(gui);
	}

	public void load() {
		fileHandler.loadFile(gui);
	}

	public FileHandler getFileHandler() {
		return fileHandler;
	}

	/**
	 * initializes the debugging dialog
	 */
	public void initDialog() {
		int x = gui.getX() + gui.getWidth();
		int y = gui.getY();
		dialog = new CoordinateDialog();
		dialog.setLocation(x, y);
		dialog.setVisible(true);
	}

	/**
	 * disposes the current debugging dialog
	 */
	public void killDialog() {
		dialog.dispose();
		dialog = null;
	}

	public void newTimer(int time) {
		timer = new MyTimer(this, time);
	}
}
