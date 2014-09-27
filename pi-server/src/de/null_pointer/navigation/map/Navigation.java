package de.null_pointer.navigation.map;


public class Navigation {
	
	private Node currentTile;
	
	public Navigation(int x, int y){
		currentTile = initializeMap(x, y);
	}
	
	//TODO
	public int tremauxAlgorithm(){
		int direction = 0;
		return direction;
	}
	
	/**
	 * Cuts a Tile out of the map
	 * @param tile
	 * 			Tile which is to be cut out of the map
	 */
	public void disconnectTile(Node tile){
		for(int i = 0; i < 4; i++){
			tile.removeNeighbor(i);
		}
	}
	
	/**
	 * WARNING: Node coordinates are messed up afterwards 
	 * (new map level is basically a new map connected to the original one)
	 * @param orientation
	 * 				direction in which the new map layer is added
	 */
	public void slope(int orientation){
		Node buffer = initializeMap(11, 11);
		Node buffer2 = buffer.getNeighbor(buffer.invertOrientation(orientation));
		disconnectTile(buffer2);
		
		currentTile.removeNeighbor(orientation);
		currentTile.addNeighbor(buffer, orientation, 0);
	}
	
	public boolean isVisited(){
		return currentTile.isVisited();
	}

	public void setVisited(){
		currentTile.setVisited();
	}
	
	public boolean isBlackTile(){
		return currentTile.isBlackTile();
	}
	
	public void setBlackTile(){
		currentTile.setBlackTile();
	}
	
	public Node[] getNeighbors(){
		return currentTile.getNeighbors();
	}
	
	public Node getNeighbor(int orientation){
		return currentTile.getNeighbor(orientation);
	}
	
	public boolean removeNeighbor(Node neighbor){
		return currentTile.removeNeighbor(neighbor);
	}
	
	public void removeNeighbor(int orientation){
		currentTile.removeNeighbor(orientation);
	}
	
	public void switchTile(int orientation){
		currentTile = currentTile.getNeighbor(orientation);
	}
	
	/**
	 * creates Rectangular Nodemap of given dimensions; returns startNode, which is in the center of the Nodemap;
	 * @param dimensionX
	 * 			width of the map; has to be at least 3, has to be uneven
	 * @param dimensionY
	 * 			height of the map; has to be at least 3, has to be uneven
	*/
	private Node initializeMap(int dimensionX, int dimensionY){
			
		Node initialNode = new Node(0, 0);
			
		Node rowPointer = initialNode;
		Node columnPointer = initialNode;
		Node drowPointer = initialNode;
		Node dcolumnPointer = initialNode;
			
		int x = (dimensionX - 1) / 2;
		int y = (dimensionY - 1) / 2;
			
		/*
		 * i: used to decide whether x Coordinates of Node are positive or negative
		 * o: used to change the orientation of the adding Nodes during iteration
		 * c: indicates the depth of the column being added
		 */
			
		//Generates the initial line of Nodes
		for(int i = 1, o = 1; i >= -1; i -= 2, o += 2){
			for(int c = 1; c <= x; c++){
				columnPointer.addNeighbor(new Node(i*c*30,0), o, 0);
				columnPointer = columnPointer.getNeighbor(o);
			}
			columnPointer = initialNode;
		}
			
		/*
		 * i: used to decide whether x coordinate of Node is positive or negative
		 * i2: used to decide whether y coordinate of Node is positive or negative
		 * o, o2, o3: used to change the orientation of the, to be added, Nodes during iteration
		 * c: indicates the depth of the column being added
		 * r: indicates the depth of the row being added
		 */
		
		// iterates to generate rows of Node-lines in both North and South
		for(int i2 = 1, o2 = 2, o3 = 0; i2 >= -1; i2 -= 2, o2 -= 2, o3 += 2){	
			// iterates to generate several rows of Node-lines
			for(int r = 1; r <= y; r++){						
				rowPointer.addNeighbor(new Node(0, r*30), o3, 0);
				rowPointer = rowPointer.getNeighbor(o3);
				columnPointer = rowPointer;
				// iterates to generate a Line of Nodes in both East and West
				for(int i = 1, o = 1; i >= -1; i -= 2, o += 2){
					// generates line of Nodes in one direction
					for(int c = 1; c <= x; c++){
						columnPointer.addNeighbor(new Node(i*c*30, i2*r*30), o, 0);
						columnPointer = columnPointer.getNeighbor(o);
						dcolumnPointer = dcolumnPointer.getNeighbor(o);
						columnPointer.addNeighbor(dcolumnPointer, o2, 0);
					}
					columnPointer = rowPointer;
					dcolumnPointer = drowPointer;
				}
				drowPointer = rowPointer;
			}
			rowPointer = initialNode;
			drowPointer = initialNode;
			columnPointer = initialNode;
			dcolumnPointer = initialNode;
		}
				
		return initialNode;
	}
}