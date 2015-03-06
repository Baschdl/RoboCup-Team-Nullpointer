package de.null_pointer.communication_pi;

import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.motorcontrol_pi.Semaphore;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.pi_server.InitializeProgram;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.AccumulatorProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.sensorprocessing_pi.ThermalSensorProcessingPi;

public class BrickControlPi extends Thread {
	private static Logger logger = Logger.getLogger(BrickControlPi.class);
	// TODO: initialisieren
	private CommunicationPi com = null;
	private Navigation navi = null;
	private Abs_ImuProcessingPi abs_Imu = null;
	private DistNxProcessingPi distNx = null;
	private EOPDProcessingPi eopdLeft = null;
	private EOPDProcessingPi eopdRight = null;
	private LSAProcessingPi lsa = null;
	private AccumulatorProcessingPi accumulator = null;
	private ThermalSensorProcessingPi thermal = null;
	private Semaphore semaphore = null;
	private InitializeProgram initializeProgram = null;
	private Properties propPiServer = null;

	private boolean readyToProcessData = true;
	private boolean sensorReady = true;
	private String[] sensor = null;

	public static enum Sensor {
		DistNx(1), LSA(2), EOPDLinks(3), EOPDRechts(4), AbsoluteIMU(5), LightSensorArray(
				2), IRThermalSensor(8);
		private final int number;

		private Sensor(int number) {
			this.number = number;
		}

		public int getNumber() {
			return number;
		}
	}

	public BrickControlPi(CommunicationPi com, Navigation navi,
			Abs_ImuProcessingPi abs_Imu, DistNxProcessingPi distNx,
			EOPDProcessingPi eopdLeft, EOPDProcessingPi eopdRight,
			LSAProcessingPi lsa, AccumulatorProcessingPi accumulator,
			ThermalSensorProcessingPi thermal,
			InitializeProgram initializeProgram, Properties propPiServer) {
		this.com = com;
		this.navi = navi;
		this.abs_Imu = abs_Imu;
		this.distNx = distNx;
		this.eopdLeft = eopdLeft;
		this.eopdRight = eopdRight;
		this.lsa = lsa;
		this.accumulator = accumulator;
		this.thermal = thermal;
		this.initializeProgram = initializeProgram;
		this.propPiServer = propPiServer;
	}

	public boolean getSensorReady() {
		return sensorReady;
	}

	public void setSensorReady() {
		sensorReady = false;
	}

	public void setSemaphore(Semaphore semaphore) {
		this.semaphore = semaphore;
	}

	public String[] getSensor() {
		return sensor;
	}

	/**
	 * Konstruktor nur fuer Testzwecke
	 */
	public BrickControlPi() {
	}

