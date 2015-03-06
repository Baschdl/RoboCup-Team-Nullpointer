package de.null_pointer.navigation.map;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.navigation.test.NavCompetitionHandler;

/**
 * 
 * @author Jan Krebes (jankrebes@null-pointer.de)
 */
public class Navigation {

	private static Logger logger = Logger.getLogger(Navigation.class);

	private Node currentTile;
	private Node startTile;

	private Node lastCheckpointTile;
	private TurnSave currentTurn = null;
	private TurnSave initialTurn = null;

	private int lastDirection = -1;
	private boolean firstTile = true;

	private NavCompetitionHandler navComp = null;

	public Navigation(Properties propPiServer, NavCompetitionHandler navComp) {
		int dimensionX = Integer.parseInt(propPiServer
				.getProperty("Navigation.Navigation.mapWidth"));
		int dimensionY = Integer.parseInt(propPiServer
				.getProperty("Navigation.Navigation.mapHeight"));
		this.currentTile = initializeMap(dimensionX, dimensionY, 0, 0, 0);
		this.currentTile.setVisited();
		this.lastCheckpointTile = initializeMap(dimensionX, dimensionY, 0, 0, 0);
		this.lastCheckpointTile.setVisited();
		this.startTile = this.currentTile;
		this.navComp = navComp;
	}

