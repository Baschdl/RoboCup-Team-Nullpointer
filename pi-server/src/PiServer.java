import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import de.null_pointer.behavior.BlackTile;
import de.null_pointer.behavior.Intersection;
import de.null_pointer.behavior.MovingForward;
import de.null_pointer.behavior.NextTile;
import de.null_pointer.behavior.Slope;
import de.null_pointer.behavior.Victim;
import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.communication_pi.BrickControlPiTest;
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
import de.null_pointer.gui.*;

public class PiServer {

	private static Logger logger = Logger.getRootLogger();

	public static void main(String[] args) {
		// Anlegen und Einrichten des Loggers

		// SimpleLayout layout = new SimpleLayout();
		PatternLayout layout = new PatternLayout("%d{ISO8601} %-6p [%c] %m%n");
		ConsoleAppender consoleAppender = new ConsoleAppender(layout);
		logger.addAppender(consoleAppender);

		// TODO: Abspeichern der Logs in ein File implementieren

		// boolean append = true;
		// FileHandler handler = new FileHandler("default.log", append);
		//
		// Logger logger2 = Logger.getLogger(PiServer.class);
		// logger.addHandler(handler);

		// Spezifiziert welche Meldungen alles ausgegeben werden
		// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
		logger.setLevel(Level.INFO);

		BrickControlPi brickCon1 = null;
		BrickControlPi brickCon2 = null;

		InitCommunicationPi initCom = new InitCommunicationPi();
		
		//TODO: Brick-IDs eintragen
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

		Abs_ImuProcessingPi absImu = new Abs_ImuProcessingPi();
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
		// Programmteile ausgef�hrt werden
		for (String s : args) {

			// ruft die GUI auf
			if (s.equals("-gui")) {
				logger.info("GUI gestartet");
				vGUI.startGUI();
			}

			// comp steht f�r competition, f�hrt das Wettkampfprogramm aus
			if (s.equals("-comp")) {
				logger.info("Wettkampfprogramm gestartet");
				arbitator.start();
			}

		}
	}
}