	public void run() {
		float[] message = { 0, 0, 0, 0 };
		while (!readyToProcessData) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (readyToProcessData) {
			message = receiveData();
			if (message != null) {
				processData(message);
			}
			try {
				sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Erhaelt die vom Brick empfangenen Daten und gibt sie an die passenden
	 * Klassen weiter.
	 * 
	 * @param receiveData
	 *            Enthaelt die vom Brick gesendeten Daten.
	 */
	void processData(float[] receiveData) {
		logger.debug("verarbeite Daten");
		// gibt empfangene Daten weiter

		logger.debug("value processData:" + Arrays.toString(receiveData));
		if (receiveData[0] == Sensor.DistNx.getNumber()) {
			// Dist-Nx
			distNx.setDistance(Math.round(receiveData[2]));
		} else if (receiveData[0] == Sensor.LightSensorArray.getNumber()) {
			// LSA
			lsa.setLSA(Math.round(receiveData[1] - 1),
					Math.round(receiveData[2]));
		} else if (receiveData[0] == Sensor.EOPDLinks.getNumber()) {
			// linker EOPD
			eopdLeft.setEOPDdistance(Math.round(receiveData[2]));
		} else if (receiveData[0] == Sensor.EOPDRechts.getNumber()) {
			// rechter EOPD
			eopdRight.setEOPDdistance(Math.round(receiveData[2]));
		} else if (receiveData[0] == Sensor.AbsoluteIMU.getNumber()) {
			// AbsIMU-ACG
			if (Math.round(receiveData[1]) >= 16) {
				abs_Imu.setAngle(Math.round(receiveData[2]),
						Math.round(receiveData[1]) - 16);
			} else {
				abs_Imu.setTiltData(Math.round(receiveData[2]),
						Math.round(receiveData[1]) - 11);
			}

		} else if (receiveData[0] == 6) {
			// Akkuladung
			accumulator.setMilliVolt(Math.round(receiveData[1]));
		} else if (receiveData[0] == 7) {
			logger.debug("Genuegend Werte uebermittelt: " + this);
			sensorReady = false;
		} else if (receiveData[0] == Sensor.IRThermalSensor.getNumber()) {
			// ThermalSensor
			thermal.setTemperature(Math.round(receiveData[2]));
		} else if (receiveData[0] == 9) {
			if (receiveData[1] == 1) {
				// TODO Reset Button gedrueckt; ggf. muss noch weiteres getan
				// werden
				navi.loadMap();
			} else if (receiveData[1] == 2) {
				initializeProgram.setProgramStarted();
			}
		} else if (receiveData[0] == 10) {
			semaphore.down();
		} else if (receiveData[0] == 11) {
			semaphore.down();
		} else if (receiveData[0] == 12) {
			semaphore.down();
		} else if (receiveData[0] == 13) {
			semaphore.down();
			semaphore.down();
		} else {

			// System.out.println("process Data (no sensor) " +
			// Arrays.toString(receiveData));
			// logger.error("process Data (no sensor) "
			// + Arrays.toString(receiveData));
		}

	}

	/**
	 * Erhaelt die an dem jeweiligen Brick angeschlossenen Sensoren und
	 * initialisert dazu passend die Sensorobjekte
	 * 
	 * @param sensor
	 *            Enthaelt die am Brick angeschlossenen Sensoren in der
	 *            Reihenfolge, in der sie angesteckt sind (Sensorport 1-4)
	 */
	public void sendSensorData(String[] sensor) {
		logger.info("intializing sensors: " + Arrays.toString(sensor));
		readyToProcessData = false;
		this.sensor = sensor;
		for (int i = 0; i < sensor.length; i++) {
			if (sensor[i].equals("Dist-Nx-v3")) {
				// dist_nx = controlClass.getDist_nx();
				sendCommand(10, 1, i + 1, 0);
				// logger.debug("DIST-NX:" + dist_nx);
			} else if (sensor[i].equals("LightSensorArray")) {
				// lsa = controlClass.getLsa();
				sendCommand(10, 2, i + 1, 0);
				// logger.debug("LSA:" + lsa);
			} else if (sensor[i].equals("EOPDLinks")) {
				// eopdLeft = controlClass.getEopdLeft();
				sendCommand(10, 3, i + 1, 1);
				// logger.debug("EOPD links" + eopdLeft);
			} else if (sensor[i].equals("EOPDRechts")) {
				// eopdRight = controlClass.getEopdRight();
				sendCommand(10, 3, i + 1, 2);
				// logger.debug("EOPD rechts" + eopdRight);
			} else if (sensor[i].equals("AbsoluteIMU-ACG")) {
				// absimu_acg = controlClass.getAbsimu_acg();
				sendCommand(10, 4, i + 1, 0);
				// logger.debug("Gyro" + absimu_acg);
			} else if (sensor[i].equals("IRThermalSensor")) {
				// tsLeft = controlClass.getTsLeft();
				sendCommand(10, 5, i + 1, 1);
				// logger.debug("ts left" + tsLeft);
			} else if (sensor[i].equals("ColourSensor")) {
				sendCommand(10, 6, i + 1, 0);
			} else {
				sendCommand(0, 0, 0, 0);
				logger.debug("Sensor existiert nicht");
			}
		}
		readyToProcessData = true;

		try {
			int linearAccelaration = Integer
					.parseInt(propPiServer
							.getProperty("CommunicationPi.BrickControlPi.linearAccelaration"));
			int rotationAccelaration = Integer
					.parseInt(propPiServer
							.getProperty("CommunicationPi.BrickControlPi.rotationAccelaration"));
			int speed = Integer
					.parseInt(propPiServer
							.getProperty("CommunicationPi.BrickControlPi.rotationSpeed"));
			setLinearAccelaration(linearAccelaration);
			setRotationAccelaration(rotationAccelaration);
			setRotationSpeed(speed);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("sendSensorData: error while parsing properties");
		}
		// notifyAll();
		logger.debug("sendSensorData flag set to: " + readyToProcessData);
	}

	/**
	 * Empfaengt einen String vom Brick
	 * 
	 * @return Gibt die von checkString(...) weiter verarbeiteten Daten zurueck
	 */
	public float[] receiveData() {
		String dataString = null;
		try {
			logger.debug("Lese Daten...");
			dataString = com.receiveString();
			logger.debug("gelesen: " + dataString);
			return checkString(dataString);

		} catch (Exception e) {
			logger.error("Fehler beim Empfangen von Daten:" + e);
			return null;
		}
	}

	/**
	 * Ueberprueft den empfangenen String auf Validitaet und teilt ihn in seine
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
				if (puf[i].matches("[-0-9]+")) {
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
	private void sendCommand(int recipient, int action) {
		String command = "*" + recipient + ";" + action + ";" + "0" + ";" + "0"
				+ "#";
		logger.debug("Sende Kommando");
		com.sendString(command);
		logger.debug("Senden des Kommandos beendet");
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
	 *            Weitere Werte, die fuer die durch das Kommando ausgeloeste
	 *            Operation noetig sind.
	 */
	private void sendCommand(int recipient, int action, int parameter) {
		String command = "*" + recipient + ";" + action + ";" + parameter + ";"
				+ "0" + "#";
		logger.debug("Sende Kommando");
		com.sendString(command);
		logger.debug("Senden des Kommandos beendet");
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
	 *            Weitere Werte, die fuer die durch das Kommando ausgeloeste
	 *            Operation noetig sind.
	 * @param options
	 *            Gibt z.B. bei den Motoren an in welche Richtung sie sich
	 *            drehen sollen.
	 */
	private void sendCommand(int recipient, int action, int parameter,
			int options) {
		String command = "*" + recipient + ";" + action + ";" + parameter + ";"
				+ options + "#";
		logger.debug("Sende Kommando");
		com.sendString(command);
		logger.debug("Senden des Kommandos beendet");

	}

	public void forward(int speed, char motorport) {
		logger.debug("Sende Kommando forward");
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
		case 'D':
			sendCommand(9, 4, speed, 1);
			break;
		default:
			break;
		}
		logger.debug("Senden des Kommandos forward beendet");

	}

	public void backward(int speed, char motorport) {
		logger.debug("Sende Kommando backward");
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
		case 'D':
			sendCommand(9, 4, speed, 2);
			break;
		default:
			break;
		}
		logger.debug("Senden des Kommandos backward beendet");

	}

	public void stop(char motorport) {
		logger.debug("Sende Kommando stop");
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
		case 'D':
			sendCommand(9, 4, 0, 3);
			break;
		default:
			break;
		}
		logger.debug("Senden des Kommandos stop beendet");

	}

	public void flt(char motorport) {
		logger.debug("Sende Kommando flt");
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
		case 'D':
			sendCommand(9, 4, 0, 4);
			break;
		default:
			break;
		}
		logger.debug("Senden des Kommandos flt beendet");

	}

	public void rotate(int angle, char motorport) {
		logger.debug("Sende Kommando rotate");
		semaphore.up();
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
		case 'D':
			semaphore.up();
			sendCommand(9, 4, angle, 5);
			break;
		default:
			break;
		}
		logger.debug("Senden des Kommandos rotate beendet");
	}

	/**
	 * sets accelaration for linear movements like forward or backward
	 * 
	 * @param accelaration
	 */
	public void setLinearAccelaration(int accelaration) {
		sendCommand(9, 5, 1, accelaration);
	}

	/**
	 * sets the accelaration for movements like turning the robot
	 * 
	 * @param accelaration
	 */
	public void setRotationAccelaration(int accelaration) {
		sendCommand(9, 5, 2, accelaration);
	}

	/**
	 * sets the speed for rotating the motor
	 * 
	 * @param speed
	 */
	public void setRotationSpeed(int speed) {
		sendCommand(9, 5, 3, speed);
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

	public void blinkColorSensorLED() {
		sendCommand(11, 1);
	}
}
