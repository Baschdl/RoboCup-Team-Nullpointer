import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.null_pointer.communication_pi.BrickControlPiTest;

@RunWith(Suite.class)
// In die geschweiften Klammern muessen weitere Testklassen mit Kommas getrennt
// eingetragen werden
@SuiteClasses({ BrickControlPiTest.class })
public class AllTests {

}
