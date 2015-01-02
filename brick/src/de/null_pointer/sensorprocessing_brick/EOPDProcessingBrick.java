package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import lejos.nxt.ADSensorPort;
import lejos.nxt.addon.EOPD;

public class EOPDProcessingBrick {

	private EOPD eopd;
	private BrickControlBrick brickControl;
	private int sensorID;

	private int[] values = { 0, 0, 0, 0, 0 };
	private int midValue = -1;

	/**
	 * @param brickControl
	 *            Association to BrickControl
	 * @param sensorID
	 *            ID of the EOPD sensor; decides wether it is the right or the
	 *            left one
	 * @param port
	 *            the port the EOPD sensor is plugged into
	 * @param longRange
	 *            defines if the EOPD operates in longRange-mode
	 */
	public EOPDProcessingBrick(BrickControlBrick brickControl,
			ADSensorPort port, int sensorID, boolean longRange) {
		eopd = new EOPD(port, longRange);
		this.brickControl = brickControl;
		this.sensorID = sensorID;
	}

	/**
	 * changes the EOPDs mode to either long- or shortRange
	 * 
	 * @param longRange
	 *            true for longRange; false for shortRange
	 */
	public void setLongRange(boolean longRange) {
		if (longRange) {
			eopd.setModeLong();
		} else {
			eopd.setModeShort();
		}
	}

	/**
	 * processes the sensor-readings, creates a middle value of the last 5
	 * Readings and sends it if it has changed
	 */
	public void processData() {
		for (int i = 0; i < 4; i++) {
			values[i] = values[i + 1];
		}
		values[4] = eopd.readRawValue();
		int buffer = 0;
		for (int i = 0; i < 5; i++) {
			buffer += values[i];
		}
		buffer /= 5;
		if (buffer != midValue) {
			midValue = buffer;
			brickControl.sendData(sensorID, 1, midValue);
		}
	}
}
