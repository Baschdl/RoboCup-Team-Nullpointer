package de.null_pointer.sensorprocessing_pi;

public class Abs_ImuProcessingPi {

	public Abs_ImuProcessingPi(int dimension_horizontal,int dimension_vertical, int dimension_rotational) {
		this.dimension_horizontal = dimension_horizontal;
		this.dimension_vertical = dimension_vertical;
		this.dimension_rotational = dimension_rotational;
	}

	private int dimension_horizontal = -1;
	private int dimension_vertical = -1;
	private int dimension_rotational = -1;

	private int angle[] = { 0, 0, 0 };
	private int heading = 0; // 0 = N, 1 = O, 2 = S, 3 = W

	public void setAngle(int angle, int dimension) {
		this.angle[dimension] = angle;

		if (dimension == dimension_horizontal) {
			if (angle < 45 && angle > 315) {
				heading = 0;
			} else if (angle < 135 && angle > 45) {
				heading = 1;
			} else if (angle < 225 && angle > 135) {
				heading = 2;
			} else {
				heading = 3;
			}
		}
	}

	public int getAngleHorizontal() {
		return angle[dimension_horizontal];
	}

	public int getAngleVertical() {
		return angle[dimension_vertical];
	}

	public int getAngleRotational() {
		return angle[dimension_rotational];
	}

	public int getHeading() {
		return heading;
	}
}
