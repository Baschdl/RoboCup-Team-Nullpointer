package de.null_pointer.testmodules.testcommunication;

import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import de.null_pointer.pi_server.InitializeProgram;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.testmodules.virtualhardware.VirtualAbsIMUACG;
import de.null_pointer.testmodules.virtualhardware.VirtualDistNX;
import de.null_pointer.testmodules.virtualhardware.VirtualEOPD;
import de.null_pointer.testmodules.virtualhardware.VirtualLSA;

public class CommunicationTest {
	private static Logger logger = Logger.getLogger(CommunicationTest.class);

	InitializeProgram initProgramm = null;

	TestBrickControlPi brickcontrol = null;

	private VirtualAbsIMUACG virtAbsImu = new VirtualAbsIMUACG();
	// TODO: Richtige Werte verwenden
	private VirtualDistNX virtDistNX = new VirtualDistNX(-1, -1);
	// TODO: Richtige Nummern verwenden
	private VirtualEOPD virtEOPDLeft = new VirtualEOPD(0);
	private VirtualEOPD virtEOPDRight = new VirtualEOPD(1);
	// TODO: Richtige Werte verwenden
	private VirtualLSA virtLSA = new VirtualLSA(-1, -1, -1, -1, -1);

	@Before
	public void prepareTest() {
		initProgramm = new InitializeProgram(logger);
		initProgramm.initializeLogger();
		initProgramm.initializeCommunication();
		initProgramm.initializeSensors();
		initProgramm.initializeNavigation();
		initProgramm.initializeBehavior();

		brickcontrol = new TestBrickControlPi(initProgramm.getComPi(),
				initProgramm.getAbsImu(), initProgramm.getDistNx(),
				initProgramm.getEopdLeft(), initProgramm.getEopdRight(),
				initProgramm.getLsa());
	}

	@Test
	public void testMovingForward() {
		fail("Not yet implemented");
	}

	@Test
	public void testBlackTile() {
		fail("Not yet implemented");
	}

	@Test
	public void testVictim() {
		fail("Not yet implemented");
	}

	@Test
	public void testSlope() {
		fail("Not yet implemented");
	}

	@Test
	public void testWallFrontClose() {
		fail("Not yet implemented");
	}

	@Test
	public void testWallLeftClose() {
		fail("Not yet implemented");
	}

	@Test
	public void testWallRightClose() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntersectionLeft() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntersectionRight() {
		fail("Not yet implemented");
	}

}
