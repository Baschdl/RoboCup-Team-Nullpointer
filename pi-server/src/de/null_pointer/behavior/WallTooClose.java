package de.null_pointer.behavior;

import java.util.Properties;

import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Logger;

import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;

public class WallTooClose implements Behavior {
	private static Logger logger = Logger.getLogger(WallTooClose.class);
	private Properties propPiServer = null;
	private MotorControlPi motorControl = null;
	private EOPDProcessingPi eopdLeft = null;
	private EOPDProcessingPi eopdRight = null;
	private Odometer odometer = null;
	private double minDistanceSide;
	private double maxDistanceSide;
	private long time;
	private int speed;
	private int percentOfSpeed;
	private boolean suppress = false;

	public WallTooClose(EOPDProcessingPi eopdRight, EOPDProcessingPi eopdLeft,
			MotorControlPi motorControl, Odometer odometer, Properties propPiServer) {
		this.propPiServer = propPiServer;
		this.motorControl = motorControl;
		this.eopdLeft = eopdLeft;
		this.eopdRight = eopdRight;
		this.odometer = odometer;
		minDistanceSide = Double.parseDouble(propPiServer
				.getProperty("Behavior.WallTooClose.minimalDistanceSide"));
		maxDistanceSide = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.maximalDistanceSide"));
		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.MovingForward.speed"));
		percentOfSpeed = Integer
				.parseInt(propPiServer
						.getProperty("Behavior.WallTooClose.percentOfOldSpeedForCorrection"));

	}

	@Override
	public boolean takeControl() {
		if ((eopdRight.getDistance() >= minDistanceSide && eopdRight
				.getDistance() < maxDistanceSide)
				|| eopdLeft.getDistance() >= minDistanceSide
				&& eopdLeft.getDistance() < maxDistanceSide) {
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		suppress = false;
		time = 0;
		logger.debug("korrigiere Fahrtrichtung (zu nah an einer Wand)");
		if (eopdRight.getDistance() >= minDistanceSide
				&& eopdRight.getDistance() < maxDistanceSide) {
			logger.debug("korrigiere Fahrtrichtung (zu nah an linker Wand)");
			motorControl.changeSpeedSingleMotorForward(2, 'A', speed + speed
					* percentOfSpeed / 100);
		} else if (eopdLeft.getDistance() >= minDistanceSide
				&& eopdLeft.getDistance() < maxDistanceSide) {
			logger.debug("korrigiere Fahrtrichtung (zu nah an rechter Wand)");
			motorControl.changeSpeedSingleMotorForward(2, 'B', speed + speed
					* percentOfSpeed / 100);
		}

		while (!suppress
				&& ((eopdRight.getDistance() >= minDistanceSide && eopdRight
						.getDistance() < maxDistanceSide) || (eopdLeft
						.getDistance() >= minDistanceSide && eopdLeft
						.getDistance() < maxDistanceSide))) {
			odometer.calculateDistance(time, speed);
			time = System.currentTimeMillis();
			try {
				Thread.sleep(2); // TODO mit S+J abklaeren
			} catch (InterruptedException e) {
				logger.fatal("InterruptedException while sleep()");
			}
			time = System.currentTimeMillis() - time;
		}
	}

	@Override
	public void suppress() {
		suppress = true;

	}

}
