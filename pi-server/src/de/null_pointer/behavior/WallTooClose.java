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
	private double minDistanceSideEOPDRight;
	private double minDistanceSideEOPDLeft;
	private double maxDistanceSideEOPDRight;
	private double maxDistanceSideEOPDLeft;
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
		
		minDistanceSideEOPDRight = Double.parseDouble(propPiServer
				.getProperty("Behavior.WallTooClose.minimalDistanceSideEOPDRight"));
		minDistanceSideEOPDLeft = Double.parseDouble(propPiServer
				.getProperty("Behavior.WallTooClose.minimalDistanceSideEOPDLeft"));
		maxDistanceSideEOPDRight = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.maximalDistanceSideEOPDRight"));
		maxDistanceSideEOPDLeft = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.maximalDistanceSideEOPDLeft"));
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
		logger.debug("takeControl: Running;");
		if ((eopdRight.getDistance() >= minDistanceSideEOPDRight && eopdRight
				.getDistance() < maxDistanceSideEOPDRight)
				|| eopdLeft.getDistance() >= minDistanceSideEOPDLeft
				&& eopdLeft.getDistance() < maxDistanceSideEOPDLeft) {
			logger.info("takeControl: Calling action: YES;");
			return true;
		}
		logger.debug("takeControl: Calling action: NO;");
		return false;
	}

	@Override
	public void action() {
		logger.info("action: Running;");
		suppress = false;
		time = 0;
		logger.debug("action: Correcting driving direction;");
		if (eopdRight.getDistance() >= minDistanceSideEOPDRight
				&& eopdRight.getDistance() < maxDistanceSideEOPDRight) {
			if(absImu.getTiltDataVertical() > angleToTakeControl){
				logger.debug("action: Correcting driving direction; On slope; Left wall too near;");
				motorControl.changeSpeedSingleMotorForward(2, 'A', slopeSpeed + slopeSpeed
						* percentOfSpeed / 100);
			}else{
			logger.debug("action: Correcting driving direction; Left wall too near;");
			motorControl.changeSpeedSingleMotorForward(2, 'A', speed + speed
					* percentOfSpeed / 100);
			}
		} else if (eopdLeft.getDistance() >= minDistanceSideEOPDLeft
				&& eopdLeft.getDistance() < maxDistanceSideEOPDLeft) {
			if(absImu.getTiltDataVertical() > angleToTakeControl){
				logger.debug("action: Correcting driving direction; On slope; Right wall too near;");
				motorControl.changeSpeedSingleMotorForward(2, 'B', slopeSpeed + slopeSpeed
						* percentOfSpeed / 100);
			}else{
				logger.debug("action: Correcting driving direction; Right wall too near;");
			motorControl.changeSpeedSingleMotorForward(2, 'B', speed + speed
					* percentOfSpeed / 100);
			}
		}
		logger.debug("action: Correcting and measuring driven distance;");
		while (!suppress
				&& ((eopdRight.getDistance() >= minDistanceSideEOPDRight && eopdRight
						.getDistance() < maxDistanceSideEOPDRight) || (eopdLeft
						.getDistance() >= minDistanceSideEOPDLeft && eopdLeft
						.getDistance() < maxDistanceSideEOPDLeft))) {
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
		logger.info("suppress: Running");
		suppress = true;

	}

}
