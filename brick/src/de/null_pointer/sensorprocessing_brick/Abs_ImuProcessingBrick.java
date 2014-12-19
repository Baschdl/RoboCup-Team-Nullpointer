package de.null_pointer.sensorprocessing_brick;

import lejos.nxt.I2CPort;
import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.sensor.AbsoluteIMU_ACG;

public class Abs_ImuProcessingBrick {

	private AbsoluteIMU_ACG abs_imu = null;
	private BrickControlBrick brickControl;

	private int[] startGyro = new int[] {0,0,0};
	private int[] gyro = new int[] {0,0,0};
	private int[] angle = new int[]{0,0,0};
	private int angleExcludingPeriodicity = 0;
	private int[] oldAngleExcludingPeriodicity = new int[]{0,0,0};
	private int[] deltaAngle = new int[]{0,0,0};
	private double time = 0;
	private double[] oldTime = new double[]{0,0,0};
	private int[] oldTiltData = new int[]{0,0,0};

	private double sensitivity = 0.00875;

	/**
	 * @param brickControl
	 * 			Association to BrickControl
	 * @param port
	 * 			the port the Abs_Imu is plugged into
	 */
	public Abs_ImuProcessingBrick(BrickControlBrick brickControl, I2CPort port) {
		abs_imu = new AbsoluteIMU_ACG(port);
		startGyro = getGyro();
		this.brickControl = brickControl;
	}

	/**
	 * Processes GyroData of given Dimension (into an angle) and sends it to pi-server, if it has changed
	 * @param dimension
	 * 			dimension to be processed;
	 * 			0: x
	 * 			1: y
	 * 			2: z
	 */
	public void processData_Angle(int dimension){
		// time since last call gets determined 
		time = System.currentTimeMillis() - oldTime[dimension];
		gyro = getGyro();
		
		// Zero error gets corrected
		gyro[dimension] -= startGyro[dimension];
		
		// seit letzem Messen Ueberschrittenen Winkel bestimmen
		deltaAngle[dimension] += gyro[dimension] * sensitivity;
		
		// zu Vorherigem Winkel wird der in der Vergangenen Zeit ueberschrittene Winkel hinzuaddiert
		angle[dimension] += deltaAngle[dimension] / (1000 / time);
		
		// bilden des Winkels ohne Periodizitaet
		angleExcludingPeriodicity = angle[dimension] % 360;
		
		// neuer Winkel wird nur verschickt, wenn er mindestens 3 Grad vom letzten abweicht
		if(angleExcludingPeriodicity <= oldAngleExcludingPeriodicity[dimension]-3 || angleExcludingPeriodicity >= oldAngleExcludingPeriodicity[dimension]+3){
			brickControl.sendData(5, dimension + 16, angleExcludingPeriodicity);
			oldAngleExcludingPeriodicity[dimension] = angleExcludingPeriodicity;
		}
		oldTime[dimension] = System.currentTimeMillis();
	}
	
	/**
	 * Processes TiltData of given dimension and sends it to pi-server, if it has changed
	 * @param dimension
	 * 			dimension to be processed;
	 * 			0: x
	 * 			1: y
	 * 			2: z
	 */
	public void processData_TiltData(int dimension){
		int[] TiltData = abs_imu.getTiltData();
		// neue TiltData wird nur verschickt, wenn sie vom letzten Wert abweicht
		if(TiltData[dimension] < oldTiltData[dimension] || TiltData[dimension] > oldTiltData[dimension]){
			brickControl.sendData(5, dimension + 11, TiltData[dimension]);
			oldTiltData[dimension] = TiltData[dimension];
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
