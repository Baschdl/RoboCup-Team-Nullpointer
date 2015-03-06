package de.null_pointer.pi_server;

import java.util.Properties;

import lejos.util.Delay;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.EmulateSensordata;
import de.null_pointer.gui.HandleValues;
import de.null_pointer.navigation.test.NavCompetitionHandler;
import de.null_pointer.navigation.test.NavSimulationHandler;

/**
 * Program for our robot to participate in the RoboCup Junior 2015 Rescue B.
 * </br> <u>Our hardware setup:</u> </br> - Raspberry Pi B+</br> - 2 Lego
 * Mindstorms Bricks</br> - 4 motors</br> - different sensors </br> </br>
 * <u>Software setup:</u> </br> required libraries: </br> - JRE </br> - JUnit 4
 * </br> - LeJOS PC Libraries </br> - log4j-1.2.17 (in /lib) </br> -
 * org.eclipse.jdt.annotation_1.1.0.v20130513-1648 (in /lib) </br> </br>
 * 
 * Classpath-User Entries: /pi-server/src/resources/
 * 
 * @author Sebastian Bischoff (sebastianbischoff@null-pointer.de) ("Baschdl" on
 *         the leJOS-Forum), Jan Krebes (jankrebes@null-pointer.de), Samuel
 *         Scherer (samuelscherer@null-pointer.de)
 * 
 * @see <a href="http://www.null-pointer.de/">Our project website</a>
 * 
 */

public class PiServer {

	private static Logger logger = Logger.getLogger("TST.SIM");
	private static boolean compProgramStarted = false;
	private static InitializeProgram initProgram = new InitializeProgram(logger);

	public static void main(String[] args) {
		initProgram.initializeLogger();
		Properties propPiServer = initProgram.getPropPiServer();

		initProgram.initializeSensors();
		initProgram.initializeCommunication();
		NavCompetitionHandler navComp = new NavCompetitionHandler();
		if (args[0].equals("nav")) {
			initProgram.setNavCompetitionHandler(navComp);
		}
		initProgram.initializeNavigation();

		TestProgram testProgram = new TestProgram(initProgram, propPiServer);

		logger.info("starting programm");

		// Entscheidet anhand der Uebergabeparameter beim Start welche
		// Programmteile ausgefuehrt werden
		String s = null;
		for (int i = 0; i < args.length; i++) {
			s = args[i];
			// ruft die GUI auf
			if (s.equals("-gui")) {

				i++;
				s = args[i];
				if (s.equals("navigation")) {
					/*
					 * Wenn der Parameter "-comp" uebergeben wurde, soll sich
					 * die GUI oeffnen, die die Karte live anzeigt
					 */
					boolean comParam = false;
					for (int j = 0; j < args.length; j++) {
						if (args[j].equals("-comp")) {
							comParam = true;
							break;
						}
					}

					logger.debug("Starte Navigation-GUI");
					if (comParam == false) {
						// test gui
						NavSimulationHandler navSim = new NavSimulationHandler(
								19, 19, true);
					} else {
						// competition gui
						navComp.createGui();
					}
					logger.info("Navigation-GUI gestartet");

				} else if (s.equals("normal")) {
					logger.debug("Starte Werte-GUI");
					/*
					 * Wenn der Parameter "comp" vor "-gui normal" uebergeben
					 * wurde, soll die Navigation nicht initialisiert werden
					 */
					// boolean comParam = false;
					// for (int j = 0; j < args.length && j < i; j++) {
					// if (args[j].equals("-comp")) {
					// comParam = true;
					// break;
					// }
					// }
					// if (comParam == false) {
					// initProgram.initializeNavigation();
					// }

					HandleValues vGUI = new HandleValues(initProgram.getLsa(),
							initProgram.getAbsImu(), initProgram.getEopdLeft(),
							initProgram.getEopdRight(),
							initProgram.getDistNx(), initProgram.getThermal(),
							initProgram.getOdometer(),
							initProgram.getMotorControl(),
							initProgram.getBrickCon1(),
							initProgram.getBrickCon2());
					logger.info("GUI gestartet");
					vGUI.start();
				}
			}

			// comp steht fuer competition, fuehrt das Wettkampfprogramm aus
			if (s.equals("-comp")) {
				startCompetitionProgram();
			}

			// startet ein Testprogramm, der nachfolgende String gibt an welches
			if (s.equals("-test")) {
				logger.debug("Waehle Testprogramm aus");
				i++;
				s = args[i];

				if (s.equals("forward")) {
					logger.debug("Starte Testprogramm forward");

					testProgram.forward(Integer.parseInt(args[i + 1]),
							Integer.parseInt(args[i + 2]));
					i += 2;
				} else if (s.equals("backward")) {
					logger.debug("Starte Testprogramm backward");
					testProgram.backward(Integer.parseInt(args[i + 1]),
							Integer.parseInt(args[i + 2]));
					i += 2;
				} else if (s.equals("rightturn")) {
					logger.debug("Starte Testprogramm rightturn");
					testProgram.rightturn(Integer.parseInt(args[i + 1]));
					i++;
				} else if (s.equals("leftturn")) {
					logger.debug("Starte Testprogramm leftturn");
					testProgram.leftturn(Integer.parseInt(args[i + 1]));
					i++;
				} else if (s.equals("flash")) {
					logger.debug("Starte Testprogramm flash");
					testProgram.flash();
				} else if (s.equals("comp")) {
					logger.debug("Starte Testprogramm comp");
					startCompetitionProgram();
					EmulateSensordata emulSensor = new EmulateSensordata(
							initProgram.getBrickCon1(),
							initProgram.getBrickCon2());
				}
				logger.info("Testprogramm gestartet");
			}

			if (i == (args.length - 1) && compProgramStarted) {
				while (!initProgram.getProgramStarted()) {
					Delay.msDelay(250);
				}
				initProgram.getArbitrator().start();
				logger.info("Programm durch Betaetigen des Enter-Buttons auf dem Brick gestartet");
			}

		}
	}

	private static void startCompetitionProgram() {
		logger.debug("Starte Wettkampfprogramm");

		/*
		 * Wenn der Parameter "-gui normal" vor "comp" uebergeben wurde, soll
		 * die Navigation nicht initialisiert werden
		 */
		// boolean guiNormalParam = false;
		// for (int j = 0; j < args.length && j < i; j++) {
		// if (args[j].equals("-gui") && args[j + 1].equals("normal")) {
		// guiNormalParam = true;
		// break;
		// }
		// }
		// if (guiNormalParam == false) {
		// initProgram.initializeNavigation();
		// }
		logger.debug("Initialize behavior...");
		initProgram.initializeBehavior();
		initProgram.getArbitrator().setDaemon(true);
		logger.info("Wettkampfprogramm gestartet");
		compProgramStarted = true;

	}
}