import org.apache.log4j.Logger;

import de.null_pointer.gui.JFDisplayValues;

public class PiServer {

	private static Logger logger = Logger.getLogger("TST.SIM");

	public static void main(String[] args) {
		InitializeProgram initProgramm = new InitializeProgram(logger);
		initProgramm.initializeLogger();
		initProgramm.initializeCommunication();
		initProgramm.initializeSensors();
		initProgramm.initializeNavigation();
		initProgramm.initializeBehavior();

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
				initProgramm.getArbitrator().start();
			}

		}
	}
}