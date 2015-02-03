package de.null_pointer.sensorprocessing_pi;

import java.util.Properties;

public class Abs_ImuProcessingPi {

	private int dimension_horizontal = -1;
	private int dimension_vertical = -1;
	private int dimension_rotational = -1;

	private int angle[] = { 0, 0, 0 };
	/**
	 * 0 = N, 1 = O, 2 = S, 3 = W
	 */
	private int heading = 0;
	private int tiltData[] = { 0, 0, 0 };

	private final Object lockAngle = new Object();
	private final Object lockTiltData = new Object();

	public Abs_ImuProcessingPi(Properties propPiServer) {
		dimension_horizontal = Integer
				.parseInt(propPiServer
						.getProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_horizontal"));
		dimension_vertical = Integer
				.parseInt(propPiServer
						.getProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_vertical"));
		dimension_rotational = Integer
				.parseInt(propPiServer
						.getProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_rotational"));
	}

	public void setAngle(int angle, int dimension) {
		synchronized (lockAngle) {
			this.angle[dimension] = angle;

			if (dimension == dimension_horizontal) {
				if (angle >= 0) {
					if (angle < 45 && angle > 315) {
						heading = 0;
					} else if (angle < 135 && angle > 45) {
						heading = 1;
					} else if (angle < 225 && angle > 135) {
						heading = 2;
					} else {
						heading = 3;
					}
				} else {
					if (angle < -45 && angle > -315) {
						heading = 0;
					} else if (angle < -135 && angle > -45) {
						heading = 2;
					} else if (angle < -225 && angle > -135) {
						heading = 1;
					} else {
						heading = 3;
					}
				}
			}
		}
	}

	public int getAngleHorizontal() {
		synchronized (lockAngle) {
			return angle[dimension_horizontal];
		}
	}

	public int getAngleVertical() {
		synchronized (lockAngle) {
			return angle[dimension_vertical];
		}
	}

	public int getAngleRotational() {
		synchronized (lockAngle) {
			return angle[dimension_rotational];
		}
	}

	public void setTiltData(int tiltData, int dimension) {
		synchronized (lockTiltData) {
			this.tiltData[dimension] = tiltData;
		}
	}

	public int getTiltDataVertical() {
		synchronized (lockTiltData) {
			return tiltData[dimension_vertical];
		}
	}

	/**
	 * returns the current heading of the robot
	 * 
	 * @return 0 = N, 1 = O, 2 = S, 3 = W
	 */
	public int getHeading() {
		synchronized (lockAngle) {
			return heading;
		}
	}

	public void setTestAngleAndHeading(int angle, int heading) {

	}

}
