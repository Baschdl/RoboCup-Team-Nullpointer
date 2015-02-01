package de.null_pointer.pi_server;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.gui.HandleValues;
import de.null_pointer.navigation.test.GuiNavigation;

/* Setup:
 * 
 * required libraries:
 * - JRE
 * - JUnit 4
 * - LeJOS PC Libraries
 * - log4j-1.2.17 (in /lib)
 * - org.eclipse.jdt.annotation_1.1.0.v20130513-1648 (in /lib)
 * 
 * Classpath- User Entries:
 * /pi-server/src/resources/
 * 
 */

public class PiServer {

	private static Logger logger = Logger.getLogger("TST.SIM");

	public static void main(String[] args) {
		InitializeProgram initProgram = new InitializeProgram(logger);
		initProgram.initializeLogger();
		Properties propPiServer = initProgram.getPropPiServer();

		initProgram.initializeSensors();
		initProgram.initializeCommunication();

		TestProgram testProgram = new TestProgram(initProgram, propPiServer);

		logger.info("starting programm");

		// Entscheidet anhand der Uebergabeparameter beim Start welche
		// Programmteile ausgefuehrt werden
		String s = null;
		for (int i = 0; i < args.length; i++) {
			s = args[i];
			// ruft die GUI auf
			if (s.equals("-gui")) {
				if (args[i + 1] == null) {
					logger.debug("Starte GUI");
					HandleValues vGUI = new HandleValues(initProgram.getLsa(),
							initProgram.getAbsImu(), initProgram.getEopdLeft(),
							initProgram.getEopdRight(),
							initProgram.getDistNx(), initProgram.getThermal());
					logger.info("GUI gestartet");
					vGUI.start();
				} else {
					i++;
					s = args[i];
					if (s.equals("navigation")) {
						logger.debug("Starte Navigation-GUI");
						GuiNavigation navGUI = new GuiNavigation();
						logger.info("Navigation-GUI gestartet");
					}
				}
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
				} else if (s.equals("flash")) {
					logger.debug("Starte Testprogramm flash");
					testProgram.flash();
				}
			}

		}
	}
}