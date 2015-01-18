package de.null_pointer.navigation.test;

import de.null_pointer.navigation.map.Navigation;

public class Handler {

	private GuiNavigation gui = null;
	private Navigation navi = null;

	private MyTimer timer = null;
	private FileHandler fileHandler = null;

	private int sizeMapX = -1;
	private int sizeMapY = -1;

	private int[][] values = null;

	private int currentX = -1;
	private int currentY = -1;
	private int heading = 0;

	public Handler(GuiNavigation gui, int sizeMapY, int sizeMapX) {
		this.gui = gui;
		timer = new MyTimer(this, 1000);
		fileHandler = new FileHandler(this);
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
		// TODO Zeitwert ggf. anpassen
		timer = new MyTimer(this, 50);
		fileHandler = new FileHandler(this);
		this.sizeMapX = sizeMapX;
		this.sizeMapY = sizeMapY;
		initValues();
	}

	private void initValues() {
		currentX = sizeMapX / 2;
		currentY = sizeMapY / 2;
		navi = new Navigation(currentX + 2, currentY + 2);
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
		if (gui != null) {
			for (int i = 0; i < sizeMapY; i++) {
				for (int j = 0; j < sizeMapX; j++) {
					gui.setColor(i, j, values[i][j]);
				}
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
		int[] tremauxCounter = navi.getTremauxCounter();
		System.out.println("tremaux a: " + tremauxCounter[0] + tremauxCounter[1]
				+ tremauxCounter[2] + tremauxCounter[3]);
		if (values[currentY - 1][currentX] == -1) {
			System.out.println("l0");
			navi.removeNeighbor(0);
		}
		if (values[currentY][currentX + 1] == -1) {
			System.out.println("l1");
			navi.removeNeighbor(1);
		}
		if (values[currentY + 1][currentX] == -1) {
			System.out.println("l2");
			navi.removeNeighbor(2);
		}
		if (values[currentY][currentX - 1] == -1) {
			System.out.println("l3");
			navi.removeNeighbor(3);
		}
		System.out.println("tremaux b: " + tremauxCounter[0] + tremauxCounter[1]
				+ tremauxCounter[2] + tremauxCounter[3]);
		
		heading = navi.tremauxAlgorithm(heading, false);
		navi.switchTile(heading);
		
		System.out.println("tremaux c: " + tremauxCounter[0] + tremauxCounter[1]
				+ tremauxCounter[2] + tremauxCounter[3]);
		
		if (gui != null) {
			gui.setColor(currentY, currentX, 2);
		}

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
			gui.setColor(currentY, currentX, 1);
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
}
