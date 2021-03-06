package de.null_pointer.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private JLabel lblThermalsensor;
	private JLabel jLThermalSensor;
	private JLabel lblSteigungswinkel;
	private JLabel jLSlopeAngle;
	private JLabel lblDistancecounter;
	private JLabel jLOdometer;
	private JButton btnCloseConnection;
	private JLabel lblMotorcontrolheading;
	private JLabel jLMotorControlHeading;
	private boolean closeConnection = false;

	/**
	 * Create the frame.
	 */
	public JFDisplayValues() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 545, 395);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("LSA Raw:");
		lblNewLabel.setBounds(10, 11, 77, 14);
		getContentPane().add(lblNewLabel);

		jLLSA = new JLabel("--");
		jLLSA.setBounds(97, 11, 248, 14);
		getContentPane().add(jLLSA);

		JLabel lblDistnxraw = new JLabel("DistNX-Raw:");
		lblDistnxraw.setBounds(10, 36, 77, 14);
		getContentPane().add(lblDistnxraw);

		jLDistNX = new JLabel("--");
		jLDistNX.setBounds(96, 36, 83, 14);
		getContentPane().add(jLDistNX);

		lblEopdRechts = new JLabel("EOPD right:");
		lblEopdRechts.setBounds(10, 61, 77, 14);
		getContentPane().add(lblEopdRechts);

		jLEOPDright = new JLabel("--");
		jLEOPDright.setBounds(97, 61, 126, 14);
		getContentPane().add(jLEOPDright);

		lblEopdLinks = new JLabel("EOPD left:");
		lblEopdLinks.setBounds(218, 61, 74, 14);
		getContentPane().add(lblEopdLinks);

		jLEOPDleft = new JLabel("--");
		jLEOPDleft.setBounds(302, 61, 142, 14);
		getContentPane().add(jLEOPDleft);

		lblAbsoluteimuacgKompassHeading = new JLabel(
				"AbsoluteIMU-ACG Compass Heading:");
		lblAbsoluteimuacgKompassHeading.setBounds(10, 86, 272, 14);
		getContentPane().add(lblAbsoluteimuacgKompassHeading);

		jLAbsoluteIMU_ACG_Compass_Heading = new JLabel("--");
		jLAbsoluteIMU_ACG_Compass_Heading.setBounds(302, 86, 43, 14);
		getContentPane().add(jLAbsoluteIMU_ACG_Compass_Heading);

		lblAbsoluteimuacgKompassWinkel = new JLabel(
				"AbsoluteIMU-ACG Compass Angle:");
		lblAbsoluteimuacgKompassWinkel.setBounds(10, 111, 272, 14);
		getContentPane().add(lblAbsoluteimuacgKompassWinkel);

		jLAbsoluteIMU_ACG_Compass_Angle = new JLabel("--");
		jLAbsoluteIMU_ACG_Compass_Angle.setBounds(302, 111, 43, 14);
		getContentPane().add(jLAbsoluteIMU_ACG_Compass_Angle);
		
		lblThermalsensor = new JLabel("ThermalSensor:");
		lblThermalsensor.setBounds(10, 136, 138, 14);
		contentPane.add(lblThermalsensor);
		
		jLThermalSensor = new JLabel("--");
		jLThermalSensor.setBounds(158, 136, 97, 14);
		contentPane.add(jLThermalSensor);
		
		lblSteigungswinkel = new JLabel("slope-angle");
		lblSteigungswinkel.setBounds(10, 161, 138, 14);
		contentPane.add(lblSteigungswinkel);
		
		jLSlopeAngle = new JLabel("--");
		jLSlopeAngle.setBounds(158, 161, 97, 14);
		contentPane.add(jLSlopeAngle);
		
		lblDistancecounter = new JLabel("DistanceCounter:");
		lblDistancecounter.setBounds(10, 186, 138, 14);
		contentPane.add(lblDistancecounter);
		
		jLOdometer = new JLabel("--");
		jLOdometer.setBounds(158, 186, 46, 14);
		contentPane.add(jLOdometer);
		
		btnCloseConnection = new JButton("Close Connection");
		btnCloseConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				closeConnection = true;
			}
		});
		btnCloseConnection.setBounds(365, 322, 154, 23);
		contentPane.add(btnCloseConnection);
		
		lblMotorcontrolheading = new JLabel("MotorControl-Heading:");
		lblMotorcontrolheading.setBounds(10, 211, 143, 14);
		contentPane.add(lblMotorcontrolheading);
		
		jLMotorControlHeading = new JLabel("--");
		jLMotorControlHeading.setBounds(158, 211, 46, 14);
		contentPane.add(jLMotorControlHeading);

		this.setVisible(true);
	}

	public void showLSARaw(int[] lsavalues) {

		jLLSA.setText(Arrays.toString(lsavalues));

	}

	public void showDistNXRaw(int distValue) {

		jLDistNX.setText(Integer.toString(distValue));

	}

	public void showRightEOPD(double value) {

		jLEOPDright.setText(Double.toString(Math.round(value*100)/100.0));

	}

	public void showLeftEOPD(double value) {

		jLEOPDleft.setText(Double.toString(Math.round(value*100)/100.0));

	}

	public void showAbsoluteIMUACG_compass_heading(int value) {

		jLAbsoluteIMU_ACG_Compass_Heading.setText(Integer.toString(value));

	}

	public void showAbsoluteIMUACG_compass_angle(int value) {

		jLAbsoluteIMU_ACG_Compass_Angle.setText(Integer.toString(value));

	}
	
	public void showThermalSensor(int value){
		
		jLThermalSensor.setText(Integer.toString(value));
		
	}
	
	public void showSlopeAngle(int value){
		
		jLSlopeAngle.setText(Integer.toString(value));
		
	}
	
	public void showOdometer(double value){
		
		jLOdometer.setText(Double.toString(value));
		
	}
	
	public void showMotorControlHeading(int value){
		
		jLMotorControlHeading.setText(Integer.toString(value));
		
	}
	
	public boolean getCloseConnection(){
		
		return closeConnection;
		
	}
	
	public void setCloseConnection(boolean value){
		
		closeConnection = value;
		
	}
}
