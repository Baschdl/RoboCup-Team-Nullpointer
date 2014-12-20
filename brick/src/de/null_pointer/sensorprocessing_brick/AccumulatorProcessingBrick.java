package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import lejos.nxt.Battery;

public class AccumulatorProcessingBrick {

	BrickControlBrick brickControl = null;

	private int milliVolt = -1;

	public AccumulatorProcessingBrick(BrickControlBrick brickControl) {
		this.brickControl = brickControl;
	}

	public void processData() {
		int tmpMilliVolt = Battery.getVoltageMilliVolt();
		if (tmpMilliVolt != milliVolt) {
			milliVolt = tmpMilliVolt;
			brickControl.sendData(6, 1, milliVolt);
		}
	}
}
