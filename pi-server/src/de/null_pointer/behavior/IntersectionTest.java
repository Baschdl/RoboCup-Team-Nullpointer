package de.null_pointer.behavior;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import de.null_pointer.communication_pi.VirtualCommunicationPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.AccumulatorProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.testmodules.testcommunication.TestBrickControlPi;

public class IntersectionTest {
	Properties props = new Properties();
	
	DistNxProcessingPi distNx = new DistNxProcessingPi();
	Abs_ImuProcessingPi abs_Imu = null;
	EOPDProcessingPi eopdLeft = new EOPDProcessingPi();
	EOPDProcessingPi eopdRight = new EOPDProcessingPi();
	LSAProcessingPi lsa = new LSAProcessingPi();

	AccumulatorProcessingPi accumulator = new AccumulatorProcessingPi();

	VirtualCommunicationPi com = null;
	TestBrickControlPi brickCon1 = null;
	TestBrickControlPi brickCon2 = null;

	MotorControlPi motorControl = null;

	Intersection classToTest = null;
	
	@Before
	public void initializeTests(){
		com = new VirtualCommunicationPi();
		brickCon1 =  new TestBrickControlPi(com, abs_Imu, distNx, eopdLeft, eopdRight, lsa, accumulator);
		brickCon2 = new TestBrickControlPi(com, abs_Imu, distNx, eopdLeft, eopdRight, lsa, accumulator);
		motorControl = new MotorControlPi(brickCon1, brickCon2);
		
		props.setProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_horizontal", "2");
		props.setProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_vertical", "1");
		props.setProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_rotational", "0");
		props.setProperty("Behavior.Intersection.minimalDistanceFront", "10");
		props.setProperty("Behavior.Intersection.maximalDistanceSide", "20");
		
		abs_Imu = new Abs_ImuProcessingPi(props);
		classToTest = new Intersection(motorControl, distNx, eopdLeft,
				eopdRight, abs_Imu, null, props); // null == nav
	}
	

	@Test
	public void testTakeControlWallAhead() {
		distNx.setTestDistance(1);
		assertEquals(true, classToTest.takeControl());
	}
	
	@Test
	public void testTakeControlFailNegative(){
		distNx.setTestDistance(-1);
		assertEquals(false, classToTest.takeControl());
	}
	
	@Test
	public void testTakeControlHallwayLeft(){
		eopdLeft.setTestEOPDdistance(21.0);
		assertEquals(true,classToTest.takeControl());
	}
	
	@Test
	public void testTakeControlHallwayRight(){
		eopdRight.setTestEOPDdistance(21.0);
		assertEquals(true, classToTest.takeControl());
	}

	@Test
	public void testAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testSuppress() {
		fail("Not yet implemented");
	}

}
