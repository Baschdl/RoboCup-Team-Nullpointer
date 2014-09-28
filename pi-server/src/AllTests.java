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

@RunWith(Suite.class)
// In die geschweiften Klammern muessen weitere Testklassen mit Kommas getrennt
// eingetragen werden
@SuiteClasses({ BrickControlPiTest.class, NodeTest.class,
		TestBrickControlPiTest.class, MovingForwardTest.class,
		BlackTileTest.class, IntersectionTest.class, NextTileTest.class,
		SlopeTest.class, VictimTest.class })
public class AllTests {

}
