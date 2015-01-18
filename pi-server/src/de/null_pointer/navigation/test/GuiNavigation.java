package de.null_pointer.navigation.test;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class GuiNavigation extends javax.swing.JFrame {

	private int sizeX = 710;
	private int sizeY = 720;
	private int sizeMapY = 19;
	private int sizeMapX = 19;

	private int phase = 0;

	private DefaultTableModel tableModel = null;
	private CellRenderer renderer = null;
	private JTable table = null;

	private Handler handler = null;

	JButton jBladen;
	JButton jBspeichern;
	JButton jBSimulieren;
	JButton jBfullReset;
	JButton jBreset;

	public GuiNavigation() {
		super();
		initGUI();
		handler = new Handler(this, sizeMapY, sizeMapX);
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			getContentPane().setLayout(null);
			createButtons();

			renderer = new CellRenderer();
			createTable();

			JScrollPane scrollpane = new JScrollPane(table);
			add(scrollpane);
			scrollpane.setBounds(0, 30, sizeX, sizeY - 30);

			pack();
			setSize(sizeX, sizeY);
			this.setLocationRelativeTo(null);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createButtons() {
		{
			jBSimulieren = new JButton();
			getContentPane().add(jBSimulieren);
			jBSimulieren.setText("Simulieren");
			jBSimulieren.setBounds(0, 0, 130, 30);
			jBSimulieren.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					simulatePressed();
				}
			});
		}
		{
			jBspeichern = new JButton();
			getContentPane().add(jBspeichern);
			jBspeichern.setText("Speichern");
			jBspeichern.setBounds(130, 0, 130, 30);
			jBspeichern.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					savePressed();
				}
			});
		}
		{
			jBladen = new JButton();
			getContentPane().add(jBladen);
			jBladen.setText("Laden");
			jBladen.setBounds(260, 0, 130, 30);
			jBladen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					loadPressed();
				}
			});
		}
		{
			jBreset = new JButton();
			getContentPane().add(jBreset);
			jBreset.setText("Reset");
			jBreset.setBounds(sizeX - 150, 0, 130, 30);
			jBreset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					resetPressed();
				}
			});
		}
	}

	private void createTable() {

		tableModel = new DefaultTableModel();
		Object[] columns = new Object[sizeMapX];
		for (int i = 0; i < sizeMapX; i++) {
			columns[i] = "";
		}
		tableModel.setColumnIdentifiers(columns);
		table = new JTable(tableModel);
		TableColumnModel columnModel = table.getColumnModel();

		for (int i = 0; i < sizeMapY; i++) {
			tableModel.insertRow(i, new Object[sizeMapY]);
			if ((i % 2) == 1) {
				table.setRowHeight(i, 60);
				columnModel.getColumn(i).setPreferredWidth(60);
			} else {
				table.setRowHeight(i, 10);
				columnModel.getColumn(i).setPreferredWidth(10);
			}
		}

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setCellSelectionEnabled(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.setEnabled(false);
		table.setDefaultRenderer(Object.class, renderer);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.rowAtPoint(e.getPoint());
					int column = target.columnAtPoint(e.getPoint());
					if (SwingUtilities.isRightMouseButton(e)) {
						handleEvent(row, column, 1);
					} else {
						handleEvent(row, column, 0);
					}
				}
			}
		});
	}

	private void handleEvent(int row, int column, int button) {
		System.out.println("r: " + row + ", c: " + column);
		handler.setValue(row, column, phase, button);
	}

	private void simulatePressed() {
		phase = 1;
		handler.startTimer();
		jBspeichern.setEnabled(false);
		jBladen.setEnabled(false);
	}

	private void resetPressed() {
		if (phase == 0) {
			handler.reset(true);
		} else {
			handler.stopTimer();
			handler.reset(false);
			phase = 0;
			jBspeichern.setEnabled(true);
			jBladen.setEnabled(true);
		}
	}

	private void savePressed() {
		handler.save();
	}

	private void loadPressed() {
		handler.load();
	}

	public void setColor(int row, int col, int value) {
		switch (value) {
		case 0: {
			renderer.setColor(row, col, Color.WHITE);
			break;
		}
		case 1: {
			renderer.setColor(row, col, Color.RED);
			break;
		}
		case 2: {
			renderer.setColor(row, col, Color.BLUE);
			break;
		}
		case -1: {
			renderer.setColor(row, col, Color.DARK_GRAY);
			break;
		}
		}
		this.repaint();
	}

	public void setText(int currentY, int currentX, String string) {
		table.setValueAt(string, currentY, currentX);
	}
}
