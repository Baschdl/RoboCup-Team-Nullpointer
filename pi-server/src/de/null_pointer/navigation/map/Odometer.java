package de.null_pointer.navigation.map;

import java.util.Properties;

import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.AccumulatorProcessingPi;

public class Odometer {

	private AccumulatorProcessingPi accumulator = null;
	private Abs_ImuProcessingPi abs_imu = null;

	Properties propPiServer = null;

	private double distanceCounter = 0;

	private double wheelRadius = 0;
	private int milliVolt = 0;
	private int angle;

	private double tmpDistance = 0;

	public Odometer(AccumulatorProcessingPi accumulator,
			Abs_ImuProcessingPi abs_imu, Properties propPiServer) {
		this.accumulator = accumulator;
		this.abs_imu = abs_imu;

		wheelRadius = Double.parseDouble(propPiServer
				.getProperty("Navigation.Odometer.wheelRadius"));
	}

	/**
	 * Calculates distance traveled in the given time period and adds it to the
	 * distance Counter
	 * 
	 * @param time
	 *            time in milliseconds
	 * @param currentSpeed
	 *            current speed of the Robot in degrees per second
	 */
	public void calculateDistance(double time, int currentSpeed) {
		milliVolt = accumulator.getMilliVolt();
		angle = deviationAngle();

		// TODO Batteriestatus beachten
		tmpDistance = (currentSpeed / 360) * 2 * Math.PI * wheelRadius
				* (time / 1000);
		distanceCounter += tmpDistance * Math.cos(angle);

	}

	/**
	 * calculates the deviation from the ideal angle
	 * 
	 * @return int
	 */
	private int deviationAngle() {
		int heading = abs_imu.getHeading();
		int angle = abs_imu.getAngleHorizontal();

		switch (heading) {
		case 0:
			if (angle <= 45) {
				return 45;
			} else {
				return (360 - angle);
			}
		case 1:
			if (angle <= 90) {
				return (90 - angle);
			} else {
				return (angle - 90);
			}
		case 2:
			if (angle <= 180) {
				return (180 - angle);
			} else {
				return (angle - 180);
			}
		case 4:
			if (angle <= 270) {
				return (270 - angle);
			} else {
				return (angle - 270);
			}
		default:
			break;
		}
		return 0;
	}

	public double getDistanceCounter() {
		return distanceCounter;
	}

	public void setDistanceCounteR(int value) {
		distanceCounter = value;
	}

	public void resetDistanceCounter() {
		distanceCounter = 0;
	}

}
