package de.null_pointer.navigation.map;

import java.util.Properties;

import org.apache.log4j.Logger;

public class Navigation {

	private static Logger logger = Logger.getLogger(Navigation.class);

	private Node currentTile;

	public Navigation(Properties propPiServer) {
		int dimensionX = Integer.parseInt(propPiServer
				.getProperty("Navigation.Navigation.mapWidth"));
		int dimensionY = Integer.parseInt(propPiServer
				.getProperty("Navigation.Navigation.mapHeight"));
	}

	// Konstruktor fuer Testzwecke
	public Navigation(int dimensionX, int dimensionY) {
		currentTile = initializeMap(dimensionX, dimensionY, 0, 0, 0);
		currentTile.setVisited();
	}

	/**
	 * evaluates the direction the robot should take based on the tremaux
	 * maze-solving algorithm
	 * 
	 * @param orientation
	 *            Current Orientation of the robot
	 * @param blackTileRetreat
	 *            true if robot recently retreated from a black tile
	 * @return returns the direction the robot should take; -1 if something went
	 *         wrong and -2, if the maze is solved
	 */
	public int tremauxAlgorithm(int orientation, boolean blackTileRetreat) {
		int direction = -1;

		int[] tremauxCounter = currentTile.getTremauxCounter();

		// check if maze is solved already
		if (checkSolved(tremauxCounter)) {
			logger.info("maze is solved");
			direction = -2;
			// method returns error, if there is no way to go
		} else if (possibleDirections(tremauxCounter) == 0) {
			logger.warn("there's no way to go");
			direction = -1;
			// check for dead end; if detected, robot reverses
		} else if (possibleDirections(tremauxCounter) == 1) {
			logger.info("dead end, taking only possible direction !");
			for (int i = 0; i < 4; i++) {
				if (tremauxCounter[i] != -2) {
					direction = i;
					break;
				}
			}
			// -> real intersection; check TremauxCounter to evaluate direction
		} else {
			if (currentTile.isVisited()
					&& tremauxCounter[currentTile
							.invertOrientation(orientation)] == 1) {
				// if tile was already visited and the previous corridor was
				// only taken once, the robot reverses and passes it a second
				// time
				logger.info("already visited, turning around !");
				currentTile.incTremauxCounter(currentTile
						.invertOrientation(orientation));
				return currentTile.invertOrientation(orientation);

			} else if (currentTile.isVisited()) {

				direction = rightmostDirection(orientation, tremauxCounter, 0);

				if (direction == -1) {
					// if there is no corridor which was never passed, the robot
					// takes the rightmost once passed corridor

					direction = rightmostDirection(orientation, tremauxCounter,
							1);

					currentTile.incTremauxCounter(direction);
					logger.info("tile already visited, passing rightmost once visited corridor !");
				} else {
					// if there is a corridor which was never passed, the robot
					// takes the rightmost one

					logger.info("tile already visited, passing rightmost never passed corridor !");
					currentTile.incTremauxCounter(direction);
					return direction;

				}
			} else {
				// tile was visited the first time; robot takes the rightmost
				// direction

				direction = rightmostDirection(orientation, tremauxCounter, 0);
				logger.info("tile was visited the first time, taking rightmost direction; d: "
						+ direction);
				currentTile.incTremauxCounter(direction);
				currentTile.setVisited();
			}
		}

		if (blackTileRetreat == false && currentTile.isVisited() == false) {
			currentTile.incTremauxCounter(currentTile
					.invertOrientation(orientation));
		}
		return direction;
	}

