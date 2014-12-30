package de.null_pointer.pi_server;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.gui.HandleValues;
import de.null_pointer.gui.JFDisplayValues;

public class PiServer {

	private static Logger logger = Logger.getLogger("TST.SIM");

	public static void main(String[] args) {
		InitializeProgram initProgram = new InitializeProgram(logger);
		initProgram.initializeLogger();
		Properties propPiServer = initProgram.getPropPiServer();

		initProgram.initializeSensors();
		initProgram.initializeCommunication();

		TestProgram testProgram = new TestProgram(initProgram,
				Integer.parseInt(propPiServer
						.getProperty("Pi_server.TestProgram.linear.speed")),
				Integer.parseInt(propPiServer
						.getProperty("Pi_server.TestProgram.turn.speed")),
				Integer.parseInt(propPiServer
						.getProperty("Pi_server.TestProgram.difference")),
				Integer.parseInt(propPiServer
						.getProperty("Pi_server.TestProgram.duration")));

		logger.info("starting programm");

		// Entscheidet anhand der Uebergabeparameter beim Start welche
		// Programmteile ausgefuehrt werden
		String s = null;
		for (int i = 0; i < args.length; i++) {
			s = args[i];
			// ruft die GUI auf
			if (s.equals("-gui")) {
				logger.debug("Starte GUI");
				HandleValues vGUI = new HandleValues(initProgram.getLsa(),
						initProgram.getAbsImu(), initProgram.getEopdLeft(),
						initProgram.getEopdRight(), initProgram.getDistNx());
				logger.info("GUI gestartet");
				vGUI.start();
			}

			// comp steht fuer competition, fuehrt das Wettkampfprogramm aus
			if (s.equals("-comp")) {
				logger.info("Wettkampfprogramm gestartet");
				initProgram.initializeNavigation();
				initProgram.initializeBehavior();

				initProgram.getArbitrator().start();
			}

			// startet ein Testprogramm, der nachfolgende String gibt an welches
			if (s.equals("-test")) {
				logger.debug("Waehle Testprogramm aus");
				i++;
				s = args[i];
				if (s.equals("forward")) {
					logger.debug("Starte Testprogramm forward");
					testProgram.forward();
				} else if (s.equals("backward")) {
					logger.debug("Starte Testprogramm backward");
					testProgram.backward();
				} else if (s.equals("rightturn")) {
					logger.debug("Starte Testprogramm rightturn");
					testProgram.rightturn();
				} else if (s.equals("leftturn")) {
					logger.debug("Starte Testprogramm leftturn");
					testProgram.leftturn();
				}
			}

		}
	}
}