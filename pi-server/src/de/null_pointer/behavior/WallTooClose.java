package de.null_pointer.behavior;

import java.util.Properties;

import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Logger;

import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;

public class WallTooClose implements Behavior {
	private static Logger logger = Logger.getLogger(WallTooClose.class);
	private Properties propPiServer = null;
	private MotorControlPi motorControl = null;
	private EOPDProcessingPi eopdLeft = null;
	private EOPDProcessingPi eopdRight = null;
	private Odometer odometer = null;
	private Abs_ImuProcessingPi absImu = null;
	private double minDistanceSide;
	private double maxDistanceSide;
	private long time;
	private int speed;
	private int slopeSpeed;
	private int percentOfSpeed;
	private int angleToTakeControl;
	
	private boolean suppress = false;
	

	public WallTooClose(EOPDProcessingPi eopdRight, EOPDProcessingPi eopdLeft,
			MotorControlPi motorControl, Odometer odometer, Properties propPiServer, Abs_ImuProcessingPi absImu) {
		this.propPiServer = propPiServer;
		this.motorControl = motorControl;
		this.eopdLeft = eopdLeft;
		this.eopdRight = eopdRight;
		this.odometer = odometer;
		this.absImu = absImu;
		
		minDistanceSide = Double.parseDouble(propPiServer
				.getProperty("Behavior.WallTooClose.minimalDistanceSide"));
		maxDistanceSide = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.maximalDistanceSide"));
		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.MovingForward.speed"));
		percentOfSpeed = Integer
				.parseInt(propPiServer
						.getProperty("Behavior.WallTooClose.percentOfOldSpeedForCorrection"));
		slopeSpeed = Integer.parseInt(propPiServer.getProperty("Behavior.Slope.speed"));
		angleToTakeControl = Integer.parseInt(propPiServer
				.getProperty("Behavior.Slope.angleToTakeControl"));

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
			if(absImu.getTiltDataVertical() > angleToTakeControl){
				logger.debug("korrigiere Fahrtrichtung (zu nah an linker Wand auf Rampe)");
				motorControl.changeSpeedSingleMotorForward(2, 'A', slopeSpeed + slopeSpeed
						* percentOfSpeed / 100);
			}else{
			logger.debug("korrigiere Fahrtrichtung (zu nah an linker Wand)");
			motorControl.changeSpeedSingleMotorForward(2, 'A', speed + speed
					* percentOfSpeed / 100);
			}
		} else if (eopdLeft.getDistance() >= minDistanceSide
				&& eopdLeft.getDistance() < maxDistanceSide) {
			if(absImu.getTiltDataVertical() > angleToTakeControl){
				logger.debug("korrigiere Fahrtrichtung (zu nah an rechter Wand auf Rampe)");
				motorControl.changeSpeedSingleMotorForward(2, 'B', slopeSpeed + slopeSpeed
						* percentOfSpeed / 100);
			}else{
			logger.debug("korrigiere Fahrtrichtung (zu nah an rechter Wand)");
			motorControl.changeSpeedSingleMotorForward(2, 'B', speed + speed
					* percentOfSpeed / 100);
			}
		}

		while (!suppress
				&& ((eopdRight.getDistance() >= minDistanceSide && eopdRight
						.getDistance() < maxDistanceSide) || (eopdLeft
						.getDistance() >= minDistanceSide && eopdLeft
						.getDistance() < maxDistanceSide))) {
			odometer.calculateDistance(time, speed);
			time = System.currentTimeMillis();
			try {
				Thread.sleep(2);
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