	/**
	 * evaluates the rightmost direction matching the given value
	 * 
	 * @param orientation
	 *            current orientation of the robot
	 * @param tremauxCounter
	 *            tremauxCounter of the to be checked tile
	 * @param value
	 * @return returns rightmost direction matching the given value, -1 if there
	 *         is none
	 */
	public int rightmostDirection(int orientation, int[] tremauxCounter,
			int value) {

		if (tremauxCounter[rightleftDirection(orientation, true)] == value) {
			return rightleftDirection(orientation, true);
		} else if (tremauxCounter[orientation] == value) {
			return orientation;
		} else if (tremauxCounter[rightleftDirection(orientation, false)] == value) {
			return rightleftDirection(orientation, false);
		} else if (tremauxCounter[currentTile.invertOrientation(orientation)] == value) {
			return currentTile.invertOrientation(orientation);
		}
		return -1;
	}

	/**
	 * @param orientation
	 * @param right
	 *            defines if the method returns either the right (true) or left
	 *            (false) direction
	 * @return either the left or the right direction of given orientation
	 */
	public int rightleftDirection(int orientation, boolean right) {
		if (right) {
			if (orientation == 3) {
				return 0;
			} else {
				return (orientation + 1);
			}
		} else {
			if (orientation == 0) {
				return 3;
			} else {
				return orientation - 1;
			}
		}

	}

	/**
	 * counts the possible directions
	 * 
	 * @param tremauxCounter
	 *            tremauxCounter of the to be checked tile
	 * @return amount of possible directions
	 */
	private int possibleDirections(int[] tremauxCounter) {
		int counter = 4;
		for (int i = 0; i < 4; i++) {
			if (tremauxCounter[i] == -2) {
				counter--;
			}
		}
		return counter;
	}

