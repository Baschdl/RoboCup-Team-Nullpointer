package de.null_pointer.navigation.test;

import javax.swing.JDialog;
import javax.swing.JTextArea;

import de.null_pointer.navigation.map.Node;

public class CoordinateDialog extends JDialog {

	JTextArea areaCur = null;
	JTextArea[] areaOrientation = null;
	JTextArea areaDebug = null;

	public CoordinateDialog() {
		super();
		areaOrientation = new JTextArea[4];
		this.setSize(300, 300);
		this.getContentPane().setLayout(null);
		initLabels();
	}

	private void initLabels() {
		{
			areaCur = new JTextArea();
			this.add(areaCur);
			areaCur.setBounds(100, 55, 80, 35);
			areaCur.setText("current:\n x: 666 y: 666");
		}
		{
			areaOrientation[0] = new JTextArea();
			this.add(areaOrientation[0]);
			areaOrientation[0].setBounds(100, 10, 80, 35);
			areaOrientation[0].setText("Orientation 0:\n x: 666 y: 666");
		}
		{
			areaOrientation[1] = new JTextArea();
			this.add(areaOrientation[1]);
			areaOrientation[1].setBounds(190, 55, 80, 35);
			areaOrientation[1].setText("Orientation 0:\n x: 666 y: 666");
		}
		{
			areaOrientation[2] = new JTextArea();
			this.add(areaOrientation[2]);
			areaOrientation[2].setBounds(100, 100, 80, 35);
			areaOrientation[2].setText("Orientation 0:\n x: 666 y: 666");
		}
		{
			areaOrientation[3] = new JTextArea();
			this.add(areaOrientation[3]);
			areaOrientation[3].setBounds(10, 55, 80, 35);
			areaOrientation[3].setText("Orientation 0:\n x: 666 y: 666");
		}
		{
			areaDebug = new JTextArea();
			this.add(areaDebug);
			areaDebug.setBounds(5, 155, 275, 100);
			areaDebug.setText("");
		}
	}

	public void updateOrientationAreas(Node currentTile) {
		areaCur.setText("current:\n x: " + currentTile.x + " y: "
				+ currentTile.y);
		for (int i = 0; i < 4; i++) {
			if (currentTile.getNeighbors()[i] != null) {
				areaOrientation[i].setText("Orientation " + i + ":\n x: "
						+ currentTile.getNeighbors()[i].x + " y: "
						+ currentTile.getNeighbors()[i].y);
			} else {
				areaOrientation[i].setText("NULL");
			}
		}
	}

	public void setDebugArea(String message) {
		areaDebug.setText(message);
	}
}
