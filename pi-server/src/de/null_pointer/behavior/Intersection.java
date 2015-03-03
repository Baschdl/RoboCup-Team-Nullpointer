package de.null_pointer.behavior;

import java.util.Properties;

import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Logger;

import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Node;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;

public class Intersection implements Behavior {
	private static Logger logger = Logger.getLogger(Intersection.class);

	private MotorControlPi motorControl;
	private DistNxProcessingPi distnx;
	private EOPDProcessingPi eopdLeft;
	private EOPDProcessingPi eopdRight;
	private Abs_ImuProcessingPi absImu;
	private Odometer odometer;
	private Navigation nav;

	private Node lastIntersection = null;

	private int minimalDistanceFront = -1;
	private int maximalDistanceSide = -1;
	private int speed = -1;
	private long time = 0;
	// TODO: passender benennen
	private int actualDistance = -1;

	public Intersection(MotorControlPi motorControl, DistNxProcessingPi distnx,
			EOPDProcessingPi eopdLeft, EOPDProcessingPi eopdRight,
			Abs_ImuProcessingPi absImu, Odometer odometer, Navigation nav,
			Properties propPiServer) {
		this.motorControl = motorControl;
		this.distnx = distnx;
		this.eopdLeft = eopdLeft;
		this.eopdRight = eopdRight;
		this.absImu = absImu;
		this.odometer = odometer;
		this.nav = nav;

		minimalDistanceFront = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.minimalDistanceFront"));
		maximalDistanceSide = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.maximalDistanceSide"));
		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.MovingForward.speed"));
	}

	@Override
	public boolean takeControl() {
		int actualDistance;
		if ((actualDistance = distnx.getDistance()) <= minimalDistanceFront
				&& actualDistance >= 0) {
			logger.info("takeControl: Wall ahead; Calling Action: YES;");
			return true;
		} else if (actualDistance < 0) {
			logger.error("takeControl: No DistNx-Values (negative value), Calling action: NO;");
		} else if (eopdLeft.getDistance() >= maximalDistanceSide) {
			if (nav.getCurrentTile() == lastIntersection/*odometer.getDistanceCounter() < 15*/) {
				logger.debug("takeControl: Hallway left detected, but distanceCounter is < 15; Calling action: NO;");
				return false;
			}
			logger.info("takeControl: Hallway left detected; Calling action: YES;");
			return true;
		} else if (eopdRight.getDistance() >= maximalDistanceSide) {
			if (nav.getCurrentTile() == lastIntersection/*odometer.getDistanceCounter() < 15*/) {
				logger.debug("takeControl: Hallway right detected, but distanceCounter is < 15; Calling action: NO;");
				return false;
			}
			logger.info("takeControl: Hallway right detected; Calling action: YES;");
			return true;
		}
		logger.debug("takeControl: No hallway detected; Calling action: NO;");
		return false;
	}

	@Override
	public void action() {
		logger.info("action: Running");
		time = 0;
		if ((actualDistance = distnx.getDistance()) > minimalDistanceFront
				&& actualDistance >= 0) {
			logger.debug("action: Wall is not ahead, moving to the centre of the tile;");
			motorControl.forward(speed);
			while (odometer.getDistanceCounter() < 30) {
				odometer.calculateDistance(time, speed);
				time = System.currentTimeMillis();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					logger.fatal("action: InterruptedException while sleep();");
				}
				time = System.currentTimeMillis() - time;
				if ((actualDistance = distnx.getDistance()) <= minimalDistanceFront
						&& actualDistance >= 0) {
					logger.debug("action: Wall is now ahead; Stopping movement;");
					break;
				}
			}
			//TODO: evtl. ausserhalb der if-Abfrage; (Konflikt mit NextTile)
			nav.switchTile(motorControl.getRotationHeading());
		}
		
		odometer.resetDistanceCounter();
		motorControl.stop();
		findHallway();

		if (lastIntersection != null) {
			nav.cutNodeConnections(lastIntersection);
		}
		lastIntersection = nav.getCurrentTile();

		// int directionToMove = nav.tremauxAlgorithm(absImu.getAbsImuHeading(),
		// false);

		int directionToMove = nav.tremauxAlgorithm(
				motorControl.getRotationHeading(), false);

		// motorControl.decideTurn(absImu.getAbsImuHeading(), directionToMove);

		motorControl.decideTurn(motorControl.getRotationHeading(),
				directionToMove);
		logger.debug("action: finished;");
	}

	private void findHallway() {
		double distanceSide = -1;
		if ((actualDistance = distnx.getDistance()) <= minimalDistanceFront
				&& actualDistance >= 0) {
			logger.debug("findHallway: Wall is ahead; Saving wall in front");
			// nav.removeNeighbor(absImu.getAbsImuHeading());
			nav.removeNeighbor(motorControl.getRotationHeading());
		}
		if ((distanceSide = eopdLeft.getDistance()) <= maximalDistanceSide
				& distanceSide > 0) {
			logger.debug("findHallway: Wall is left; Saving wall on the left");
			// nav.removeNeighbor(nav.rightleftDirection(
			// absImu.getAbsImuHeading(), false));
			nav.removeNeighbor(nav.rightleftDirection(
					motorControl.getRotationHeading(), false));
		}
		if ((distanceSide = eopdRight.getDistance()) <= maximalDistanceSide
				& distanceSide > 0) {
			logger.debug("findHallway: Wall is right; Saving wall on the right");
			// nav.removeNeighbor(nav.rightleftDirection(
			// absImu.getAbsImuHeading(), true));
			nav.removeNeighbor(nav.rightleftDirection(
					motorControl.getRotationHeading(), true));
		}
	}

	@Override
	public void suppress() {
	}

}
