package de.null_pointer.navigation.map;

public class NodeAdapted extends lejos.robotics.pathfinding.Node {
	
	private int visited = 0;

	public NodeAdapted(float x, float y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public int getVisited() {
		return visited;
	}

	public void visit() {
		visited++;
	}

}
