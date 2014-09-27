package de.null_pointer.communication_pi;

import java.util.Arrays;

import org.apache.log4j.Logger;

public class BrickControlPi {
	private static Logger logger = Logger.getLogger(BrickControlPi.class);
	// TODO: initialisieren
	private InitCommunicationPi initCom = new InitCommunicationPi();
	private RealCommunicationPi com;

	private boolean readyToProcessData;

	public BrickControlPi() {
		// TODO: brick_id passend uebergeben+ ueberpruefen des casts
		com = (RealCommunicationPi) initCom.initConnection(null);
	}

	/**
	 * Erhaelt die vom Brick empfangenen Daten und gibt sie an die passenden
	 * Klassen weiter.
	 * 
	 * @param receiveData
	 *            Enthaelt die vom Brick gesendeten Daten.
	 */
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

	/**
	 * Erhaelt die an dem jeweiligen Brick angeschlossenen Sensoren und
	 * initialisert dazu passend die Sensorobjekte
	 * 
	 * @param Sensor
	 *            Enthaelt die am Brick angeschlossenen Sensoren in der
	 *            Reihenfolge, in der sie angesteckt sind (Sensorport 1-4)
	 */
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

	/**
	 * Empfaengt einen String vom Brick
	 * 
	 * @return Gibt die von checkString(...) weiter verarbeiteten Daten zurück
	 */
	float[] receiveData() {
		String dataString = null;
		try {
			// logger.info("Lese Zeile...");
			dataString = com.receiveString();
			// logger.info("gelesen.");
			return checkString(dataString);

		} catch (Exception e) {
			logger.error("Fehler beim Empfangen von Daten:" + e);
			return null;
		}
	}

	/**
	 * Ueberprueft den empfangenen String auf Validität und teilt ihn in seine
	 * Einzelteile auf.
	 * 
	 * @param dataString
	 *            Enthaelt die Daten, welche ueberprueft und weiter verarbeitet
	 *            werden sollen
	 * @return Gibt die uebergebenen Daten aufgeteilt in ihre Einzelteile
	 *         zurueck
	 */
	float[] checkString(String dataString) {
		float[] dataInt = new float[4];
		String debugString = dataString;
		// Ermittelt die Laenge des Strings
		int indexMaxDataString = dataString.length() - 1;
		// Ueberprueft, ob die Kodierung richtig ist ('*' am Anfang, '#' am
		// Ende)
		if (dataString.charAt(0) == '*'
				&& dataString.charAt(indexMaxDataString) == '#') {
			// Entfernt das '*' am Anfang des Strings
			dataString = dataString.substring(1, indexMaxDataString);
			String puf[] = new String[4];
			// TODO: Siehe Unterrichtsmaterial, Kommentare ergaenzen
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
				// Wandelt die Strings in Integer um und speichert sie ab
				if (puf[i].matches("[0-9]+")) {
					dataInt[i] = Integer.parseInt(puf[i]);
				} else {
					logger.error("Ungueltige Zeichen im uebertragenen String (Buchstaben, mehrere/ falsche Separatoren...): "
							+ debugString);
					return null;
				}

			}
			return dataInt;
		} else {
			logger.error("Fehlerhafte Daten: " + debugString);
			return null;
		}
	}

	/**
	 * Kodiert die zu sendenden Kommandos und sendet sie ab.
	 * 
	 * @param recipient
	 *            Spezifiziert an welchen Sensor/ Motor... das Kommando
	 *            gerichtet ist.
	 * @param action
	 *            Gibt an was passieren soll bzw. welche Methode auf dem Brick
	 *            aufgerufen werden soll.
	 */
	public void sendCommand(int recipient, int action) {
		String command = "*" + recipient + ";" + action + ";" + "0" + ";" + "0"
				+ "#";
		com.sendString(command);
	}

	/**
	 * Kodiert die zu sendenden Kommandos und sendet sie ab.
	 * 
	 * @param recipient
	 *            Spezifiziert an welchen Sensor/ Motor... das Kommando
	 *            gerichtet ist.
	 * @param action
	 *            Gibt an was passieren soll bzw. welche Methode auf dem Brick
	 *            aufgerufen werden soll.
	 * @param parameter
	 *            Weitere Werte, die für die durch das Kommando ausgeloeste
	 *            Operation noetig sind.
	 */
	public void sendCommand(int recipient, int action, int parameter) {
		String command = "*" + recipient + ";" + action + ";" + parameter + ";"
				+ "0" + "#";
		com.sendString(command);
	}

	/**
	 * Kodiert die zu sendenden Kommandos und sendet sie ab.
	 * 
	 * @param recipient
	 *            Spezifiziert an welchen Sensor/ Motor... das Kommando
	 *            gerichtet ist.
	 * @param action
	 *            Gibt an was passieren soll bzw. welche Methode auf dem Brick
	 *            aufgerufen werden soll.
	 * @param parameter
	 *            Weitere Werte, die für die durch das Kommando ausgeloeste
	 *            Operation noetig sind.
	 * @param options
	 *            Gibt z.B. bei den Motoren an in welche Richtung sie sich
	 *            drehen sollen.
	 */
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
