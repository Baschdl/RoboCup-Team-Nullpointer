package de.null_pointer.navigation.map;

import org.apache.log4j.Logger;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * This class represents a Node which can be connected to other neighboring
 * nodes. Node sets can be searched using search algorithms. Typically the
 * search algorithm only requires one starting node and one goal node. It
 * assumes these nodes are linked by intermediate nodes.
 * 
 * @author BB
 * @see lejos.robotics.pathfinding.SearchAlgorithm
 */
public class Node {

	private static Logger logger = Logger.getLogger(Node.class);

	/**
	 * This constant is multiplied with the float coordinate to get an integer
	 * for faster internal computations. A multiplier of 100 gives the
	 * equivalent of 2 decimal places (1.2345 = 123)
	 */
	static final int MULTIPLIER = 100;

	/**
	 * The x coordinate of this node.
	 */
	public float x;

	/**
	 * The y coordinate of this node.
	 */
	public float y;

	/**
	 * The cumulative distance from the start node to this node.
	 */
	// private float g_score = 0;

	/*
	 * The estimated distance to the goal node from this node. Distance
	 * "as the crow flies"
	 */
	// private float h_score = 0;

	/**
	 * The predecessor node used by A* search algorithm to mark off the previous
	 * node in the search tree.
	 */
	// private Node cameFrom = null;

	/**
	 * Indicates if intersection/ node was visited already.
	 */
	private boolean visited = false;

	/**
	 * List of neighbors to this node.
	 * 0 = North
	 * 1 = East
	 * 2 = South
	 * 3 = West
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
	public Node(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited() {
		this.visited = true;
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
	
	public Node getNeighbor(int orientation){
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
	 * Adds a neighboring node to this node, connecting them together. Note: You
	 * must make sure to add this node to the neighbor, and also add the
	 * neighbor to this node. This method doesn't do both.
	 * 
	 * @param neighbor
	 *            The neighboring node to connect with.
	 * @param orientation
	 * 			  indicates the orientation of the added Node
	 * @return Returns false if the neighbor already existed, or if you try to
	 *         add this node to itself as a neighbor.
	 */
	// TODO: addNeighbor()-Methode unbedingt mit jUnit testen
	public boolean addNeighbor(Node neighbor, int orientation) {
		// Check if Node is already connected
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
		int i = orientation;
		neighbors[i] = neighbor;

		neighbor.addNeighbor(this, invertOrientation(i));
		return true;
	}

	/**
	 * Removes a node from this node as neighbors, effectively disconnecting
	 * them. Note: You have to remove this node from the neighbor, and also
	 * remove the neighbor from this node. This method doesn't do both.
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
				return true;
			}
		}
		logger.error("Node konnte nicht entfernt werden, da er kein Nachbar ist");
		return false;
	}

	public void removeNeighbor(int orientation) {
		int i = -1;
		// Falls eine Exception auftritt, weist er der Variable i keinen neuen
		// Wert zu, sondern benutzt -1
		if (neighbors[i = orientation] == null) {
			logger.warn("Nachbar-Eintrag war bereits null/ kein Nachbar vorhanden");
		} else {
			neighbors[i].removeNeighbor(this);
			neighbors[i] = null;

		}
	}

	private int invertOrientation(int initialOrientation) {
		int calculated_orientation = 0;
		if (initialOrientation <= 1) {
			calculated_orientation = calculated_orientation + 2;
		} else if (initialOrientation >= 2) {
			calculated_orientation = calculated_orientation - 2;
		}
		return calculated_orientation;
	}
	/**
	 * Method used by A* to calculate search score. The H score is the estimated
	 * distance to the goal node from this node. It can either be distance
	 * "as the crow flies" or in the case of a grid navigation mesh, the minimum
	 * number of grid spaces to get to the goal node (x squares horizontally + y
	 * squares vertically from goal). NOTE: There is no getH_score() because the
	 * A* algorithm only needs to set this value, not retrieve it.
	 * 
	 * @param h
	 */
	// protected void setH_Score(float h) {
	// h_score = h;
	// }

	/**
	 * Calculates the distance to a neighbor node. This method is used to
	 * optimize the algorithm.
	 * 
	 * @param neighbor
	 * @return the distance to neighbor
	 */
	// protected float calculateG(Node neighbor) {
	// return (float) Point2D.distance(this.x, this.y, neighbor.x, neighbor.y);
	// }

	/**
	 * Calculates the distance to the goal node. This method is used to optimize
	 * the algorithm.
	 * 
	 * @param goal
	 * @return the distance to goal
	 */
	// protected float calculateH(Node goal) {
	// return calculateG(goal);
	// }

	/**
	 * Method used by A* to calculate search score. The G score is the
	 * cumulative distance from the start node to this node.
	 * 
	 * @param g
	 */
	// protected void setG_Score(float g) {
	// g_score = g;
	// }

	/**
	 * Method used by A* to calculate search score. The G score is the
	 * cumulative distance from the start node to this node.
	 * 
	 * @return the search score
	 */
	// protected float getG_Score() {
	// return g_score;
	// }

	/**
	 * Method used by A* to calculate search score. You can't set FScore because
	 * it is derived internally by adding the gscore and hscore.
	 */
	// protected float getF_Score() {
	// return g_score + h_score;
	// }

	/**
	 * Used by A* search. Stores the node that the search came from prior to
	 * this node.
	 * 
	 * @return the predecessor node
	 */
	// protected Node getPredecessor() {
	// return cameFrom;
	// }

	/**
	 * Used by A* search. Stores the node that the search came from prior to
	 * this node.
	 * 
	 * @param orig
	 */
	// protected void setPredecessor(Node orig) {
	// cameFrom = orig;
	// }
}