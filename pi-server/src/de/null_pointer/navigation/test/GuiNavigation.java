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

	private int sizeX = 707;
	private int sizeY = 713;
	private int sizeMapY = 19;
	private int sizeMapX = 19;

	private int phase = 0;
	private boolean timerActive = false;

	private DefaultTableModel tableModel = null;
	private CellRenderer renderer = null;
	private JTable table = null;

	private Handler handler = null;

	JButton jBload;
	JButton jBsave;
	JButton jBSimulate;
	JButton jBfullReset;
	JButton jBreset;
	JButton jBstartStop;

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
			jBSimulate = new JButton();
			getContentPane().add(jBSimulate);
			jBSimulate.setText("Simulate");
			jBSimulate.setBounds(0, 0, 130, 30);
			jBSimulate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					simulatePressed();
				}
			});
		}
		{
			jBsave = new JButton();
			getContentPane().add(jBsave);
			jBsave.setText("Save");
			jBsave.setBounds(130, 0, 130, 30);
			jBsave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					savePressed();
				}
			});
		}
		{
			jBload = new JButton();
			getContentPane().add(jBload);
			jBload.setText("Load");
			jBload.setBounds(260, 0, 130, 30);
			jBload.addActionListener(new ActionListener() {
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
		{
			jBstartStop = new JButton();
			getContentPane().add(jBstartStop);
			jBstartStop.setText("stop");
			jBstartStop.setBounds(sizeX - 250, 0, 100, 30);
			jBstartStop.setVisible(false);
			jBstartStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startStopPressed();
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
						handleMouseClick(row, column, 1);
					} else {
						handleMouseClick(row, column, 0);
					}
				}
			}
		});
	}

	private void handleMouseClick(int row, int column, int button) {
		handler.setValue(row, column, phase, button);
	}

	private void simulatePressed() {
		phase = 1;
		handler.initDialog();

		handler.startTimer();
		timerActive = true;
		jBsave.setEnabled(false);
		jBload.setEnabled(false);
		jBstartStop.setVisible(true);
	}

	private void resetPressed() {
		if (phase == 0) {
			handler.reset(true);
		} else {
			handler.stopTimer();
			handler.killDialog();
			handler.reset(false);
			phase = 0;
			timerActive = false;
			jBstartStop.setVisible(false);
			jBsave.setEnabled(true);
			jBload.setEnabled(true);
		}
	}

	private void savePressed() {
		handler.save();
	}

	private void loadPressed() {
		handler.load();
	}

	private void startStopPressed() {
		if (timerActive) {
			handler.stopTimer();
			timerActive = false;
			jBstartStop.setText("start");
		} else {
			handler.startTimer();
			timerActive = true;
			jBstartStop.setText("stop");
		}
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

	public void setText(int row, int col, String text) {
		table.setValueAt(text, row, col);
	}
}
