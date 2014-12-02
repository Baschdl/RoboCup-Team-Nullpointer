package de.null_pointer.sensorprocessing_brick;

import lejos.nxt.I2CPort;
import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.sensor.AbsoluteIMU_ACG;

public class Abs_ImuProcessingBrick {

	private AbsoluteIMU_ACG abs_imu = null;
	private BrickControlBrick brickControl;

	private int[] startGyro = new int[] { 0, 0, 0 };
	private int[] gyro = new int[] { 0, 0, 0 };
	private int angle = 0;
	private int angleExcludingPeriodicity = 0;
	private int deltaAngle = 0;

	// TODO: Richtiger Wert?
	private double sensitivity = 0.00875;

	public Abs_ImuProcessingBrick(BrickControlBrick brickControl, I2CPort port) {
		abs_imu = new AbsoluteIMU_ACG(port);
		startGyro = getGyro();
		this.brickControl = brickControl;
	}

	// TODO:
	public void processData() {
		gyro = getGyro();
		for (int i = 0; i < 3; i++) {
			// Nullpunktfehler korrigieren
			gyro[i] -= startGyro[i];
			deltaAngle += gyro[i] * sensitivity;
		}
		// TODO: Zeitwert anpassen
		angle += deltaAngle / 100;

		angleExcludingPeriodicity = angle % 360;

		for (int i = 0; i < 3; i++) {
			brickControl.sendData(5, 11 + i, gyro[i]);
		}
	}

	public int getFilter() {
		return abs_imu.getFilter();
	}

	public int setFilter(int filterValue) {
		return abs_imu.setFilter(filterValue);
	}

	public int[] getGyro() {
		return abs_imu.getGyro();
	}
}
