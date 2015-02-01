package de.null_pointer.sensorprocessing_brick;

import lejos.nxt.SensorPort;
import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.sensor.DThermalIR;

public class ThermalSensorProcessingBrick {

	private DThermalIR thermal = null;
	private BrickControlBrick brickControl = null;

	private int[] values = { 0, 0, 0, 0, 0 };
	private int midValue = -1;

	/**
	 * @param brickControl
	 *            Association to BrickControl
	 * @param port
	 *            the port the Abs_Imu is plugged into
	 */
	public ThermalSensorProcessingBrick(BrickControlBrick brickControl,
			SensorPort port) {
		thermal = new DThermalIR(port);
		this.brickControl = brickControl;
	}

	/**
	 * processes the sensor-readings, creates a middle value of the last 5
	 * Readings and sends it to pi-server if it has changed
	 */
	public void processData() {
		for (int i = 0; i < 4; i++) {
			values[i] = values[i + 1];
		}
		values[4] = Math.round(thermal.readObject());
		int buffer = 0;
		for (int i = 0; i < 5; i++) {
			buffer += values[i];
		}
		buffer /= 5;
		if (buffer != midValue) {
			midValue = buffer;
			brickControl.sendData(8, 1, midValue);
		}
	}
}
