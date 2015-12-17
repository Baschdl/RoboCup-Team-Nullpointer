package de.null_pointer.gui;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.sensorprocessing_pi.ThermalSensorProcessingPi;

public class HandleValues extends Thread {

	private JFDisplayValues valueGUI = new JFDisplayValues();
	private LSAProcessingPi lsaprocclass = null;
	private Abs_ImuProcessingPi absimuprocclass = null;
	private EOPDProcessingPi eopdLeftprocclass = null;
	private EOPDProcessingPi eopdRightprocclass = null;
	private DistNxProcessingPi distnxprocclass = null;
	private ThermalSensorProcessingPi thermalSensorprocclass = null;
	private Odometer odometerclass = null;
	private MotorControlPi motorcontrolpi = null;
	private BrickControlPi brickCon1 = null;
	private BrickControlPi brickCon2 = null;

	public HandleValues(LSAProcessingPi lsaprocclass,
			Abs_ImuProcessingPi absimuprocclass,
			EOPDProcessingPi eopdLeftprocclass,
			EOPDProcessingPi eopdRightprocclass,
			DistNxProcessingPi distnxprocclass,
			ThermalSensorProcessingPi thermalSensorprocclass,
			Odometer odometerclass, MotorControlPi motorcontrolpi, BrickControlPi brickCon1, BrickControlPi brickCon2) {
		this.lsaprocclass = lsaprocclass;
		this.absimuprocclass = absimuprocclass;
		this.eopdLeftprocclass = eopdLeftprocclass;
		this.eopdRightprocclass = eopdRightprocclass;
		this.distnxprocclass = distnxprocclass;
		this.thermalSensorprocclass = thermalSensorprocclass;
		this.odometerclass = odometerclass;
		this.motorcontrolpi = motorcontrolpi;
		this.brickCon1 = brickCon1;
		this.brickCon2 = brickCon2;
	}

	public void run() {
		while (true) {

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			readLSARaw();
			readDistNXRaw();
			readRightEOPD();
			readLeftEOPD();
			readAbs_ImuCompass_Angle();
			readAbs_ImuCompass_Heading();
			readThermalSensor();
			readSlopeAngle();
			readOdometer();
			readMotorControlHeading();
			closeConnection();

		}

	}

	private void readLSARaw() {

		valueGUI.showLSARaw(lsaprocclass.getLSA());

	}

	private void readDistNXRaw() {

		valueGUI.showDistNXRaw(distnxprocclass.getDistance());

	}

	private void readRightEOPD() {

		valueGUI.showRightEOPD(eopdRightprocclass.getDistance());

	}

	private void readLeftEOPD() {

		valueGUI.showLeftEOPD(eopdLeftprocclass.getDistance());

	}

	private void readAbs_ImuCompass_Heading() {

		valueGUI.showAbsoluteIMUACG_compass_heading(absimuprocclass
				.getAbsImuHeading());
	}

	private void readAbs_ImuCompass_Angle() {

		valueGUI.showAbsoluteIMUACG_compass_angle(absimuprocclass
				.getAngleHorizontal());

	}

	private void readThermalSensor() {

		valueGUI.showThermalSensor(thermalSensorprocclass.getTemperature());

	}

	private void readSlopeAngle() {

		valueGUI.showSlopeAngle(absimuprocclass.getTiltDataVertical());

	}

	private void readOdometer() {

		valueGUI.showOdometer(odometerclass.getDistanceCounter());

	}

	private void readMotorControlHeading(){
		
		valueGUI.showMotorControlHeading(motorcontrolpi.getRotationHeading());
		
	}
	
	private void closeConnection(){
		
		
		if(valueGUI.getCloseConnection()){
			brickCon1.exitBrick();
			brickCon2.exitBrick();
			valueGUI.setCloseConnection(false);
		}
		
	}
}
