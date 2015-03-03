package de.null_pointer.navigation.test;

import de.null_pointer.navigation.map.Navigation;

public class NavCompetitionHandler {

	private GuiNavigation gui = null;
	private CoordinateDialog dialog = null;

	private int currentX = 9;
	private int currentY = 9;

	public NavCompetitionHandler() {
		this.gui = new GuiNavigation();
	}

	public void switchTile(int direction) {
		gui.setColor(currentY, currentX, 0);
		switch (direction) {
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
		gui.setColor(currentY, currentX, 1);
	}

	public void removeNeighbor(int direction) {
		int bufferX = currentX;
		int bufferY = currentY;
		switch (direction) {
		case 0: {
			bufferY -= 1;
			break;
		}
		case 1: {
			bufferX += 1;
			break;
		}
		case 2: {
			bufferY += 1;
			break;
		}
		case 3: {
			bufferX -= 12;
		}
		}
		gui.setColor(bufferY, bufferX, -1);
	}

}