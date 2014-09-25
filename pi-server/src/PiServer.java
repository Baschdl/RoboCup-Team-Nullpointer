import java.util.logging.FileHandler;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

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
		logger.setLevel(Level.ALL);

		logger.info("starting programm");

		// Entscheidet anhand der Uebergabeparameter beim Start welche Programmteile ausgeführt werden
		for (String s : args) {
			// TODO: Aufruf des Wettkampfprogrammes implementieren, muss das
			// immer ausgeführt werden? Oder wollen wir die Möglichkeit haben auch nur die Werte auszugeben?
			
			// comp steht für competition, führt das Wettkampfprogramm aus
			// if (s.equals("-comp")) {
			//
			// }

			// ruft die GUI auf
			// else
			if (s.equals("-gui")) {

			}
		}
	}
}