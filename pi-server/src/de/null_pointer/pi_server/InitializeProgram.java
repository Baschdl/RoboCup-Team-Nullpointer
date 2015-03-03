package de.null_pointer.pi_server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.null_pointer.behavior.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;

import de.null_pointer.behavior.BlackTile;
import de.null_pointer.behavior.Intersection;
import de.null_pointer.behavior.MovingForward;
import de.null_pointer.behavior.NextTile;
import de.null_pointer.behavior.SilverTile;
import de.null_pointer.behavior.Slope;
import de.null_pointer.behavior.Victim;
import de.null_pointer.behavior.WallTooClose;
import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.communication_pi.CommunicationPi;
import de.null_pointer.communication_pi.InitCommunicationPi;
import de.null_pointer.communication_pi.RealCommunicationPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.motorcontrol_pi.Semaphore;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.AccumulatorProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.sensorprocessing_pi.ThermalSensorProcessingPi;
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
	private AccumulatorProcessingPi accumulator = null;
	private ThermalSensorProcessingPi thermal = null;

	private Navigation nav = null;
	private Odometer odometer = null;
	private Arbitrator arbitrator = null;
	private CommunicationPi comPi1 = null;
	private CommunicationPi comPi2 = null;

	private Semaphore available = null;

	private Properties propPiServer = null;

	private boolean programStarted = false;

	public Arbitrator getArbitrator() {
		return arbitrator;
	}

	public BrickControlPi getBrickCon1() {
		return brickCon1;
	}

	public BrickControlPi getBrickCon2() {
		return brickCon2;
	}

	public InitCommunicationPi getInitCom() {
		return initCom;
	}

	public MotorControlPi getMotorControl() {
		return motorControl;
	}

	public Abs_ImuProcessingPi getAbsImu() {
		return absImu;
	}

	public EOPDProcessingPi getEopdLeft() {
		return eopdLeft;
	}

	public EOPDProcessingPi getEopdRight() {
		return eopdRight;
	}

	public DistNxProcessingPi getDistNx() {
		return distNx;
	}

	public LSAProcessingPi getLsa() {
		return lsa;
	}

	public AccumulatorProcessingPi getAccumulator() {
		return accumulator;
	}

	public Navigation getNav() {
		return nav;
	}

	public Odometer getOdometer() {
		return odometer;
	}

	public CommunicationPi getComPi1() {
		return comPi1;
	}

	public CommunicationPi getComPi2() {
		return comPi2;
	}

	public Properties getPropPiServer() {
		return propPiServer;
	}

	public ThermalSensorProcessingPi getThermal() {
		return thermal;
	}

	public void setProgramStarted() {
		this.programStarted = true;
	}

	public InitializeProgram(Logger logger) {
		InitializeProgram.logger = logger;
	}

	public void initializeLogger() {
		logger.setLevel(Level.INFO);
		loadConfiguration("resources/log4j.properties");
		propPiServer = loadConfiguration("resources/pi_server.properties");

		logger.log(Level.INFO, "---");
		logger.log(Level.INFO, "my Message is: it should work now");

		// Spezifiziert welche Meldungen alles ausgegeben werden
		// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
		// logger.setLevel(Level.INFO);
	}

	public void initializeProperties() {
		propPiServer = loadConfiguration("resources/pi_server.properties");
	}

	public void initializeSensors() {
		absImu = new Abs_ImuProcessingPi(propPiServer);
		eopdLeft = new EOPDProcessingPi();
		eopdRight = new EOPDProcessingPi();
		distNx = new DistNxProcessingPi();
		lsa = new LSAProcessingPi();
		accumulator = new AccumulatorProcessingPi();
		thermal = new ThermalSensorProcessingPi();
	}

	public void initializeCommunication() {

		String[] brickIDs = { propPiServer.getProperty("Brick.One.USB.ID"),
				propPiServer.getProperty("Brick.Two.USB.ID") };

		for (int i = 0; i < 2; i++) {
			initCom = new InitCommunicationPi();
			if (i == 0) {
				comPi1 = initCom.initConnection(brickIDs[i]);
			} else if (i == 1) {
				comPi2 = initCom.initConnection(brickIDs[i]);
			}
			if (comPi1 instanceof RealCommunicationPi) {
				// TODO: Angeschlossene Sensoren uebergeben
				if (i == 0) {
					brickCon1 = new BrickControlPi(
							(RealCommunicationPi) comPi1, nav, absImu, distNx,
							eopdLeft, eopdRight, lsa, accumulator, thermal,
							this);
				} else if (i == 1) {
					brickCon2 = new BrickControlPi(
							(RealCommunicationPi) comPi2, nav, absImu, distNx,
							eopdLeft, eopdRight, lsa, accumulator, thermal,
							this);
				} else {
					logger.warn("Es wurde versucht Verbindungen zu mehr als zwei Bricks einzurichten");
				}

			} else {
				if (i == 0) {
					brickCon1 = new TestBrickControlPi(comPi1, nav, absImu,
							distNx, eopdLeft, eopdLeft, lsa, accumulator,
							thermal, this);
				} else if (i == 1) {
					brickCon2 = new TestBrickControlPi(comPi2, nav, absImu,
							distNx, eopdLeft, eopdLeft, lsa, accumulator,
							thermal, this);
				} else {
					logger.warn("Es wurde versucht virtuelle Verbindungen zu mehr als zwei Bricks einzurichten");
				}
			}
		}

		if (brickCon1 != null) {
			logger.info("Communication with Brick 1 started");
			String sensor[] = {
					propPiServer.getProperty("Brick.One.Sensorport.One"),
					propPiServer.getProperty("Brick.One.Sensorport.Two"),
					propPiServer.getProperty("Brick.One.Sensorport.Three"),
					propPiServer.getProperty("Brick.One.Sensorport.Four") };
			brickCon1.sendSensorData(sensor);
			brickCon1.start();
		}
		if (brickCon2 != null) {
			logger.info("Communication with Brick 2 started");
			String sensor[] = {
					propPiServer.getProperty("Brick.Two.Sensorport.One"),
					propPiServer.getProperty("Brick.Two.Sensorport.Two"),
					propPiServer.getProperty("Brick.Two.Sensorport.Three"),
					propPiServer.getProperty("Brick.Two.Sensorport.Four") };
			brickCon2.sendSensorData(sensor);
			brickCon2.start();
		}

		motorControl = new MotorControlPi(brickCon1, brickCon2);
	}

	public void initializeNavigation() {
		nav = new Navigation(propPiServer);
		odometer = new Odometer(accumulator, absImu, propPiServer);
	}

	public void initializeBehavior() {
		// TODO: Reihenfolge richtig?
		Behavior b1 = new MovingForward(motorControl, odometer, propPiServer);
		Behavior b2 = new NextTile(/* absImu, */motorControl, nav, odometer,
				propPiServer);
		Behavior b3 = new Slope(motorControl, absImu, nav, propPiServer);
		Behavior b4 = new Intersection(motorControl, distNx, eopdLeft,
				eopdRight, absImu, odometer, nav, propPiServer);
		Behavior b5 = new SilverTile(lsa, nav, propPiServer);
		Behavior b6 = new BlackTile(motorControl, lsa, absImu, nav, odometer,
				propPiServer);
		Behavior b7 = new WallTooClose(eopdRight, eopdLeft, motorControl,
				odometer, propPiServer, absImu);
		Behavior b8 = new Victim(brickCon2, motorControl, nav, thermal,
				propPiServer);

		Behavior[] behavior = { b1, b2, /** b3, **/
		b4, b5, b6, /** b7, **/
		b8 };

		// Abritrator wird erst initialisiert, wenn von beiden Bricks gemeldet
		// wird, dass jeweils mindestens 10 Sensorwerte an pi-server geschickt
		// wurden
		while (brickCon1.getSensorReady() && brickCon2.getSensorReady()) {
		}
		logger.info("Genug Sensorwerte erhalten");

		arbitrator = new Arbitrator(behavior);
		available = new Semaphore(arbitrator, behavior);
		brickCon1.setSemaphore(available);
		brickCon2.setSemaphore(available);

		while (!programStarted) {
			Delay.msDelay(250);
		}
		logger.info("Programm durch Betaetigen des Enter-Buttons auf dem Brick gestartet");

	}

	private static Properties loadConfiguration(String configFile) {

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
		return properties;

	}
}
