package de.null_pointer.navigation.test;

import de.null_pointer.navigation.map.Navigation;

public class Handler {

	private GuiNavigation gui = null;
	private Navigation navi = null;

	private int sizeMapX = -1;
	private int sizeMapY = -1;

	private int[][] values = null;

	private int currentX = -1;
	private int currentY = -1;
	private int heading = 0;

	public Handler(GuiNavigation gui, int sizeMapY, int sizeMapX) {
		this.gui = gui;
		this.sizeMapX = sizeMapX;
		this.sizeMapY = sizeMapY;
		initValues();
	}

	/**
	 * Konstruktor fuer Testzwecke
	 * 
	 * @param sizeMapY
	 * @param sizeMapX
	 */
	public Handler(int sizeMapY, int sizeMapX) {
		this.sizeMapX = sizeMapX;
		this.sizeMapY = sizeMapY;
		initValues();
	}

	private void initValues() {
		currentX = sizeMapX / 2;
		currentY = sizeMapY / 2;
		navi = new Navigation(currentX, currentY);
		values = new int[sizeMapY][sizeMapX];
		for (int i = 0; i < sizeMapY; i++) {
			for (int j = 0; j < sizeMapX; j++) {
				if (i % 2 == 0
						&& j % 2 == 0
						|| (i == 0 || i == sizeMapY - 1 || j == 0 || j == sizeMapX - 1)) {
					values[i][j] = -1;
					if (gui != null) {
						gui.setColor(i, j, -1);
					}
				} else {
					values[i][j] = 0;
					if (gui != null) {
						gui.setColor(i, j, 0);
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

	public void setValues(int[][] values) {
		this.values = values;

		for (int i = 0; i < sizeMapY; i++) {
			for (int j = 0; j < sizeMapX; j++) {
				gui.setColor(i, j, values[i][j]);
			}
		}
	}

	public int[][] getValues() {
		return values;
	}

	public void setValue(int row, int col, int phase, int button) {
		int value = 0;
		if (phase == 0) {
			if (button == 0) {
				if (values[row][col] == -1) {
					value = 0;
				} else {
					value = -1;
				}
			} else {
				if (values[row][col] == 1) {
					value = 0;
				} else {
					value = 1;
				}
			}
		} else {
			value = 2;
		}

		values[row][col] = value;
		if (gui != null) {
			gui.setColor(row, col, value);
		}
	}

	public void simulate() {
		if (values[currentY - 1][currentX] == -1) {
			navi.removeNeighbor(0);
		}
		if (values[currentY + 1][currentX] == -1) {
			navi.removeNeighbor(2);
		}
		if (values[currentY][currentX - 1] == -1) {
			navi.removeNeighbor(3);
		}
		if (values[currentY][currentX + 1] == -1) {
			navi.removeNeighbor(1);
		}

		heading = navi.tremauxAlgorithm(heading, false);
		System.out.println("sim: " + heading);
		navi.switchTile(heading);

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

		values[currentY][currentX] = 2;
		if (gui != null) {
			gui.setColor(currentY, currentX, 2);
		}
	}

	public void reset(boolean fullReset) {
		if (fullReset) {
			heading = 0;
			initValues();
		} else {
			heading = 0;
			currentX = sizeMapX / 2;
			currentY = sizeMapY / 2;
			navi = new Navigation(currentX, currentY);

			for (int i = 0; i < sizeMapY; i++) {
				for (int j = 0; j < sizeMapX; j++) {
					if (values[i][j] == 1 || values[i][j] == 2) {
						values[i][j] = 0;
						if (gui != null) {
							gui.setColor(i, j, 0);
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
}