	/**
	 * checks if Maze is solved
	 * 
	 * @param tremauxCounter
	 *            tremauxCounter of the to be checked tile
	 * @return true if solved, false if not
	 */
	private boolean checkSolved(int[] tremauxCounter) {
		int counter = 4;
		for (int i = 0; i < 4; i++) {
			if (tremauxCounter[i] == -2 || tremauxCounter[i] == 2) {
				counter--;
			}
		}
		if (counter == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Cuts a Tile out of the map
	 * 
	 * @param tile
	 *            Tile which is to be cut out of the map
	 */
	public void disconnectTile(Node tile) {
		for (int i = 0; i < 4; i++) {
			tile.removeNeighbor(i);
		}
	}

	/**
	 * creates a new map layer and attaches it to the existing map
	 * 
	 * @param orientation
	 *            direction in which the new map layer is added
	 * @param mSizeX
	 *            Width of the to be generated map layer (at least 3; has to be
	 *            uneven)
	 * @param mSizeY
	 *            Height of the to be generated map layer (at least 3; has to be
	 *            uneven)
	 */
	public void slope(int orientation, int mSizeX, int mSizeY) {
		int x = currentTile.x;
		int y = currentTile.y;
		if (orientation == 0) {
			y += 30;
		} else if (orientation == 1) {
			x += 30;
		} else if (orientation == 2) {
			y -= 30;
		} else if (orientation == 3) {
			x -= 30;
		}
		Node buffer = initializeMap(mSizeX, mSizeY, x, y, 1);
		Node buffer2 = buffer
				.getNeighbor(buffer.invertOrientation(orientation));
		disconnectTile(buffer2);

		currentTile.removeNeighbor(orientation);
		currentTile.addNeighbor(buffer, orientation, 0);
	}

	/**
	 * used for testing purposes
	 * 
	 * @return returns current Node
	 */
	public Node getCurrentTile() {
		return currentTile;
	}

	public boolean isBlackTile() {
		return currentTile.isBlackTile();
	}

	public void setBlackTile() {
		currentTile.setBlackTile();
	}

	public Node[] getNeighbors() {
		return currentTile.getNeighbors();
	}

	public Node getNeighbor(int orientation) {
		return currentTile.getNeighbor(orientation);
	}

	public boolean removeNeighbor(Node neighbor) {
		return currentTile.removeNeighbor(neighbor);
	}

	public void removeNeighbor(int orientation) {
		currentTile.removeNeighbor(orientation);
	}

	public void switchTile(int orientation) {
		currentTile = currentTile.getNeighbor(orientation);
		if (currentTile == null) {
			logger.warn("CurrentTile is NULL !");
		}
	}

	/**
	 * used for testing purposes
	 * 
	 * @return
	 */
	public int[] getTremauxCounter() {
		return currentTile.getTremauxCounter();
	}

	/**
	 * used for testing purposes
	 * 
	 * @param tremaux
	 */
	public void setTremauxCounter(int[] tremaux) {
		currentTile.setTremauxCounter(tremaux);
	}

	/**
	 * creates Rectangular Nodemap of given dimensions; returns startNode, which
	 * is in the center of the Nodemap;
	 * 
	 * @param dimensionX
	 *            width of the map; has to be at least 3, has to be uneven
	 * @param dimensionY
	 *            height of the map; has to be at least 3, has to be uneven
	 */
	private Node initializeMap(int dimensionX, int dimensionY, int startX,
			int startY, int startZ) {

		logger.debug("initialising Node-Map...");
		Node initialNode = new Node(startX, startY, startZ);

		Node rowPointer = initialNode;
		Node columnPointer = initialNode;
		Node secondRowPointer = initialNode;
		Node secondColumnPointer = initialNode;

		int x = (dimensionX - 1) / 2;
		int y = (dimensionY - 1) / 2;

		/*
		 * i: used to decide whether x Coordinates of Node are positive or
		 * negative o: used to change the orientation of the adding Nodes during
		 * iteration c: indicates the depth of the column being added
		 */

		// Generates the initial line of Nodes
		for (int i = 1, o = 1; i >= -1; i -= 2, o += 2) {
			for (int c = 1; c <= x; c++) {
				columnPointer.addNeighbor(new Node(startX + (i * c * 30),
						startY, startZ), o, 0);
				logger.debug("created Node: x:" + (startX + (i * c * 30))
						+ " y: " + startY);
				columnPointer = columnPointer.getNeighbor(o);
			}
			columnPointer = initialNode;
		}

		/*
		 * i: used to decide whether x coordinate of Node is positive or
		 * negative i2: used to decide whether y coordinate of Node is positive
		 * or negative o, o2, o3: used to change the orientation of the, to be
		 * added, Nodes during iteration c: indicates the depth of the column
		 * being added r: indicates the depth of the row being added
		 */

		// iterates to generate rows of Node-lines in both North and South
		for (int i2 = 1, o2 = 2, o3 = 0; i2 >= -1; i2 -= 2, o2 -= 2, o3 += 2) {
			// iterates to generate several rows of Node-lines
			for (int r = 1; r <= y; r++) {
				rowPointer.addNeighbor(new Node(startX, startY + (r * 30),
						startZ), o3, 0);
				rowPointer = rowPointer.getNeighbor(o3);
				columnPointer = rowPointer;
				// iterates to generate a Line of Nodes in both East and West
				for (int i = 1, o = 1; i >= -1; i -= 2, o += 2) {
					// generates line of Nodes in one direction
					for (int c = 1; c <= x; c++) {
						columnPointer.addNeighbor(
								new Node(startX + (i * c * 30), startY
										+ (i2 * r * 30), startZ), o, 0);
						logger.debug("created Node: x:"
								+ (startX + (i * c * 30)) + " y: "
								+ (startY + (i2 * r * 30)));
						columnPointer = columnPointer.getNeighbor(o);
						secondColumnPointer = secondColumnPointer
								.getNeighbor(o);
						columnPointer.addNeighbor(secondColumnPointer, o2, 0);
					}
					columnPointer = rowPointer;
					secondColumnPointer = secondRowPointer;
				}
				secondRowPointer = rowPointer;
			}
			rowPointer = initialNode;
			secondRowPointer = initialNode;
			columnPointer = initialNode;
			secondColumnPointer = initialNode;
		}

		return initialNode;
	}

}