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
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.gui.*;

public class PiServer {

	private static Logger logger = Logger.getRootLogger();
	private static JFDisplayValues vGUI = new JFDisplayValues();

	public static void main(String[] args) {
		// TODO: Angeschlossene Sensoren uebergeben
		BrickControlPi brickCon1 = new BrickControlPi();
		BrickControlPi brickCon2 = new BrickControlPi();

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
		logger.setLevel(Level.ALL);

		logger.info("starting programm");

		// Entscheidet anhand der Uebergabeparameter beim Start welche
		// Programmteile ausgeführt werden
		for (String s : args) {
			// comp steht für competition, führt das Wettkampfprogramm aus
			if (s.equals("-comp")) {
				arbitator.start();
			}

			// ruft die GUI auf
			else if (s.equals("-gui")) {
				vGUI.startGUI();
			}
		}
	}
}