	/**
	 * contructor for Testing purposes
	 * 
	 * @param dimensionX
	 * @param dimensionY
	 */
	public Navigation(int dimensionX, int dimensionY) {
		currentTile = initializeMap(dimensionX, dimensionY, 0, 0, 0);
		currentTile.setVisited();
		lastCheckpointTile = initializeMap(dimensionX, dimensionY, 0, 0, 0);
		lastCheckpointTile.setVisited();
		startTile = currentTile;
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
		try {
			int direction = -1;

			// check if direction to move was already evaluated
			// if so, the last evaluated direction gets returned
			if (currentTile.getTremauxAlreadyEvaluated() == false) {
				int[] tremauxCounter = currentTile.getTremauxCounter();

				if (blackTileRetreat == false && firstTile == false) {
					currentTile.incTremauxCounter(currentTile
							.invertOrientation(orientation));
				}

				// check if maze is solved already
				if (checkSolved(tremauxCounter) && currentTile == startTile) {

					logger.info("maze is solved ! d: -2");
					return -2;
					// method returns error, if there is no way to go
				} else if (possibleDirections(tremauxCounter) == 0) {
					direction = -1;

					logger.warn("there's no way to go ! d: " + direction);

					// check for dead end; if detected, robot takes only
					// possible
					// direction
				} else if (possibleDirections(tremauxCounter) == 1) {
					for (int i = 0; i < 4; i++) {
						if (tremauxCounter[i] != -2) {
							direction = i;
							break;
						}
					}

					logger.info("dead end, taking only possible direction ! d: "
							+ direction);

					// check for hallway and follow it if it's not a black tile
					// retreat
				} else if (possibleDirections(tremauxCounter) == 2) {
					if (blackTileRetreat == false) {
						for (int i = 0; i < 4; i++) {
							if (tremauxCounter[i] != -2
									&& i != currentTile
											.invertOrientation(orientation)) {
								direction = i;
								break;
							}
						}

						logger.info("hallway detectet ! d: " + direction);

					} else {
						for (int i = 0; i < 4; i++) {
							if (tremauxCounter[i] != -2
									&& i != currentTile
											.invertOrientation(lastDirection)) {
								direction = i;
								break;
							}
						}
						logger.info("BlackTileRetreat: hallway detectet ! d: "
								+ direction);
					}

					// -> real intersection; check TremauxCounter to evaluate
					// direction
				} else {

					// if tile was already visited and the previous corridor was
					// only taken once, the robot reverses and passes it a
					// second
					// time
					if (blackTileRetreat == false) {
						if (currentTile.isVisited()
								&& tremauxCounter[currentTile
										.invertOrientation(orientation)] == 1) {

							direction = currentTile
									.invertOrientation(orientation);

							if (blackTileRetreat) {
								logger.info("BlackTileRetreat: already visited, turning around ! d: "
										+ direction);
							} else {
								logger.info("already visited, turning around ! d: "
										+ direction);
							}

							if (direction >= 0) {
								currentTile.incTremauxCounter(direction);
							}
							lastDirection = direction;
							addTurn(direction, tremauxCounter, blackTileRetreat);
							return direction;
						}
					}
					if (currentTile.isVisited()) {

						direction = rightmostDirection(orientation,
								tremauxCounter, 0);

						// if there is no corridor which was never passed, the
						// robot
						// takes the rightmost once passed corridor
						if (direction == -1) {
							direction = rightmostDirection(orientation,
									tremauxCounter, 1);

							if (blackTileRetreat) {
								logger.info("BlackTileRetreat: tile already visited, passing rightmost once visited corridor ! d: "
										+ direction);
							} else {
								logger.info("tile already visited, passing rightmost once visited corridor ! d: "
										+ direction);
							}
						}

						// if there is a corridor which was never passed, the
						// robot
						// takes the rightmost one
						else {
							if (blackTileRetreat) {
								logger.info("BlackTileRetreat: tile already visited, passing rightmost never passed corridor ! d: "
										+ direction);
							} else {
								logger.info("tile already visited, passing rightmost never passed corridor ! d: "
										+ direction);
							}
						}
					}
					// tile was visited the first time; robot takes the
					// rightmost
					// direction
					else {
						direction = rightmostDirection(orientation,
								tremauxCounter, 0);

						logger.info("tile was visited the first time, taking rightmost direction ! d: "
								+ direction);

						currentTile.setVisited();
					}
				}

				if (firstTile) {
					firstTile = false;
				}
				if (direction >= 0) {
					currentTile.incTremauxCounter(direction);
				}
				lastDirection = direction;
				currentTile.setTremauxAlreadyEvaluated(true);
				addTurn(direction, tremauxCounter, blackTileRetreat);
				return direction;
			} else {
				logger.info("Direction already evaluated ! d: " + lastDirection);
				return lastDirection;
			}
		} catch (Exception e) {
			logger.error("TremauxAlgorithm: an error occured: " + e);
			return -1;
		}
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
			if (tremauxCounter[i] == -2 || tremauxCounter[i] >= 2) {
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
	 *            Tile to be cut out of the map
	 */
	public void disconnectTile(Node tile) {
		try {
			for (int i = 0; i < 4; i++) {
				tile.removeNeighbor(i);
			}
		} catch (Exception e) {
			logger.error("disconnectTile: an error occured: " + e);
		}
	}

	/**
	 * Warning: errors occured when using this method !
	 * 
	 * Cuts all side-Node-Connections from the currentTile to the last
	 * Intersection
	 * 
	 * @param lastIntersection
	 *            Node of the last Intersection
	 */
	public void cutNodeConnections(Node lastIntersection) {
		try {
			Node buffer = currentTile;
			if (buffer.z == lastIntersection.z) {
				int orientation = -1;
				if (buffer.x == lastIntersection.x) {
					if (buffer.y < lastIntersection.y) {
						orientation = 2;
					} else if (buffer.y == lastIntersection.y) {
						orientation = -1;
					} else {

						orientation = 0;
					}
				} else if (buffer.y == lastIntersection.y) {
					if (buffer.x < lastIntersection.x) {
						orientation = 3;
					} else {
						orientation = 1;
					}
				}
				if (orientation > -1) {
					while (buffer != lastIntersection) {
						buffer = buffer.getNeighbor(orientation);
						buffer.removeNeighbor(rightleftDirection(orientation,
								true));
						buffer.removeNeighbor(rightleftDirection(orientation,
								false));
					}
				}
			}
		} catch (Exception e) {
			logger.error("cutNodeConnections: an error occured: " + e);
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
		try {
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
			Node buffer2 = buffer.getNeighbor(buffer
					.invertOrientation(orientation));
			disconnectTile(buffer2);

			currentTile.removeNeighbor(orientation);
			currentTile.addNeighbor(buffer, orientation, 0);

		} catch (Exception e) {
			logger.error("slope: an error occured: " + e);
		}
	}

	/**
	 * used for testing purposes
	 * 
	 * @return returns current Node
	 */
	public Node getCurrentTile() {
		return currentTile;
	}

	/**
	 * 
	 * @return returns true if currentTile is marked as a black-tile <br>
	 *         false if not
	 */
	public boolean isBlackTile() {
		try {
			return currentTile.isBlackTile();
		} catch (Exception e) {
			logger.error("isBlackTile: an error occured: " + e);
			return false;
		}
	}

	/**
	 * marks the currentTile as a black-tile
	 */
	public void setBlackTile() {
		try {
			currentTile.setBlackTile();
		} catch (Exception e) {
			logger.error("setBlackTile: an error occured: " + e);
		}
	}

	/**
	 * @return returns an array[4] of the neighbors of the currentTile
	 */
	public Node[] getNeighbors() {
		try {
			return currentTile.getNeighbors();
		} catch (Exception e) {
			logger.error("getNeighbors: an error occured: " + e);
			return null;
		}
	}

	/**
	 * returns the Neighbor of the currentTile in given orientation
	 * 
	 * @param orientation
	 *            orientation of the wanted neighbor
	 * @return Neighbor of given orientation
	 */
	public Node getNeighbor(int orientation) {
		try {
			return currentTile.getNeighbor(orientation);
		} catch (Exception e) {
			logger.error("getNeighbor: an error occured: " + e);
			return null;
		}
	}

	/**
	 * removes the given neighbor of the currentTile
	 * 
	 * @param neighbor
	 *            neighboring node to remove
	 * @return true if successfully; false if given node was not a neighbor of
	 *         the currentTile or is the currentTile is NULL
	 */
	public boolean removeNeighbor(Node neighbor) {
		try {
			return currentTile.removeNeighbor(neighbor);
		} catch (Exception e) {
			logger.error("removeNeighbor(Node): an error occured: " + e);
			return false;
		}
	}

	/**
	 * remove the Neighbor of the currentTile in the given orientation
	 * 
	 * @param orientation
	 *            orientation of the to be removed neighbor
	 */
	public void removeNeighbor(int orientation) {
		try {
			logger.info("removeNeighbor(int): removing neighbor; ORIENTATION: "
					+ orientation);
			if (navComp != null) {
				navComp.removeNeighbor(orientation);
			}
			currentTile.removeNeighbor(orientation);
		} catch (Exception e) {
			logger.error("removeNeighbor(int): an error occured: " + e);
		}
	}

	/**
	 * used to switch the currentTile with its neighbor in given orientation
	 * 
	 * @param orientation
	 *            direction to switch the Tile
	 */
	public void switchTile(int orientation) {
		try {
			logger.info("switchTile(): Switching tile");
			if (navComp != null) {
				navComp.switchTile(orientation);
			}
			currentTile.setTremauxAlreadyEvaluated(false);
			currentTile = currentTile.getNeighbor(orientation);
			if (currentTile == null) {
				logger.error("CurrentTile is NULL !");
			}
		} catch (Exception e) {
			logger.error("switchTile: an error occured: " + e);
		}
	}

	/**
	 * @return returns the TremauxCounter of the currentTile; null if currentTle
	 *         is null
	 */
	public int[] getTremauxCounter() {
		try {
			return currentTile.getTremauxCounter();
		} catch (Exception e) {
			logger.error("getTremauxCounter: an error occured: " + e);
			return null;
		}
	}

	/**
	 * used for testing purposes
	 * 
	 * @param tremauxCounter
	 */
	public void setTremauxCounter(int[] tremauxCounter) {
		try {
			currentTile.setTremauxCounter(tremauxCounter);
		} catch (Exception e) {
			logger.error("setTremauxCounter: an error occured: " + e);
		}
	}

	/**
	 * creates Rectangular node-map of given dimensions; returns center-node
	 * 
	 * @param dimensionX
	 *            width of the map; has to be at least 3, has to be uneven
	 * @param dimensionY
	 *            height of the map; has to be at least 3, has to be uneven
	 * @param startX
	 *            x-coordinate of the center-node
	 * @param startY
	 *            y-coordinate of the center-node
	 * @param startZ
	 *            z-coordinate of the center-node
	 * @return returns the center-node of the newly created node-map
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

		// i: used to decide whether x Coordinates of Node are positive or
		// negative
		// o: used to change the orientation of the adding Nodes during
		// iteration
		// c: indicates the depth of the column being added

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

		// xSign: used to decide whether x coordinate of Node is positive or
		// negative
		// ySign: used to decide whether y coordinate of Node is positive
		// or negative
		// orientLeftRight, orientDownUp, orientUpDown:
		// used to change the orientation of the to be added Node from left to
		// right/ down to up/ up to down
		// column: indicates the number of the column being added
		// row: indicates the number of the row being added

		// iterates to generate rows of Node-lines in both North and South
		for (int ySign = 1, orientDownUp = 2, orientUpDown = 0; ySign >= -1; ySign -= 2, orientDownUp -= 2, orientUpDown += 2) {
			// iterates to generate several rows of Node-lines
			for (int row = 1; row <= y; row++) {
				rowPointer.addNeighbor(new Node(startX, startY
						+ (ySign * row * 30), startZ), orientUpDown, 0);
				rowPointer = rowPointer.getNeighbor(orientUpDown);
				// iterates to generate a Line of Nodes in both East and West
				for (int xSign = 1, orientLeftRight = 1; xSign >= -1; xSign -= 2, orientLeftRight += 2) {
					// generates line of Nodes in one direction
					columnPointer = rowPointer;
					secondColumnPointer = secondRowPointer;
					for (int column = 1; column <= x; column++) {
						columnPointer.addNeighbor(new Node(startX
								+ (xSign * column * 30), startY
								+ (ySign * row * 30), startZ), orientLeftRight,
								0);
						columnPointer = columnPointer
								.getNeighbor(orientLeftRight);
						secondColumnPointer = secondColumnPointer
								.getNeighbor(orientLeftRight);
						columnPointer.addNeighbor(secondColumnPointer,
								orientDownUp, 0);
					}
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

	/**
	 * overwrites the current mapPointer with the mapPointer of the last
	 * checkpoint
	 */
	public void loadMap() {
		try {
			currentTile = lastCheckpointTile;
			currentTurn = null;
		} catch (Exception e) {
			logger.error("loadMap: an error occured: " + e);
		}

	}

	/**
	 * saves the current state of the map to a backup-map
	 */
	public void saveMap() {
		try {
			currentTurn = initialTurn;
			while (currentTurn != null) {
				int[] tremauxCounter = currentTurn.getTremauxCounter();
				for (int i = 0; i < 4; i++) {
					if (tremauxCounter[i] == -2) {
						lastCheckpointTile.removeNeighbor(i);
					}
				}
				lastCheckpointTile.setTremauxCounter(tremauxCounter);
				lastCheckpointTile.setVisited();
				lastCheckpointTile = lastCheckpointTile.getNeighbor(currentTurn
						.getMoveDirection());
				currentTurn = currentTurn.getNextTurn();
			}
		} catch (Exception e) {
			logger.error("saveMap: an error occured: " + e);
		}
	}

	private void addTurn(int moveDirection, int[] tremauxCounter,
			boolean blackTileRetreat) {
		try {
			TurnSave nextTurn = new TurnSave(moveDirection, tremauxCounter);
			if (currentTurn != null) {
				if (blackTileRetreat == false) {
					currentTurn.setNextTurn(nextTurn);
				}
				currentTurn = nextTurn;
			} else {
				initialTurn = nextTurn;
				currentTurn = initialTurn;
			}
		} catch (Exception e) {
			logger.error("addTurn: an error occured: " + e);
		}
	}

	/**
	 * used to ask if a victim was already found on a wall in the given
	 * direction on the currentTile
	 * 
	 * @param direction
	 *            direction to search for a found victim
	 * @return true if a victim has already been found; false if not
	 */
	public boolean getVictimFound(int direction) {
		try {
			return currentTile.getVictimFound(direction);
		} catch (Exception e) {
			logger.error("getVictimFound: an error occured: " + e);
			return false;
		}
	}

	/**
	 * used to save the position of a found victim on the currentTile
	 * 
	 * @param direction
	 *            direction in which the victim was found
	 */
	public void setVictimFound(int direction) {
		try {
			currentTile.setVictimFound(direction);
		} catch (Exception e) {
			logger.error("setVictimFound: an error occured: " + e);
		}
	}

}