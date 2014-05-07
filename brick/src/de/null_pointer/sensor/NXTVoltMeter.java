package de.null_pointer.sensor;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;

/**
 * @author Sebastian Bischoff (sebastian@salzreute.de)
 * 
 */
public class NXTVoltMeter extends I2CSensor {

	private byte[] bufAbsoluteVoltage = new byte[2];
	private byte[] bufRelativeVoltage = new byte[2];
	private byte[] bufReferenceVoltage = new byte[2];

	private final static byte COMMAND = 0x41;

	public enum Command {
		ChangeReferenceVoltage('d');

		private Command(char value) {
			this.value = value;
		}

		final char value;
	}

	/**
	 * 
	 * @param port
	 *            The port where the sensor is plugged, for example
	 *            "SensorPort.S1"
	 * @param address
	 *            The I2C bus address of NXTVoltMeter
	 */

	public NXTVoltMeter(I2CPort port, int address) {
		super(port, address, I2CPort.LEGO_MODE, TYPE_LOWSPEED_9V);
	}

	/**
	 * 
	 * @param port
	 *            The port where the sensor is plugged, like "SensorPort.S1"
	 */

	public NXTVoltMeter(I2CPort port) {
		this(port, 0x26 /* DEFAULT_I2C_ADDRESS */);
	}

	/**
	 * 
	 * @param cmd
	 *            The command which is sent to the NXTVoltMeter.
	 * @return Returns 0 when no error occured and a negative number when an
	 *         error occured.
	 */

	public int sendCommand(Command cmd) {
		return sendData(COMMAND, (byte) cmd.value);
	}

	/**
	 * Gets the absolute value from the NXTVoltMeter
	 * 
	 * @return int value
	 */
	public int getAbsoluteVoltage() {
		int ret = -99;

		int err = getData(0x43, bufAbsoluteVoltage, 2);
		if (err == 0) {
			ret = bufAbsoluteVoltage[0] & 0xff + bufAbsoluteVoltage[1] & 0xff;
		} else {
			ret = -1;
		}

		return ret;
	}

	/**
	 * Gets the relative value from the NXTVoltMeter
	 * 
	 * @return int value
	 */
	public int getRelativeVoltage() {
		int ret = -99;

		int err = getData(0x45, bufRelativeVoltage, 2);
		if (err == 0) {
			ret = bufRelativeVoltage[0] & 0xff + bufRelativeVoltage[1] & 0xff;
		} else {
			ret = -1;
		}

		return ret;
	}

	/**
	 * Gets the reference value from the NXTVoltMeter
	 * 
	 * @return int value
	 */
	public int getReferenceVoltage() {
		int ret = -99;

		int err = getData(0x47, bufReferenceVoltage, 2);
		if (err == 0) {
			ret = bufReferenceVoltage[0] & 0xff + bufReferenceVoltage[1] & 0xff;
		} else {
			ret = -1;
		}

		return ret;
	}

	// TODO: Maybe it doesn't work
	/**
	 * 
	 * @param newFilterValue
	 *            New Reference Voltage value.
	 * @return error Negative number when a error occured, 0 otherwise.
	 */
	public int setReferenceVoltage(int newReferenceVoltage) {
		int ret = -1;
		int err = sendData(0x47, (byte) newReferenceVoltage);
		if (err == 0) {
			ret = 0;

		}
		return ret;
	}

	/**
	 * Change Reference voltage to current absolute voltage.
	 * 
	 * @return Returns 0 when no error occured and a negative number when an
	 *         error occured.
	 */
	public int writeCurrentAbsoluteVoltage() {
		return this.sendCommand(Command.ChangeReferenceVoltage);
	}
}
