package de.null_pointer.sensor;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;

public class AbsoluteIMU_ACG extends I2CSensor {
	/**
	 * @author Sebastian Bischoff (sebastian@salzreute.de)
	 * 
	 * @param port
	 *            The port where the sensor is plugged in, for example
	 *            "SensorPort.S1"
	 * 
	 * @param address
	 *            The I2C bus address of AbsoluteIMU_ACG
	 */

	public AbsoluteIMU_ACG(I2CPort port, int address) {
		super(port, address, I2CPort.LEGO_MODE, TYPE_LOWSPEED_9V);
	}

	/**
	 * 
	 * @param port
	 * 
	 *            The port where the sensor is plugged, for example
	 *            "SensorPort.S1"
	 */

	public AbsoluteIMU_ACG(I2CPort port) {
		super(port, 0x22 /* DEFAULT_I2C_ADDRESS */, I2CPort.LEGO_MODE,
				TYPE_LOWSPEED);
	}

	private byte[] bufTilt = { -99, -99, -99 };
	private byte[] bufAccelerometer = { -99, -99, -99, -99, -99, -99 };
	private byte[] bufCompass = { -99, -99 };
	private byte[] bufMagneticField = { -99, -99, -99, -99, -99, -99 };
	private byte[] bufGyro = { -99, -99, -99, -99, -99, -99 };
	private byte[] bufFilterValue = { -99 };

	private final static byte COMMAND = 0x41;

	private enum Command {

		CALIBRATE_COMPASS_BEGIN('C'), CALIBRATE_COMPASS_END('c'), SET_SENSITIVITY_2G(
				'1'), SET_SENSITIVITY_4G('2'), SET_SENSITIVITY_8G('3'), SET_SENSITIVITY_16G(
				'4');

		final char value;

		private Command(char value) {
			this.value = value;
		}

	}

	/**
	 * 
	 * @param cmd
	 *            The command which is sent to the AbsoluteIMU_ACG.
	 * 
	 * @return Returns 0 when no error occured and a negative number when an
	 *         error occured.
	 */

	private int sendCommand(Command cmd) {
		return sendData(COMMAND, (byte) cmd.value);
	}

	/**
	 * 
	 * Starts the calibration of the Compass.
	 * 
	 * @return Returns 0 when no error occured and a negative number when an
	 * 
	 *         error occured.
	 */

	public int calibrateCompassBegin() {
		return this.sendCommand(Command.CALIBRATE_COMPASS_BEGIN);
	}

	/**
	 * 
	 * Ends the calibration of the Compass.
	 * 
	 * @return Returns 0 when no error occured and a negative number when an
	 *         error occured.
	 */

	public int calibrateCompassEnd() {
		return this.sendCommand(Command.CALIBRATE_COMPASS_END);
	}

	/**
	 * 
	 * @return Returns 0 when no error occured and a negative number when an 
	 *         error occured.
	 */

	public int setSensitivity2G() {
		return this.sendCommand(Command.SET_SENSITIVITY_2G);
	}

	/**
	 * 
	 * @return Returns 0 when no error occured and a negative number when an
	 *         error occured.
	 */

	public int setSensitivity4G() {
		return this.sendCommand(Command.SET_SENSITIVITY_4G);
	}

	/**
	 * 
	 * @return Returns 0 when no error occured and a negative number when an
	 *         error occured.
	 */

	public int setSensitivity8G() {
		return this.sendCommand(Command.SET_SENSITIVITY_8G);
	}

	/**
	 * 
	 * @return Returns 0 when no error occured and a negative number when an
	 *         error occured.
	 */

	public int setSensitivity16G() {
		return this.sendCommand(Command.SET_SENSITIVITY_16G);
	}

	/**
	 * 
	 * Returns the Tilt data from the Accelerometer
	 * 
	 * @return Array of ints: x-axis-tilt, y-axis-tilt, z-axis-tilt; -1 when an
	 *         error occured
	 */

