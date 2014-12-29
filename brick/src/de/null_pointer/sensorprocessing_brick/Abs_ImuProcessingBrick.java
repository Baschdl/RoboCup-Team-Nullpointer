package de.null_pointer.sensorprocessing_brick;

import lejos.nxt.I2CPort;
import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.sensor.AbsoluteIMU_ACG;

public class Abs_ImuProcessingBrick {

	private AbsoluteIMU_ACG abs_imu = null;
	private BrickControlBrick brickControl;

	// TODO: noch anpassen !
	private int dimension_horizontal = 0;
	private int dimension_vertical = 1;

	private int[] startGyro = new int[] { 0, 0, 0 };
	private int[] gyro = new int[] { 0, 0, 0 };
	private int[] angle = new int[] { 0, 0, 0 };
	private int angleExcludingPeriodicity = 0;
	private int[] oldAngleExcludingPeriodicity = new int[] { 0, 0, 0 };
	private int[] deltaAngle = new int[] { 0, 0, 0 };
	private double time = 0;
	private double[] oldTime = new double[] { 0, 0, 0 };
	private int[] oldTiltData = new int[] { 0, 0, 0 };

	private double sensitivity = 0.00875;

	/**
	 * @param brickControl
	 *            Association to BrickControl
	 * @param port
	 *            the port the Abs_Imu is plugged into
	 */
	public Abs_ImuProcessingBrick(BrickControlBrick brickControl, I2CPort port) {
		abs_imu = new AbsoluteIMU_ACG(port);
		startGyro = getGyro();
		this.brickControl = brickControl;
	}

	/**
	 * Processes GyroData of given Dimension (into an angle) and sends it to
	 * pi-server, if it has changed
	 * 
	 * @param dimension_horizontal
	 *            dimension to be processed; 0:x, 1:y, 2:z
	 */
	public void processData_Angle() {
		// time since last call gets determined
		time = System.currentTimeMillis() - oldTime[dimension_horizontal];
		gyro = getGyro();

		// Zero error gets corrected
		gyro[dimension_horizontal] -= startGyro[dimension_horizontal];

		// new angle gets calculated
		deltaAngle[dimension_horizontal] += gyro[dimension_horizontal]
				* sensitivity;
		angle[dimension_horizontal] += deltaAngle[dimension_horizontal]
				/ (1000 / time);

		// angleExcludingPeriodicity gets calculated
		angleExcludingPeriodicity = angle[dimension_horizontal] % 360;

		// new angle will only be sent away, if it has changed by +- 3 Degrees
		if (angleExcludingPeriodicity <= oldAngleExcludingPeriodicity[dimension_horizontal] - 3
				|| angleExcludingPeriodicity >= oldAngleExcludingPeriodicity[dimension_horizontal] + 3) {
			brickControl.sendData(5, dimension_horizontal + 16,
					angleExcludingPeriodicity);
			oldAngleExcludingPeriodicity[dimension_horizontal] = angleExcludingPeriodicity;
		}
		oldTime[dimension_horizontal] = System.currentTimeMillis();
	}

	/**
	 * Processes TiltData of given dimension and sends it to pi-server, if it
	 * has changed
	 * 
	 * @param dimension_vertical
	 *            dimension to be processed; 0:x, 1:y, 2:z
	 */
	public void processData_TiltData() {
		int[] TiltData = abs_imu.getTiltData();
		// new TiltData will only be sent away, if it has changed
		if (TiltData[dimension_vertical] < oldTiltData[dimension_vertical]
				|| TiltData[dimension_vertical] > oldTiltData[dimension_vertical]) {
			brickControl.sendData(5, dimension_vertical + 11,
					TiltData[dimension_vertical]);
			oldTiltData[dimension_vertical] = TiltData[dimension_vertical];
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
