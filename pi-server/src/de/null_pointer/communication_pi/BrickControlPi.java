package de.null_pointer.communication_pi;

import java.util.Arrays;

import org.apache.log4j.Logger;

public class BrickControlPi {
	private static Logger logger = Logger.getLogger(BrickControlPi.class);
	// TODO: initialisieren
	private RealCommunicationPi com;

	private boolean readyToProcessData;

	void processData(float[] receiveData) {
		// gibt empfangene Daten weiter

		// System.out.println("value processData:" +
		// Arrays.toString(receiveData));
		if (receiveData[0] == 1) {
			// Dist-Nx
		} else if (receiveData[0] == 2) {
			// LSA
		} else if (receiveData[0] == 3) {
			// linker EOPD
		} else if (receiveData[0] == 4) {
			// rechter EOPD
		} else if (receiveData[0] == 5) {
			// AbsIMU-ACG
		} else if (receiveData[0] == 6) {
			// linker Tastsenspr
		} else if (receiveData[0] == 7) {
			// rechter Tastsensor
		} else if (receiveData[0] == 8) {
			// Boltmeter
		} else {
			// System.out.println("process Data (no sensor) " +
			// Arrays.toString(receiveData));
			logger.error("process Data (no sensor) "
					+ Arrays.toString(receiveData));
		}

	}

	public void sendSensorData(String[] Sensor) {
		logger.info("intializing sensors: " + Arrays.toString(Sensor));
		readyToProcessData = false;
		for (int i = 0; i < Sensor.length; i++) {
			if (Sensor[i].equals("Dist-Nx-v3")) {
				// dist_nx = controlClass.getDist_nx();
				sendCommand(10, 1, i + 1, 0);
				// logger.debug("DIST-NX:" + dist_nx);
			} else if (Sensor[i].equals("LightSensorArray")) {
				// lsa = controlClass.getLsa();
				sendCommand(10, 2, i + 1, 0);
				// logger.debug("LSA:" + lsa);
			} else if (Sensor[i].equals("EOPDLinks")) {
				// eopdLeft = controlClass.getEopdLeft();
				sendCommand(10, 3, i + 1, 1);
				// logger.debug("EOPD links" + eopdLeft);
			} else if (Sensor[i].equals("EOPDRechts")) {
				// eopdRight = controlClass.getEopdRight();
				sendCommand(10, 3, i + 1, 2);
				// logger.debug("EOPD rechts" + eopdRight);
			} else if (Sensor[i].equals("AbsoluteIMU-ACG")) {
				// absimu_acg = controlClass.getAbsimu_acg();
				sendCommand(10, 4, i + 1, 0);
				// logger.debug("Gyro" + absimu_acg);
			} else if (Sensor[i].equals("TouchSensorLinks")) {
				// tsLeft = controlClass.getTsLeft();
				sendCommand(10, 5, i + 1, 1);
				// logger.debug("ts left" + tsLeft);
			} else if (Sensor[i].equals("TouchSensorRechts")) {
				// tsRight = controlClass.getTsRight();
				sendCommand(10, 5, i + 1, 2);
				// logger.debug("ts right" + tsRight);
			} else if (Sensor[i].equals("NXTVoltmeter")) {
				// nxt_vm = controlClass.getNxt_vm();
				sendCommand(10, 6, i + 1, 0);
				// logger.debug("NXT Voltmeter" + nxt_vm);
			} else {
				logger.debug("Sensor existiert nicht");
			}
		}
		readyToProcessData = true;
		logger.debug("sendSensorData flag set to: " + readyToProcessData);
	}

