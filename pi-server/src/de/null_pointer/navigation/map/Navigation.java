package de.null_pointer.navigation.map;


public class Navigation {
	
	private Node currentTile;
	
	public Navigation(){
		currentTile = initializeMap(11,11);
	}
	
	// width, height have to be at least 3 (for  3 Tiles); have to be uneven;
	//creates Rectangular Nodemap of given dimensions; startNode is in the center of the Nodemap;
	public Node initializeMap(int dimensionX, int dimensionY){
			
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
				columnPointer.addNeighbor(new Node(i*c*30,0), o);
				columnPointer = columnPointer.getNeighbor(o);
			}
			columnPointer = initialNode;
		}
			
		/*
		 * i: used to decide whether x coordinate of Node is positive or negative
		 * i2: used to decide whether y coordinate of Node is positive or negative
		 * o, o2, o3: used to change the orientation of the to be added Nodes during iteration
		 * c: indicates the depth of the column being added
		 * r: indicates the depth of the row being added
		 */
		
		// iterates to generate rows of Node-lines in both North and South;	
		for(int i2 = 1, o2 = 2, o3 = 0; i2 >= -1; i2 -= 2, o2 -= 2, o3 += 2){	
			// iterates to generate several rows of Node-lines;
			for(int r = 1; r <= y; r++){						
				rowPointer.addNeighbor(new Node(0, r*30), o3);
				rowPointer = rowPointer.getNeighbor(o3);
				columnPointer = rowPointer;
				// iterates to generate a Line of Nodes in both East and West;
				for(int i = 1, o = 1; i >= -1; i -= 2, o += 2){
					// generates line of Nodes in one direction;
					for(int c = 1; c <= x; c++){
						columnPointer.addNeighbor(new Node(i*c*30, i2*r*30), o);
						columnPointer = columnPointer.getNeighbor(o);
						dcolumnPointer = dcolumnPointer.getNeighbor(o);
						columnPointer.addNeighbor(dcolumnPointer, o2);
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