	public int[] getTiltData() {
		int[] ret = new int[3];
		int err = getData(0x42, bufTilt, 3);

		if (err == 0) {
			for (int i = 0; i < 3; i++) {
				ret[i] = bufTilt[i] & 0xff;
			}
		} else {
			for (int i = 0; i < 3; i++) {
				ret[i] = -1;
			}
		}
		return ret;
	}

	/**
	 * 
	 * Returns the acceleration data from the Accelerometer
	 * 
	 * @return Array of ints: x-axis-acceleration, y-axis-acceleration,
	 *         z-axis-acceleration; -1 when an error occured
	 */

	public int[] getAcceleration() {
		int[] ret = new int[3];
		int err = getData(0x45, bufAccelerometer, 6);

		if (err == 0) {

			int j = 0;
			for (int i = 0; i < 3; i++) {
				ret[i] = (bufAccelerometer[j] & 0xff)
						+ ((bufAccelerometer[j + 1] & 0xff) * 256);
				j = j + 2;
			}
		} else {
			for (int i = 0; i < 3; i++) {
				ret[i] = -1;
			}
		}

		return ret;
	}

	/**
	 * 
	 * Returns the Compass heading from the Compass
	 * 
	 * @return Returns the Compass Heading; -1 when an error occured
	 */

	public int getCompassHeading() {
		int ret = -1;
		int err = getData(0x4B, bufCompass, 2);

		if (err == 0) {
			ret = (bufCompass[0] & 0xff) + ((bufCompass[1] & 0xff) * 256);
		}

		return ret;

	}

	/**
	 * 
	 * Returns the raw Magnetic field from the Accelerometer
	 * 
	 * @return Array of ints: Magnetic field along x-axis, Magnetic field along
	 *         y-axis, Magnetic field along z-axis; -1 when an error occured
	 */

	public int[] getMagneticField() {
		int[] ret = new int[3];
		int err = getData(0x4D, bufMagneticField, 6);

		if (err == 0) {
			int j = 0;
			for (int i = 0; i < 3; i++) {
				ret[i] = (bufMagneticField[j] & 0xff)
						+ ((bufMagneticField[j + 1] & 0xff) * 256);
				j = j + 2;
			}
		} else {
			for (int i = 0; i < 3; i++) {
				ret[i] = -1;
			}
		}
		return ret;
	}

	/**
	 * 
	 * Returns the Gyro data from the Accelerometer
	 * 
	 * @return Array of ints: x-axis Gyro data, y-axis Gyro data, z-axis Gyro
	 *         data; -1 when an error occured
	 */

	public int[] getGyro() {
		int[] ret = new int[3];
		int err = getData(0x53, bufGyro, 6);

		if (err == 0) {
			int j = 0;
			for (int i = 0; i < 3; i++) {
				ret[i] = (bufGyro[j] & 0xff) + ((bufGyro[j + 1] & 0xff) * 256);
				j = j + 2;
			}
		} else {
			for (int i = 0; i < 3; i++) {
				ret[i] = -1;
			}
		}
		return ret;
	}

	public int getFilter() {
		int ret = -1;
		int err = getData(0x5A, bufFilterValue, 1);
		if (err == 0) {
			ret = bufFilterValue[0];
		}
		return ret;
	}

	/**
	 * 
	 * @param newFilterValue
	 *            New filter value between 0 and 7, 4 is the default value. 0
	 *            means no filter (fast), 7 is the highest filter.
	 * 
	 * @return Returns error code, 0 is good, -1 not
	 */

	public int setFilter(int newFilterValue) {
		int ret = -1;

		if (newFilterValue > 7 || newFilterValue < 0) {
			throw new IllegalArgumentException(
					"Filter value MUST be between 0 and 7!");
		} else {
			int err = sendData(0x5A, (byte) newFilterValue);

			if (err == 0) {
				ret = 0;
			}
		}
		return ret;
	}
}
