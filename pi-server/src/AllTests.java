import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.null_pointer.behavior.BlackTileTest;
import de.null_pointer.behavior.IntersectionTest;
import de.null_pointer.behavior.MovingForwardTest;
import de.null_pointer.behavior.NextTileTest;
import de.null_pointer.behavior.SlopeTest;
import de.null_pointer.behavior.VictimTest;
import de.null_pointer.communication_pi.BrickControlPiTest;
import de.null_pointer.navigation.map.NodeTest;
import de.null_pointer.testmodules.testcommunication.TestBrickControlPiTest;
import de.null_pointer.testmodules.testcommunication.TestCommunication;

@RunWith(Suite.class)
// In die geschweiften Klammern muessen weitere Testklassen mit Kommas getrennt
// eingetragen werden
@SuiteClasses({ BrickControlPiTest.class, NodeTest.class,
		TestBrickControlPiTest.class, MovingForwardTest.class,
		BlackTileTest.class, IntersectionTest.class, NextTileTest.class,
		SlopeTest.class, VictimTest.class, TestCommunication.class })
public class AllTests {

	private static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void setUp() {
		// Anlegen und Einrichten des Loggers

		// SimpleLayout layout = new SimpleLayout();
		PatternLayout layout = new PatternLayout("%d{ISO8601} %-6p [%c] %m%n");
		ConsoleAppender consoleAppender = new ConsoleAppender(layout);
		logger.addAppender(consoleAppender);

		// Spezifiziert welche Meldungen alles ausgegeben werden
		// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
		logger.setLevel(Level.INFO);
	}
}