	float[] receiveData() {
		String dataString = null;
		String debugString = "-";
		float[] dataInt = new float[4];
		try {
			// System.out.println("Lese Zeile...");
			dataString = com.receiveString();
			debugString = dataString;
			// System.out.println("gelesen.");
			int indexMaxDataString = dataString.length() - 1;
			if (dataString.charAt(0) == '*'
					&& dataString.charAt(indexMaxDataString) == '#') {
				dataString = dataString.substring(1, indexMaxDataString);
				String puf[] = new String[4];
				int index1 = 0, index2 = 0;
				for (int i = 0; i < 4; i++) {
					if (index1 < dataString.length()) {
						index2 = dataString.indexOf(";", index1);
						if (index2 >= 0) {
							puf[i] = dataString.substring(index1, index2);
							index1 = index2 + 1;
						} else {
							puf[i] = dataString.substring(index1);
						}
					}
					dataInt[i] = Integer.parseInt(puf[i]);
				}
				return dataInt;
			} else {
				logger.error("Fehlerhafte Daten" + debugString);
				return null;
			}
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	public void sendCommand(int recipient, int action) {
		String command = "*" + recipient + ";" + action + ";" + "0" + ";" + "0"
				+ "#";
		com.sendString(command);
	}

	public void sendCommand(int recipient, int action, int parameter) {
		String command = "*" + recipient + ";" + action + ";" + parameter + ";"
				+ "0" + "#";
		com.sendString(command);
	}

	private void sendCommand(int recipient, int action, int parameter,
			int options) {

		String command = "*" + recipient + ";" + action + ";" + parameter + ";"
				+ options + "#";
		com.sendString(command);

	}

	public void forward(int speed, char motorport) {
		switch (motorport) {
		case 'A':
			sendCommand(9, 1, speed, 1);
			break;
		case 'B':
			sendCommand(9, 2, speed, 1);
			break;

		case 'C':
			sendCommand(9, 3, speed, 1);
			break;
		default:
			break;
		}

	}

	public void backward(int speed, char motorport) {
		switch (motorport) {
		case 'A':
			sendCommand(9, 1, speed, 2);
			break;
		case 'B':
			sendCommand(9, 2, speed, 2);
			break;

		case 'C':
			sendCommand(9, 3, speed, 2);
			break;
		default:
			break;
		}

	}

	public void stop(char motorport) {
		switch (motorport) {
		case 'A':
			sendCommand(9, 1, 0, 3);
			break;
		case 'B':
			sendCommand(9, 2, 0, 3);
			break;

		case 'C':
			sendCommand(9, 3, 0, 3);
			break;
		default:
			break;
		}

	}

	public void flt(char motorport) {
		switch (motorport) {
		case 'A':
			sendCommand(9, 1, 0, 4);
			break;
		case 'B':
			sendCommand(9, 2, 0, 4);
			break;

		case 'C':
			sendCommand(9, 3, 0, 4);
			break;
		default:
			break;
		}

	}

	public void rotate(int angle, char motorport) {
		switch (motorport) {
		case 'A':
			sendCommand(9, 1, angle, 5);
			break;
		case 'B':
			sendCommand(9, 2, angle, 5);
			break;

		case 'C':
			sendCommand(9, 3, angle, 5);
			break;
		default:
			break;
		}
	}

	/**
	 * @param mode
	 *            true for long mode, false for short mode
	 * 
	 * @param eopd
	 *            3 is the left EOPD, 4 is the right EOPD
	 */
	public void setEOPDMode(boolean mode, byte eopd) {
		if (mode == true) {
			if (eopd == 3) {
				sendCommand(3, 1);
			} else if (eopd == 4) {
				sendCommand(4, 1);
			} else {
				logger.warn("Sensor existiert nicht");
			}
		} else if (mode == false) {
			if (eopd == 3) {
				sendCommand(3, 2);
			} else if (eopd == 4) {
				sendCommand(4, 2);
			} else {
				logger.warn("Sensor existiert nicht");
			}
		}
	}

	public void calibrateLSABlack() {
		sendCommand(2, 3);
	}

	public void calibrateLSAWhite() {
		sendCommand(2, 4);
	}

	// TODO: Noch einzubauen
	public void exitBrick() {
		sendCommand(99, 0, 0);
	}

	public void resetMotor(char motorport) {
		sendCommand(9, 1, 0, 6);

	}

}
