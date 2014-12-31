package de.null_pointer.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class JFDisplayValues extends JFrame {

	private JPanel contentPane;
	public JLabel jLLSA;
	public JLabel jLDistNX;
	public JLabel lblEopdRechts;
	public JLabel jLEOPDright;
	public JLabel lblEopdLinks;
	public JLabel jLEOPDleft;
	public JLabel lblAbsoluteimuacgKompassHeading;
	public JLabel jLAbsoluteIMU_ACG_Compass_Heading;
	private JLabel lblAbsoluteimuacgKompassWinkel;
	private JLabel jLAbsoluteIMU_ACG_Compass_Angle;

	/**
	 * Create the frame.
	 */
	public JFDisplayValues() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("LSA Raw:");
		lblNewLabel.setBounds(10, 11, 54, 14);
		getContentPane().add(lblNewLabel);

		jLLSA = new JLabel("--");
		jLLSA.setBounds(83, 11, 82, 14);
		getContentPane().add(jLLSA);

		JLabel lblDistnxraw = new JLabel("DistNX-Raw:");
		lblDistnxraw.setBounds(10, 36, 66, 14);
		getContentPane().add(lblDistnxraw);

		jLDistNX = new JLabel("--");
		jLDistNX.setBounds(82, 36, 83, 14);
		getContentPane().add(jLDistNX);

		lblEopdRechts = new JLabel("EOPD rechts:");
		lblEopdRechts.setBounds(10, 61, 66, 14);
		getContentPane().add(lblEopdRechts);

		jLEOPDright = new JLabel("--");
		jLEOPDright.setBounds(82, 61, 126, 14);
		getContentPane().add(jLEOPDright);

		lblEopdLinks = new JLabel("EOPD links:");
		lblEopdLinks.setBounds(218, 61, 54, 14);
		getContentPane().add(lblEopdLinks);

		jLEOPDleft = new JLabel("--");
		jLEOPDleft.setBounds(282, 61, 142, 14);
		getContentPane().add(jLEOPDleft);

		lblAbsoluteimuacgKompassHeading = new JLabel(
				"AbsoluteIMU-ACG Kompass Ausrichtung:");
		lblAbsoluteimuacgKompassHeading.setBounds(10, 86, 198, 14);
		getContentPane().add(lblAbsoluteimuacgKompassHeading);

		jLAbsoluteIMU_ACG_Compass_Heading = new JLabel("--");
		jLAbsoluteIMU_ACG_Compass_Heading.setBounds(218, 86, 159, 14);
		getContentPane().add(jLAbsoluteIMU_ACG_Compass_Heading);

		lblAbsoluteimuacgKompassWinkel = new JLabel(
				"AbsoluteIMU-ACG Kompass Winkel:");
		lblAbsoluteimuacgKompassWinkel.setBounds(10, 111, 169, 14);
		getContentPane().add(lblAbsoluteimuacgKompassWinkel);

		jLAbsoluteIMU_ACG_Compass_Angle = new JLabel("--");
		jLAbsoluteIMU_ACG_Compass_Angle.setBounds(189, 111, 177, 14);
		getContentPane().add(jLAbsoluteIMU_ACG_Compass_Angle);

		this.setVisible(true);
	}

	public void showLSARaw(int[] lsavalues) {

		jLLSA.setText(Arrays.toString(lsavalues));

	}

	public void showDistNXRaw(int distValue) {

		jLDistNX.setText(Integer.toString(distValue));

	}

	public void showRightEOPD(double value) {

		jLEOPDright.setText(Double.toString(value));

	}

	public void showLeftEOPD(double value) {

		jLEOPDleft.setText(Double.toString(value));

	}

	public void showAbsoluteIMUACG_compass_heading(int value) {

		jLAbsoluteIMU_ACG_Compass_Heading.setText(Integer.toString(value));

	}

	public void showAbsoluteIMUACG_compass_angle(int value) {

		jLAbsoluteIMU_ACG_Compass_Heading.setText(Integer.toString(value));

	}
}
