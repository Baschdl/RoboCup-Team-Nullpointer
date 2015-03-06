package de.null_pointer.behavior;

import java.util.Properties;

import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Logger;

import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;

public class WallTooClose implements Behavior {
	private static Logger logger = Logger.getLogger(WallTooClose.class);
	private Properties propPiServer = null;
	private MotorControlPi motorControl = null;
	private EOPDProcessingPi eopdLeft = null;
	private EOPDProcessingPi eopdRight = null;
	private Odometer odometer = null;
	private Abs_ImuProcessingPi absImu = null;
	private DistNxProcessingPi distnx;
	private double minDistanceSideEOPDRight;
	private double minDistanceSideEOPDLeft;
	private double maxDistanceSideEOPDRight;
	private double maxDistanceSideEOPDLeft;
	private int minimalDistanceFront = -1;
	private long time;
	private int speed;
	private int slopeSpeed;
	private int percentOfSpeed;
	private int correctionSpeed;
	private int angleToTakeControl;
	private boolean correctingToTheRight = false;
	private boolean correctingToTheLeft = false;
	private boolean suppress = false;
	private Navigation nav;
	private int directionWhereCorrectionNeeded = 0;
	private boolean alreadyCorrecting = false;

	public WallTooClose(EOPDProcessingPi eopdRight, DistNxProcessingPi distnx,
			EOPDProcessingPi eopdLeft, MotorControlPi motorControl,
			Odometer odometer, Properties propPiServer, Navigation nav,
			Abs_ImuProcessingPi absImu) {
		this.propPiServer = propPiServer;
		this.motorControl = motorControl;
		this.eopdLeft = eopdLeft;
		this.eopdRight = eopdRight;
		this.odometer = odometer;
		this.absImu = absImu;
		this.distnx = distnx;
		this.nav = nav;

		minDistanceSideEOPDRight = Double
				.parseDouble(propPiServer
						.getProperty("Behavior.WallTooClose.minimalDistanceSideEOPDRight"));
		minDistanceSideEOPDLeft = Double
				.parseDouble(propPiServer
						.getProperty("Behavior.WallTooClose.minimalDistanceSideEOPDLeft"));
		maxDistanceSideEOPDRight = Integer
				.parseInt(propPiServer
						.getProperty("Behavior.Intersection.maximalDistanceSideEOPDRight"));
		maxDistanceSideEOPDLeft = Integer
				.parseInt(propPiServer
						.getProperty("Behavior.Intersection.maximalDistanceSideEOPDLeft"));
		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.MovingForward.speed"));
		percentOfSpeed = Integer
				.parseInt(propPiServer
						.getProperty("Behavior.WallTooClose.percentOfOldSpeedForCorrection"));
		slopeSpeed = Integer.parseInt(propPiServer
				.getProperty("Behavior.Slope.speed"));
		angleToTakeControl = Integer.parseInt(propPiServer
				.getProperty("Behavior.Slope.angleToTakeControl"));
		minimalDistanceFront = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.minimalDistanceFront"));
		correctionSpeed = Integer.parseInt(propPiServer.getProperty("Behavior.WallTooClose.correctionSpeed"));

	}

	@Override
	public boolean takeControl() {
		logger.debug("takeControl: Running;");
		if ((eopdRight.getDistance() >= minDistanceSideEOPDRight && eopdRight
				.getDistance() > 0)
				|| (eopdLeft.getDistance() >= minDistanceSideEOPDLeft && eopdLeft.getDistance() > 0)
				&& (!alreadyCorrecting) && (distnx.getDistance() > minimalDistanceFront)) {
			logger.info("takeControl: Calling action: YES;");
			return true;
		}
		logger.debug("takeControl: Calling action: NO;");
		return false;
	}

	@Override
	public void action() {
		logger.info("action: Running;");
		alreadyCorrecting = true;
		time = 0;
		logger.debug("action: Correcting driving direction;");
		if (eopdRight.getDistance() <= minDistanceSideEOPDRight
				&& eopdRight.getDistance() > 0
				&& !correctingToTheLeft) {
			correctingToTheLeft = true;
			if (absImu.getTiltDataVertical() > angleToTakeControl) {
				logger.debug("action: Correcting driving direction; On slope; Left wall too close;");
				motorControl.changeSpeedSingleMotorForward(2, 'A', slopeSpeed
						+ slopeSpeed * percentOfSpeed / 100);
				directionWhereCorrectionNeeded = 1;
			} else {
				motorControl.stop();
				logger.debug("action: Correcting driving direction; Left wall too close;");
				directionWhereCorrectionNeeded = 2;
			}
		} else if (eopdLeft.getDistance() <= minDistanceSideEOPDLeft
				&& eopdLeft.getDistance() > 0
				&& !correctingToTheRight) {
			correctingToTheRight = true;
			if (absImu.getTiltDataVertical() > angleToTakeControl) {
				logger.debug("action: Correcting driving direction; On slope; Right wall too close;");
				motorControl.changeSpeedSingleMotorForward(2, 'B', slopeSpeed
						+ slopeSpeed * percentOfSpeed / 100);
				directionWhereCorrectionNeeded = 3;
			} else {
				motorControl.stop();
				logger.debug("action: Correcting driving direction; Left wall too close;");
				directionWhereCorrectionNeeded = 4;
			}
		}
		logger.debug("action: Correcting and measuring driven distance;");
		while (!suppress
				&& ((eopdRight.getDistance() <= minDistanceSideEOPDRight && eopdRight
						.getDistance() > 0) || (eopdLeft.getDistance() <= minDistanceSideEOPDRight && eopdLeft
						.getDistance() > 0)) && (distnx.getDistance() > minimalDistanceFront)) {
			if (directionWhereCorrectionNeeded == 2) {
				motorControl.right(correctionSpeed);
			} else if (directionWhereCorrectionNeeded == 4) {
				motorControl.left(correctionSpeed);
			} else if (directionWhereCorrectionNeeded == 1
					|| directionWhereCorrectionNeeded == 3) {
				odometer.calculateDistance(time, speed);
				time = System.currentTimeMillis();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					logger.fatal("InterruptedException while sleep()");
				}
				time = System.currentTimeMillis() - time;
				if ((odometer.getDistanceCounter() % 30) > 29) {
					odometer.addValueToDistanceCounter(30 - (odometer
							.getDistanceCounter() % 30));
					nav.switchTile(motorControl.getRotationHeading());
				}
			}
			
		}
		if(directionWhereCorrectionNeeded == 2){
			motorControl.stop();
			motorControl.rotateright(5);
		}else if(directionWhereCorrectionNeeded == 4){
			motorControl.stop();
			motorControl.rotateleft(5);
		}
		correctingToTheLeft = false;
		correctingToTheRight = false;
		directionWhereCorrectionNeeded = 0;
		alreadyCorrecting = false;
		logger.debug("action: Finished correction;");
		suppress = false;
	}

	@Override
	public void suppress() {
		logger.info("suppress: Running");
		suppress = true;

	}

}
