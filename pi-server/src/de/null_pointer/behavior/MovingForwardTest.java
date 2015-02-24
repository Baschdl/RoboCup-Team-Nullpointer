package de.null_pointer.behavior;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import de.null_pointer.communication_pi.VirtualCommunicationPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.AccumulatorProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import de.null_pointer.sensorprocessing_pi.ThermalSensorProcessingPi;
import de.null_pointer.testmodules.testcommunication.TestBrickControlPi;

public class MovingForwardTest {

	Properties props = new Properties();

	Abs_ImuProcessingPi abs_Imu = null;
	DistNxProcessingPi distNx = null;
	EOPDProcessingPi eopdLeft = null;
	EOPDProcessingPi eopdRight = null;
	LSAProcessingPi lsa = null;
	AccumulatorProcessingPi accumulator = null;
	ThermalSensorProcessingPi thermal = null;
	Odometer odom = null;

	TestBrickControlPi brickCon1 = null;
	TestBrickControlPi brickCon2 = null;
	MotorControlPi motorControl = null;
	VirtualCommunicationPi com = null;

	MovingForward classToTest = null;

	@Before
	public void initializeMovingForwardTest() {
		props.setProperty("Behavior.MovingForward.speed", "200");
		props.setProperty(
				"SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_horizontal",
				"2");
		props.setProperty(
				"SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_vertical",
				"1");
		props.setProperty(
				"SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_rotational",
				"0");
		props.setProperty("Navigation.Odometer.wheelRadius", "2.0");
		abs_Imu = new Abs_ImuProcessingPi(props);
		distNx = new DistNxProcessingPi();
		eopdLeft = new EOPDProcessingPi();
		eopdRight = new EOPDProcessingPi();
		lsa = new LSAProcessingPi();
		accumulator = new AccumulatorProcessingPi();
		thermal = new ThermalSensorProcessingPi();
		odom = new Odometer(accumulator, abs_Imu, props);

		com = new VirtualCommunicationPi();
		brickCon1 = new TestBrickControlPi(com, null, abs_Imu, distNx,
				eopdLeft, eopdRight, lsa, accumulator, thermal); // null == nav
		brickCon2 = new TestBrickControlPi(com, null, abs_Imu, distNx,
				eopdLeft, eopdRight, lsa, accumulator, thermal); // null == nav

		motorControl = new MotorControlPi(brickCon1, brickCon2);

		classToTest = new MovingForward(motorControl, odom, props);

	}

	@Test
	public void testTakeControl() {
		assertEquals(true, classToTest.takeControl());
	}

	@Test
	public void testActionWithoutLoop() {
		classToTest.testActionWithoutLoop();
		assertEquals(0, motorControl.getMode());
	}

	@Test
	public void testSuppress() {
		classToTest.testSetMoving(false);
		classToTest.suppress();
		assertEquals(false, classToTest.testGetMoving());
	}

}
