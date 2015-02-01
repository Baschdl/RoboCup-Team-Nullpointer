package de.null_pointer.navigation.test;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
	JButton jBstep;

	JTextField jTFtimerspeed;
	JLabel jLtimertext;

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
			createTextField();

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
			jBsave.setBounds(sizeX - 276, 0, 80, 30);
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
			jBload.setBounds(sizeX - 196, 0, 80, 30);
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
			jBreset.setBounds(sizeX - 116, 0, 100, 30);
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
			jBstartStop.setBounds(130, 0, 100, 30);
			jBstartStop.setVisible(false);
			jBstartStop.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					startStopPressed();
				}
			});
		}
		{
			jBstep = new JButton();
			getContentPane().add(jBstep);
			jBstep.setText("step");
			jBstep.setBounds(230, 0, 100, 30);
			jBstep.setVisible(false);
			jBstep.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					stepPressed();
				}
			});
		}
	}

	private void createTextField() {
		{
			jTFtimerspeed = new JTextField();
			getContentPane().add(jTFtimerspeed);
			jTFtimerspeed.setText("500");
			jTFtimerspeed.setHorizontalAlignment(SwingConstants.CENTER);
			jTFtimerspeed.setBounds(260, 5, 40, 20);
			jTFtimerspeed.setVisible(true);
		}
		{
			jLtimertext = new JLabel();
			getContentPane().add(jLtimertext);
			jLtimertext.setText("Zeitabstand in ms:");
			jLtimertext.setHorizontalAlignment(SwingConstants.CENTER);
			jLtimertext.setBounds(130, 0, 130, 30);
			jLtimertext.setVisible(true);
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
		handler.setValue(row, column, button);
	}

	private void simulatePressed() {
		phase = 1;
		handler.initDialog();

		jTFtimerspeed.setVisible(false);
		jLtimertext.setVisible(false);
		handler.newTimer(Integer.parseInt(jTFtimerspeed.getText()));
		handler.startTimer();
		timerActive = true;
		jBstartStop.setText("stop");
		jBsave.setEnabled(false);
		jBload.setEnabled(false);
		jBstartStop.setVisible(true);
		jBstep.setVisible(true);
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
			jBstep.setVisible(false);
			jTFtimerspeed.setVisible(true);
			jLtimertext.setVisible(true);
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

	private void stepPressed() {
		handler.simulate();
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
		case 3: {
			renderer.setColor(row, col, Color.GREEN);
			break;
		}
		case -1: {
			renderer.setColor(row, col, Color.DARK_GRAY);
			break;
		}
		case -2: {
			renderer.setColor(row, col, Color.BLACK);
			break;
		}
		default: {
			renderer.setColor(row, col, Color.YELLOW);
			break;
		}
		}
		this.repaint();
	}

	public void setText(int row, int col, String text) {
		table.setValueAt(text, row, col);
	}

	public int getValueAt(int row, int col) {
		int value = 0;
		Color color = renderer.getColor(row, col);
		if (color == Color.RED) {
			value = 1;
		} else if (color == Color.BLUE) {
			value = 2;
		} else if (color == Color.GREEN) {
			value = 3;
		} else if (color == Color.DARK_GRAY) {
			value = -1;
		} else if (color == Color.BLACK) {
			value = -2;
		} else if (color == Color.YELLOW) {
			value = 4;
		}
		return value;
	}
}
