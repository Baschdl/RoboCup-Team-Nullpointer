package de.null_pointer.sensorprocessing_pi;

import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.*;

public class Abs_ImuProcessingPiTest {
	Properties props = new Properties();

	Abs_ImuProcessingPi classToTest;
	
	@Test
	public void headingTestTwo(){
		props.setProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_horizontal", "2");
		props.setProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_vertical", "1");
		props.setProperty("SensorProcessing_Pi.Abs_ImuProcessingPi.dimension_rotational", "0");
		
		classToTest = new Abs_ImuProcessingPi(props);
		
		classToTest.setAngle(-40, 2);
		assertEquals(0,classToTest.getAbsImuHeading());
		
	}
	
}
