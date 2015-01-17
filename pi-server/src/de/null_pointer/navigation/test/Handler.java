package de.null_pointer.navigation.test;

import de.null_pointer.navigation.map.Navigation;

public class Handler {

	private GuiNavigation gui = null;
	private Navigation navi = null;

	private int[][] values = null;

	private int currentX = -1;
	private int currentY = -1;
	private int heading = 0;

	public Handler(GuiNavigation gui, int sizeMapY, int sizeMapX) {
		this.gui = gui;
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
					gui.setColor(i, j, values[i][j]);
				} else {
					values[i][j] = 0;
				}
			}
		}
		this.gui.setColor(currentY, currentX, 1);
		gui.repaint();
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
		gui.setColor(row, col, value);
	}

	public void simulate() {
		if (values[currentY - 1][currentX] == -1) {
			navi.removeNeighbor(0);
		}
		if (values[currentY + 1][currentX] == -1) {
			navi.removeNeighbor(2);
		}
		if (values[currentY][currentX - 1] == -1) {
			navi.removeNeighbor(1);
		}
		if (values[currentY][currentX + 1] == -1) {
			navi.removeNeighbor(3);
		}

		heading = navi.tremauxAlgorithm(heading, false);
		navi.switchTile(heading);

		switch (heading) {
		case 0: {
			currentY -= 2;
			break;
		}
		case 1: {
			currentX -= 2;
			break;
		}
		case 2: {
			currentY += 2;
			break;
		}
		case 3: {
			currentX += 2;
		}
		}

		gui.setColor(currentY, currentX, 2);
	}
}
