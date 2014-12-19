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
import de.null_pointer.gui.JFDisplayValues;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.testmodules.testcommunication.TestBrickControlPi;

public class PiServer {

	private static Logger logger = Logger.getLogger("TST.SIM");

	public static void main(String[] args) {
		System.out.println("Hello World!");

		logger.setLevel(Level.INFO);
		loadConfiguration("log4j.properties");
		logger.log(Level.INFO, "---");
		logger.log(Level.INFO, "my Message is: it should work now");

		// Spezifiziert welche Meldungen alles ausgegeben werden
		// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
		// logger.setLevel(Level.INFO);

		BrickControlPi brickCon1 = null;
		BrickControlPi brickCon2 = null;

		InitCommunicationPi initCom = new InitCommunicationPi();

		// TODO: Brick-IDs eintragen
		String[] brickIDs = { null, null };
		CommunicationPi comPi;

		for (int i = 0; i < 2; i++) {
			comPi = initCom.initConnection(brickIDs[i]);
			if (comPi instanceof RealCommunicationPi) {
				// TODO: Angeschlossene Sensoren uebergeben
				if (i == 0) {
					brickCon1 = new BrickControlPi((RealCommunicationPi) comPi);
				} else if (i == 1) {
					brickCon2 = new BrickControlPi((RealCommunicationPi) comPi);
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

		MotorControlPi motorControl = new MotorControlPi(brickCon1, brickCon2);

		Abs_ImuProcessingPi absImu = new Abs_ImuProcessingPi(-1, -1, -1);
		EOPDProcessingPi eopdLeft = new EOPDProcessingPi();
		EOPDProcessingPi eopdRight = new EOPDProcessingPi();
		DistNxProcessingPi distNx = new DistNxProcessingPi();
		LSAProcessingPi las = new LSAProcessingPi();

		int widthMap = 11;
		int heightMap = 11;
		Navigation nav = new Navigation(widthMap, heightMap);

		// TODO: Reihenfolge richtig?
		Behavior b1 = new MovingForward(motorControl);
		Behavior b2 = new NextTile(absImu, nav);
		Behavior b3 = new Slope(motorControl, absImu, nav);
		Behavior b4 = new BlackTile(motorControl, las, nav);
		Behavior b5 = new Intersection(motorControl, distNx, eopdLeft,
				eopdRight, nav);
		Behavior b6 = new Victim(motorControl);

		Behavior[] behavior = { b1, b2, b3, b4, b5, b6 };

		Arbitrator arbitator = new Arbitrator(behavior);

		JFDisplayValues vGUI = new JFDisplayValues();

		logger.info("starting programm");

		// Entscheidet anhand der Uebergabeparameter beim Start welche
		// Programmteile ausgeführt werden
		for (String s : args) {

			// ruft die GUI auf
			if (s.equals("-gui")) {
				logger.info("GUI gestartet");
				vGUI.startGUI();
			}

			// comp steht für competition, führt das Wettkampfprogramm aus
			if (s.equals("-comp")) {
				logger.info("Wettkampfprogramm gestartet");
				arbitator.start();
			}

		}
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