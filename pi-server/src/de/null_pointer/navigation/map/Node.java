package de.null_pointer.navigation.map;

import org.apache.log4j.Logger;

/**
 * This class represents a Node which can be connected to other neighboring
 * nodes. Node sets can be searched using search algorithms. Typically the
 * search algorithm only requires one starting node and one goal node. It
 * assumes these nodes are linked by intermediate nodes.
 * 
 * Orignal Code from lejos;
 * 
 * @author BB
 * @see lejos.robotics.pathfinding.SearchAlgorithm
 */
public class Node {

	private static Logger logger = Logger.getLogger(Node.class);

	/**
	 * The x coordinate of this node.
	 */
	public int x;

	/**
	 * The y coordinate of this node.
	 */
	public int y;

	/**
	 * The z coordinate of this node.
	 */
	public int z;

	/**
	 * Indicates if intersection/ node is a Black Tile.
	 */
	private boolean blackTile = false;

	/**
	 * Indicates if intersection/ node was already visited.
	 */
	private boolean visited = false;

	/**
	 * List of neighbors to this node. 0 = North; 1 = East; 2 = South; 3 = West;
	 */
	private Node[] neighbors = new Node[4];

	private int[] tremauxCounter = { 0, 0, 0, 0 };

	/**
	 * Creates a new instance of a node.
	 * 
	 * @param x
	 *            The x coordinate of this node.
	 * @param y
	 *            The y coordinate of this node.
	 */
	public Node(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean isBlackTile() {
		return blackTile;
	}

	public void setBlackTile() {
		blackTile = true;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited() {
		visited = true;
	}

	/**
	 * used for testing purposes
	 * 
	 * @param tremaux
	 */
	public void setTremauxCounter(int[] tremaux) {
		tremauxCounter = tremaux;
	}

	public int[] getTremauxCounter() {
		return tremauxCounter;
	}

	public void incTremauxCounter(int orientation) {
		tremauxCounter[orientation]++;
	}

	/**
	 * Returns all the neighbors which this node is connected to.
	 * 
	 * @return The collection of all neighboring nodes.
	 */
	public Node[] getNeighbors() {
		return neighbors;
	}

	public Node getNeighbor(int orientation) {
		return neighbors[orientation];
	}

	private void setNeighbors(Node[] neighborCopy) {
		neighbors = neighborCopy;
	}

	/**
	 * Indicates the number of neighbors (nodes connected to this node).
	 * 
	 * @return int Number of neighbors.
	 */
	public int neighbors() {
		int counter = 0;
		for (Node neighbor : neighbors) {
			if (neighbor != null) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * Adds a neighboring node to this node, connecting them together.
	 * 
	 * @param neighbor
	 *            The neighboring node to connect with.
	 * @param orientation
	 *            indicates the orientation of the added Node
	 * @param abort
	 *            has to be 0; used to prevent wrong error messages
	 * @return Returns false if the neighbor already existed, or if you try to
	 *         add this node to itself as a neighbor.
	 */
	// TODO: addNeighbor()-Methode unbedingt mit jUnit testen
	public boolean addNeighbor(Node neighbor, int orientation, int abort) {
		// Check if Node is already connected
		if (abort == 2) {
			return true;
		}
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] == neighbor) {
				if (i == orientation) {
					logger.error("Der einzufuegende Nachbar ist schon an der gewollten Stelle eingefuegt!");
				} else {
					logger.error("Der einzufuegende Nachbar ist schon an einer anderen Stelle eingefuegt!");
				}
				return false;
			}
		}

		// Check to make sure doesn't add itself
		if (neighbor == this) {
			logger.error("Die Node wird mit sich selbst verbunden!");
			return false;
		}
		int a = abort + 1;
		neighbors[orientation] = neighbor;

		neighbor.addNeighbor(this, invertOrientation(orientation), a);
		return true;
	}

	/**
	 * Removes a node from this node as neighbors, effectively disconnecting
	 * them.
	 * 
	 * @param neighbor
	 *            The neighboring node to disconnect from.
	 * @return Returns false if the neighbor did not previously exist as a
	 *         neighbor.
	 */
	public boolean removeNeighbor(Node neighbor) {
		for (int i = 0; i < neighbors.length; i++) {
			if (neighbors[i] == neighbor) {
				Node[] neighborCopy = neighbors[i].getNeighbors();
				for (int j = 0; j < neighborCopy.length; j++) {
					if (neighborCopy[j] == this) {
						neighborCopy[j] = null;
					}
				}
				neighbors[i].setNeighbors(neighborCopy);
				neighbors[i] = null;
				tremauxCounter[i] = -2;
				return true;
			}
		}
		logger.error("Node konnte nicht entfernt werden, da er kein Nachbar ist");
		return false;
	}

	/**
	 * Removes a node from this node as neighbors, effectively disconnecting
	 * them.
	 * 
	 * @param orientation
	 *            Orientation of neighboring node to disconnect from.
	 * @return Returns false if the neighbor did not previously exist as a
	 *         neighbor.
	 */
	public void removeNeighbor(int orientation) {
		if (neighbors[orientation] == null) {
			//logger.warn("Nachbar-Eintrag war bereits null/ kein Nachbar vorhanden");
		} else {
			neighbors[orientation].removeNeighbor(this);
			neighbors[orientation] = null;
			tremauxCounter[orientation] = -2;
		}
	}

	public int invertOrientation(int initialOrientation) {
		int calculated_orientation = 0;
		if (initialOrientation <= 1) {
			calculated_orientation = initialOrientation + 2;
		} else if (initialOrientation >= 2) {
			calculated_orientation = initialOrientation - 2;
		}
		return calculated_orientation;
	}

}