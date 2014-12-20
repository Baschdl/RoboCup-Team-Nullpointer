package de.null_pointer.pi_server;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import de.null_pointer.behavior.BlackTile;
import de.null_pointer.behavior.Intersection;
import de.null_pointer.behavior.MovingForward;
import de.null_pointer.behavior.NextTile;
import de.null_pointer.behavior.Slope;
import de.null_pointer.behavior.Victim;
import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.communication_pi.CommunicationPi;
import de.null_pointer.communication_pi.InitCommunicationPi;
import de.null_pointer.communication_pi.RealCommunicationPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.testmodules.testcommunication.TestBrickControlPi;

public class InitializeProgram {
	private static Logger logger = null;
	private BrickControlPi brickCon1 = null;
	private BrickControlPi brickCon2 = null;
	private InitCommunicationPi initCom = null;
	private MotorControlPi motorControl = null;
	private Abs_ImuProcessingPi absImu = null;
	private EOPDProcessingPi eopdLeft = null;
	private EOPDProcessingPi eopdRight = null;
	private DistNxProcessingPi distNx = null;
	private LSAProcessingPi lsa = null;
	private Navigation nav = null;
	private Arbitrator arbitrator = null;
	private CommunicationPi comPi = null;

	public Arbitrator getArbitrator() {
		return arbitrator;
	}

	public InitializeProgram(Logger logger) {
		InitializeProgram.logger = logger;
	}

	public void initializeLogger() {
		logger.setLevel(Level.INFO);
		loadConfiguration("log4j.properties");
		logger.log(Level.INFO, "---");
		logger.log(Level.INFO, "my Message is: it should work now");

		// Spezifiziert welche Meldungen alles ausgegeben werden
		// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
		// logger.setLevel(Level.INFO);
	}

	public void initializeCommunication() {

		initCom = new InitCommunicationPi();

		// TODO: Brick-IDs eintragen
		String[] brickIDs = { null, null };

		for (int i = 0; i < 2; i++) {
			comPi = initCom.initConnection(brickIDs[i]);
			if (comPi instanceof RealCommunicationPi) {
				// TODO: Angeschlossene Sensoren uebergeben
				if (i == 0) {
					brickCon1 = new BrickControlPi((RealCommunicationPi) comPi, absImu, distNx, eopdLeft, eopdLeft, lsa);
				} else if (i == 1) {
					brickCon2 = new BrickControlPi((RealCommunicationPi) comPi, absImu, distNx, eopdLeft, eopdLeft, lsa);
				} else {
					logger.warn("Es wurde versucht Verbindungen zu mehr als zwei Bricks einzurichten");
				}

			} else {
				if (i == 0) {
					brickCon1 = new TestBrickControlPi();
				} else if (i == 1) {
					brickCon2 = new TestBrickControlPi();
				} else {
					logger.warn("Es wurde versucht virtuelle Verbindungen zu mehr als zwei Bricks einzurichten");
				}
			}
		}
	}

	public void initializeSensors() {
		motorControl = new MotorControlPi(brickCon1, brickCon2);

		absImu = new Abs_ImuProcessingPi(-1, -1, -1);
		eopdLeft = new EOPDProcessingPi();
		eopdRight = new EOPDProcessingPi();
		distNx = new DistNxProcessingPi();
		lsa = new LSAProcessingPi();
	}

	public void initializeNavigation() {
		int widthMap = 11;
		int heightMap = 11;
		nav = new Navigation(widthMap, heightMap);
	}

	public void initializeBehavior() {
		// TODO: Reihenfolge richtig?
		Behavior b1 = new MovingForward(motorControl);
		Behavior b2 = new NextTile(absImu, nav);
		Behavior b3 = new Slope(motorControl, absImu, nav);
		Behavior b4 = new BlackTile(motorControl, lsa, nav);
		Behavior b5 = new Intersection(motorControl, distNx, eopdLeft,
				eopdRight, nav);
		Behavior b6 = new Victim(motorControl);

		Behavior[] behavior = { b1, b2, b3, b4, b5, b6 };

		arbitrator = new Arbitrator(behavior);
	}

	private static void loadConfiguration(String configFile) {

		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = Main.class.getClassLoader().getResourceAsStream(configFile);
			properties.load(input);
			logger.log(Level.INFO, "load configuration file " + configFile);
			logger.log(Level.INFO, properties.getProperty("my.value"));

		} catch (IOException e) {
			logger.log(Level.ERROR, "configuration file not found "
					+ configFile + " " + e.getMessage());
		}

	}
}
