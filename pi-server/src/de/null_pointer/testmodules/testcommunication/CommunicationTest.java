package de.null_pointer.testmodules.testcommunication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import de.null_pointer.communication_pi.CommunicationPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.pi_server.InitializeProgram;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.testmodules.testbehavior.TestBlackTile;
import de.null_pointer.testmodules.testbehavior.TestIntersection;
import de.null_pointer.testmodules.testbehavior.TestMovingForward;
import de.null_pointer.testmodules.testbehavior.TestNextTile;
import de.null_pointer.testmodules.testbehavior.TestSlope;
import de.null_pointer.testmodules.testbehavior.TestVictim;
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
	private VirtualDistNX virtDistNX = new VirtualDistNX(800, 10);
	// TODO: Richtige Nummern verwenden
	private VirtualEOPD virtEOPDLeft = new VirtualEOPD(0);
	private VirtualEOPD virtEOPDRight = new VirtualEOPD(1);
	// TODO: Richtige Werte verwenden
	private VirtualLSA virtLSA = new VirtualLSA(100, 80, 40, 0);

	private CommunicationPi comPi = null;
	private Abs_ImuProcessingPi absImu = null;
	private DistNxProcessingPi distNx = null;
	private EOPDProcessingPi eopdLeft = null;
	private EOPDProcessingPi eopdRight = null;
	private LSAProcessingPi lsa = null;
	private MotorControlPi motorControl = null;
	private Navigation nav = null;

	private TestBlackTile testBlackTile = null;
	private TestIntersection testInters = null;
	private TestMovingForward testMovFor = null;
	private TestNextTile testNextTile = null;
	private TestSlope testSlope = null;
	private TestVictim testVictim = null;

	@Before
	public void prepareTest() {
		initProgramm = new InitializeProgram(logger);
		initProgramm.initializeLogger();
		initProgramm.initializeCommunication();
		initProgramm.initializeSensors();
		initProgramm.initializeNavigation();
		initProgramm.initializeBehavior();

		comPi = initProgramm.getComPi();
		absImu = initProgramm.getAbsImu();
		distNx = initProgramm.getDistNx();
		eopdLeft = initProgramm.getEopdLeft();
		eopdRight = initProgramm.getEopdRight();
		lsa = initProgramm.getLsa();
		motorControl = initProgramm.getMotorControl();
		nav = initProgramm.getNav();

		testBlackTile = new TestBlackTile(motorControl, lsa, nav);
		testInters = new TestIntersection(motorControl, distNx, eopdLeft,
				eopdRight, nav);
		testMovFor = new TestMovingForward(motorControl);
		testNextTile = new TestNextTile(absImu, nav);
		testSlope = new TestSlope(motorControl, absImu, nav);
		testVictim = new TestVictim(motorControl);

		brickcontrol = new TestBrickControlPi(comPi, absImu, distNx, eopdLeft,
				eopdRight, lsa);
	}

	@Test
	public void testMovingForward() {
		ArrayList<ArrayList<String>> sensorData = new ArrayList<ArrayList<String>>();
		sensorData.add(new ArrayList<String>(Arrays.asList(virtAbsImu
				.getDrivingForward())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtDistNX.getOK())));
		sensorData.add(new ArrayList<String>(
				Arrays.asList(virtEOPDLeft.getOK())));
		sensorData.add(new ArrayList<String>(Arrays.asList(virtEOPDRight
				.getOK())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(0))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(1))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(2))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(3))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(4))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(5))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(6))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(7))));
		ArrayList<String> comStrings = makeOneDimensionalList(sensorData);
		sendData(comStrings);
		if (testMovFor.takeControl()) {
			testMovFor.action();
		}
		assertEquals(true, testMovFor.isActive());
	}

	@Test
	public void testBlackTile() {
		// Warte auf andere Implemntierung der VirtualLSA-Klasse
		fail("Not yet implemented");
	}

	@Test
	public void testVictim() {
		// Setzt Thermal-Sensor vorraus
		fail("Not yet implemented");
	}

	@Test
	public void testSlope() {
		ArrayList<ArrayList<String>> sensorData = new ArrayList<ArrayList<String>>();
		sensorData.add(new ArrayList<String>(
				Arrays.asList(virtAbsImu.getRamp())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtDistNX.getOK())));
		sensorData.add(new ArrayList<String>(
				Arrays.asList(virtEOPDLeft.getOK())));
		sensorData.add(new ArrayList<String>(Arrays.asList(virtEOPDRight
				.getOK())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(0))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(1))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(2))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(3))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(4))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(5))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(6))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(7))));
		ArrayList<String> comStrings = makeOneDimensionalList(sensorData);
		sendData(comStrings);
		if (testSlope.takeControl()) {
			testSlope.action();
		}
		assertEquals(true, testSlope.isActive());
	}

	@Test
	public void testWallFrontClose() {
		ArrayList<ArrayList<String>> sensorData = new ArrayList<ArrayList<String>>();
		sensorData.add(new ArrayList<String>(Arrays.asList(virtAbsImu
				.getDrivingForward())));
		sensorData.add(new ArrayList<String>(Arrays.asList(virtDistNX
				.getNoSpaceInFront())));
		sensorData.add(new ArrayList<String>(
				Arrays.asList(virtEOPDLeft.getOK())));
		sensorData.add(new ArrayList<String>(Arrays.asList(virtEOPDRight
				.getOK())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(0))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(1))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(2))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(3))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(4))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(5))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(6))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(7))));
		ArrayList<String> comStrings = makeOneDimensionalList(sensorData);
		sendData(comStrings);
		if (testInters.takeControl()) {
			testInters.action();
		}
		// TODO: Weitere Ueberpruefung, ob richtiger Teil des Behaviors
		// anspricht
		assertEquals(true, testInters.isActive());
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
		ArrayList<ArrayList<String>> sensorData = new ArrayList<ArrayList<String>>();
		sensorData.add(new ArrayList<String>(Arrays.asList(virtAbsImu
				.getDrivingForward())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtDistNX.getOK())));
		sensorData.add(new ArrayList<String>(Arrays.asList(virtEOPDLeft
				.getIntersection())));
		sensorData.add(new ArrayList<String>(Arrays.asList(virtEOPDRight
				.getOK())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(0))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(1))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(2))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(3))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(4))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(5))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(6))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(7))));
		ArrayList<String> comStrings = makeOneDimensionalList(sensorData);
		sendData(comStrings);
		if (testInters.takeControl()) {
			testInters.action();
		}
		// TODO: Weitere Ueberpruefung, ob richtiger Teil des Behaviors
		// anspricht
		assertEquals(true, testInters.isActive());
	}

	@Test
	public void testIntersectionRight() {
		ArrayList<ArrayList<String>> sensorData = new ArrayList<ArrayList<String>>();
		sensorData.add(new ArrayList<String>(Arrays.asList(virtAbsImu
				.getDrivingForward())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtDistNX.getOK())));
		sensorData.add(new ArrayList<String>(
				Arrays.asList(virtEOPDLeft.getOK())));
		sensorData.add(new ArrayList<String>(Arrays.asList(virtEOPDRight
				.getIntersection())));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(0))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(1))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(2))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(3))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(4))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(5))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(6))));
		sensorData
				.add(new ArrayList<String>(Arrays.asList(virtLSA.getWhite(7))));
		ArrayList<String> comStrings = makeOneDimensionalList(sensorData);
		sendData(comStrings);
		if (testInters.takeControl()) {
			testInters.action();
		}
		// TODO: Weitere Ueberpruefung, ob richtiger Teil des Behaviors
		// anspricht
		assertEquals(true, testInters.isActive());
	}

	private ArrayList<String> makeOneDimensionalList(
			ArrayList<ArrayList<String>> sensorData) {
		ArrayList<String> data = new ArrayList<>(50);
		int sensors = sensorData.size();
		int maxNumberData = 0;
		int tempMaxNumberData = 0;
		for (int i = 0; i < sensors; i++) {
			if ((tempMaxNumberData = sensorData.get(i).size()) > maxNumberData) {
				maxNumberData = tempMaxNumberData;
			}
		}
		for (int j = 0; j < maxNumberData; j++) {
			for (int i = 0; i < sensors; i++) {
				if (j < (sensorData.get(i).size())) {
					data.add(sensorData.get(i).get(j));
				} else if (j == (sensorData.get(i).size())) {
					sensorData.remove(i);
					sensors--;
				}
			}
		}
		return data;
	}

	private void sendData(ArrayList<String> comStrings) {
		for (String data : comStrings) {
			comPi.sendString(data);
		}

	}

}
