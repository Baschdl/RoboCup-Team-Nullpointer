package de.null_pointer.pi_server;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.gui.JFDisplayValues;

public class PiServer {

	private static Logger logger = Logger.getLogger("TST.SIM");

	public static void main(String[] args) {
		InitializeProgram initProgram = new InitializeProgram(logger);
		initProgram.initializeLogger();
		Properties propPiServer = initProgram.getPropPiServer();
		initProgram.initializeCommunication();
		initProgram.initializeSensors();
		initProgram.initializeNavigation();
		initProgram.initializeBehavior();

		TestProgram testProgram = new TestProgram(initProgram,
				Integer.parseInt(propPiServer
						.getProperty("Pi_server.TestProgram.linear.speed")),
				Integer.parseInt(propPiServer
						.getProperty("Pi_server.TestProgram.turn.speed")),
				Integer.parseInt(propPiServer
						.getProperty("Pi_server.TestProgram.difference")),
				Integer.parseInt(propPiServer
						.getProperty("Pi_server.TestProgram.duration")));
		JFDisplayValues vGUI = new JFDisplayValues();

		logger.info("starting programm");

		// Entscheidet anhand der Uebergabeparameter beim Start welche
		// Programmteile ausgefuehrt werden
		String s = null;
		for (int i = 0; i < args.length; i++) {
			s = args[i];
			// ruft die GUI auf
			if (s.equals("-gui")) {
				logger.info("GUI gestartet");
				vGUI.startGUI();
			}

			// comp steht fuer competition, fuehrt das Wettkampfprogramm aus
			if (s.equals("-comp")) {
				logger.info("Wettkampfprogramm gestartet");
				initProgram.getArbitrator().start();
			}

			// startet ein Testprogramm, der nachfolgende String gibt an welches
			if (s.equals("-test")) {
				i++;
				s = args[i];
				if (s.equals("forward")) {
					testProgram.forward();
				} else if (s.equals("backward")) {
					testProgram.backward();
				} else if (s.equals("rightturn")) {
					testProgram.rightturn();
				} else if (s.equals("leftturn")) {
					testProgram.leftturn();
				}
			}

		}
	}
}