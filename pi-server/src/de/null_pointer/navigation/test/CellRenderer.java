package de.null_pointer.navigation.test;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRenderer extends DefaultTableCellRenderer {

	public CellRenderer() {
	}

	private HashMap<Integer, Color> cellData = new HashMap<Integer, Color>();

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		JTextArea label = new JTextArea((String) value);
		int key = ((row + 1) * 1000) + column;
		label.setOpaque(true);
		if (cellData.containsKey(key)) {
			label.setBackground(cellData.get(key));
		} else {
			label.setBackground(Color.WHITE);
		}
		return label;
	}

	public void setColor(int row, int column, Color color) {
		int key = ((row + 1) * 1000) + column;
		cellData.put(key, color);
	}

}
