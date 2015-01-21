package de.null_pointer.behavior;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
	DistNxProcessingPi distNx = new DistNxProcessingPi(); // Sensoren aus
															// sensorprocessingPi
	Abs_ImuProcessingPi abs_Imu = new Abs_ImuProcessingPi(null);
	EOPDProcessingPi eopdLeft = new EOPDProcessingPi();
	EOPDProcessingPi eopdRight = new EOPDProcessingPi();
	LSAProcessingPi lsa = new LSAProcessingPi();

	// Properties propPiServer =
	// loadConfiguration("resources/pi_server.properties");

	AccumulatorProcessingPi accumulator = new AccumulatorProcessingPi();

	VirtualCommunicationPi com = new VirtualCommunicationPi();
	TestBrickControlPi brickCon1 = new TestBrickControlPi(com, abs_Imu, distNx,
			eopdLeft, eopdRight, lsa, accumulator);
	TestBrickControlPi brickCon2 = new TestBrickControlPi(com, abs_Imu, distNx,
			eopdLeft, eopdRight, lsa, accumulator);

	MotorControlPi motorControl = new MotorControlPi(brickCon1, brickCon2);

	Intersection classToTest = new Intersection(motorControl, distNx, eopdLeft,
			eopdRight, abs_Imu, null, null); // null == nav; null2 = prop

	@Test
	public void testTakeControl() {
		distNx.setTestDistance(0);
		// assertEquals(Intersection.);

		fail("Not yet implemented");
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
