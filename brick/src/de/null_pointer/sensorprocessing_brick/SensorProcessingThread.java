package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;

public class SensorProcessingThread extends Thread {

	private BrickControlBrick brickControl = null;
	private Abs_ImuProcessingBrick abs_imu = null;
	private AccumulatorProcessingBrick accumulator = null;
	private DistNxProcessingBrick distnx = null;
	private EOPDProcessingBrick leftEOPD = null;
	private EOPDProcessingBrick rightEOPD = null;
	private LSAProcessingBrick lsa = null;
	private ThermalSensorProcessingBrick thermal = null;

	public SensorProcessingThread(BrickControlBrick brickControl,
			Abs_ImuProcessingBrick abs_imu,
			AccumulatorProcessingBrick accumulator,
			DistNxProcessingBrick distnx, EOPDProcessingBrick leftEOPD,
			EOPDProcessingBrick rightEOPD, LSAProcessingBrick lsa,
			ThermalSensorProcessingBrick thermal) {
		this.brickControl = brickControl;
		this.abs_imu = abs_imu;
		this.accumulator = accumulator;
		this.distnx = distnx;
		this.leftEOPD = leftEOPD;
		this.rightEOPD = rightEOPD;
		this.lsa = lsa;
		this.thermal = thermal;
	}

	public void run() {
		int counter = 0;
		while (true) {
			if (abs_imu != null) {
				// abs_imu.processData_Angle();
				abs_imu.processData_TiltData();
			}
			if (accumulator != null) {
				accumulator.processData();
			}
			if (distnx != null) {
				distnx.processData();
			}
			if (leftEOPD != null) {
				leftEOPD.processData();
			}
			if (rightEOPD != null) {
				rightEOPD.processData();
			}
			if (lsa != null) {
				lsa.processData();
			}
			if (thermal != null) {
				thermal.processData();
			}

			if (counter < 10) {
				counter++;
			} else if (counter == 10) {
				brickControl.sendData(7, 1, 1);
				counter++;
			}
		}
	}
}
