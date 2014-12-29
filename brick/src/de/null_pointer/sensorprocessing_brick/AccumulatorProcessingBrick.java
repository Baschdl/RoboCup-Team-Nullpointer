package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import lejos.nxt.Battery;

public class AccumulatorProcessingBrick {

	BrickControlBrick brickControl = null;

	private int milliVolt = -1;

	/**
	 * @param brickControl
	 *            Association to BrickControl
	 */
	public AccumulatorProcessingBrick(BrickControlBrick brickControl) {
		this.brickControl = brickControl;
	}

	/**
	 * processes the reading of the Battery Voltage and sends it to pi server if
	 * it has changed
	 */
	public void processData() {
		int tmpMilliVolt = Battery.getVoltageMilliVolt();
		if (tmpMilliVolt != milliVolt) {
			milliVolt = tmpMilliVolt;
			brickControl.sendData(6, 1, milliVolt);
		}
	}
}
