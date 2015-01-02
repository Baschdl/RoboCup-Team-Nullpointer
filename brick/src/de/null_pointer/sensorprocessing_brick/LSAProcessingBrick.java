package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.sensor.LightSensorArray;
import lejos.nxt.I2CPort;

public class LSAProcessingBrick {

	private LightSensorArray lsa = null;
	private BrickControlBrick brickControl;

	private int[][] values = { { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };
	private int[] midValues = new int[] { -1, -1, -1, -1, -1, -1, -1, -1 };

	/**
	 * @param brickControl
	 *            Association to BrickControl
	 * @param port
	 *            the port the Lightsensor-Array is plugged into
	 */
	public LSAProcessingBrick(BrickControlBrick brickControl, I2CPort port) {
		lsa = new LightSensorArray(port);
		this.brickControl = brickControl;
	}

	/**
	 * processes the sensor-readings, creates middle values of the last 5
	 * Readings and sends them to pi-server if they have changed
	 */
	public void processData() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++) {
				values[i][j] = values[i + 1][j];
			}
		}
		for (int i = 0; i < 8; i++) {
			values[4] = lsa.getLightValues();
		}

		int[] buffer = { 0, 0, 0, 0, 0, 0, 0, 0 };

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 8; j++) {
				buffer[j] += values[i][j];
			}
		}
		for (int i = 0; i < 8; i++) {
			buffer[i] /= 5;
		}
		for (int i = 0; i < 8; i++) {
			if (buffer[i] != midValues[i]) {
				midValues[i] = buffer[i];
				brickControl.sendData(2, i + 1, midValues[i]);
			}
		}
	}

	/**
	 * calibrates black value (should be on black surface)
	 * 
	 * @return 0 if no error occurred, negative number if error occurred
	 */
	public int calibrateBlack() {
		return lsa.calibrateBlack();
	}

	/**
	 * calibrates white value (should be on white surface)
	 * 
	 * @return 0 if no error occurred, negative number if error occurred
	 */
	public int calibrateWhite() {
		return lsa.calibrateWhite();
	}

	/**
	 * sets Lsa Sensor into sleep mode or wakes it up
	 * 
	 * @param sleep
	 *            true: sleep false: wake up
	 * @return 0 if no error occurred, negative number if error occurred
	 */
	public int sleep(boolean sleep) {
		if (sleep) {
			return lsa.sleep();
		} else {
			return lsa.wakeUp();
		}
	}
}
