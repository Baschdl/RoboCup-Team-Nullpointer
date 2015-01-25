package de.null_pointer.navigation.test;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

// keine saubere Trennung: Integration-Tests und Unit-Test
public class NavigationIntegrationTest {
	static Handler handler = null;

	@Rule
	public CatchAllExceptionsRule catchAllExceptionsRule = new CatchAllExceptionsRule();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		handler = new Handler(19, 19);
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
		handler.reset(true);
	}

	@Test
	public void testLeftTurnException() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/turn-left.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 8100ms
			Thread.sleep(10000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}

	@Test
	public void testRightTurnException() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/turn-right.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 8100ms
			Thread.sleep(10000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}

	@Test
	public void testLabyrinth1Exception() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/lab1.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 8100ms
			Thread.sleep(10000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}
	
	@Test
	public void testLabyrinth2Exception() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/lab2.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 8100ms
			Thread.sleep(10000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}
	
	@Test
	public void testOneWayException() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/one-way.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 8100ms
			Thread.sleep(10000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}
	
	@Test
	public void testBottomLeftException() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/bottom-left-corner.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 800ms
			Thread.sleep(1000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}
	
	@Test
	public void testBottomRightException() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/bottom-right-corner.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 800ms
			Thread.sleep(1000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}
	
	@Test
	public void testTopRightException() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/top-right-corner.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 800ms
			Thread.sleep(1000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}
	
	@Test
	public void testTopLeftException() {
		try {
			handler.getFileHandler().loadFile(
					new File("").getAbsolutePath()
							+ "/src/resources/maps/top-left-corner.map");
			handler.startTimer();

			// maximale Ausfuehrungszeit sind 800ms
			Thread.sleep(1000);

			handler.stopTimer();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Should not have thrown any exception");
		}
	}

}
