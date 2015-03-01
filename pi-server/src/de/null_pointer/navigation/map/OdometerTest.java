package de.null_pointer.navigation.map;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Properties;

public class OdometerTest {

	Properties props = new Properties();

	Odometer odometer = null;

	@Test
	public void TestDistanceCounter() {
		props.setProperty("Navigation.Odometer.wheelRadius", "2.4");
		double time = 0;
		odometer = new Odometer(null, null, props);
		while (odometer.getDistanceCounter() <= 30) {
			odometer.calculateDistance(time, 200);
			time = System.currentTimeMillis();
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
			}
			time = System.currentTimeMillis() - time;
		}
		assertEquals(30, odometer.getDistanceCounter(), 2);
	}
}
