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
	 * @param dimension
	 * 			0-2; Dimension which will be processed
	 */
	public void processData(int dimension){
		// Die Zeit seit dem letzten Aufruf wird ermittelt
		time = System.currentTimeMillis() - oldTime[dimension];
		gyro = getGyro();
		
		// Nullpunktfehler korrigieren
		gyro[dimension] -= startGyro[dimension];
		
		//seit letzem Messen Ueberschrittenen Winkel bestimmen
		deltaAngle[dimension] += gyro[dimension] * sensitivity;
		
		// zu Vorherigem Winkel wird der in der Vergangenen Zeit ueberschrittene Winkel hinzuaddiert
		angle[dimension] += deltaAngle[dimension] / (1000 / time);
		
		// bilden des Winkels ohne Periodizitaet
		angleExcludingPeriodicity = angle[dimension] % 360;
		
		// neuer Winkel wird nur verschickt, wenn er mindestens 3 Grad vom letzten abweicht
		if(angleExcludingPeriodicity <= oldAngleExcludingPeriodicity[dimension]-3 || angleExcludingPeriodicity >= oldAngleExcludingPeriodicity[dimension]+3){
			brickControl.sendData(5, 16, angleExcludingPeriodicity);
			oldAngleExcludingPeriodicity[dimension] = angleExcludingPeriodicity;
		}
		oldTime[dimension] = System.currentTimeMillis();
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
