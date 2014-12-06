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
	private int oldAngleExcludingPeriodicity = 0;
	private int deltaAngle = 0;
	private int horizontalDimension = -1;
	private double time = 0;
	private double oldTime = 0;

	// TODO: Richtiger Wert?
	private double sensitivity = 0.00875;

	/**
	 * @param brickControl
	 * 			Association to BrickControl
	 * @param port
	 * 			the port the Abs_Imu is plugged into
	 * @param horizontalDimension 
	 * 			0-2; Number of the horizontal Dimension
	 */
	public Abs_ImuProcessingBrick(BrickControlBrick brickControl, I2CPort port, int horizontalDimension) {
		abs_imu = new AbsoluteIMU_ACG(port);
		startGyro = getGyro();
		this.brickControl = brickControl;
		this.horizontalDimension = horizontalDimension;
	}

	public void processData(){
		time = System.currentTimeMillis() - oldTime;
		gyro = getGyro();
		
		// Nullpunktfehler korrigieren
		gyro[horizontalDimension] -= startGyro[horizontalDimension];
		deltaAngle += gyro[horizontalDimension] * sensitivity;
		
		angle += deltaAngle / (1000 / time);

		angleExcludingPeriodicity = angle % 360;
		
		//neuer Winkel wird nur verschickt, wenn er mindestens 3 Grad vom letzten abweicht
		if(angleExcludingPeriodicity <= oldAngleExcludingPeriodicity-3 || angleExcludingPeriodicity >= oldAngleExcludingPeriodicity+3){
			brickControl.sendData(5, 16, angleExcludingPeriodicity);
		}
		oldTime = System.currentTimeMillis();
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
