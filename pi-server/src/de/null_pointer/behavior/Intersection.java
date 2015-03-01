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
			logger.info("Wall ahead");
			return true;
		} else if (actualDistance < 0) {
			logger.error("No DistNx-Values (negative value)");
		} else if (eopdLeft.getDistance() >= maximalDistanceSide) {
			if (odometer.getDistanceCounter() < 15) {
				logger.info("Behavior Intersection: detectet, but distanceCounter is < 15");
				return false;
			}
			logger.info("Behavior Intersection: Hallway left");
			return true;
		} else if (eopdRight.getDistance() >= maximalDistanceSide) {
			if (odometer.getDistanceCounter() < 15) {
				logger.info("Behavior Intersection: detectet, but distanceCounter is < 15");
				return false;
			}
			logger.info("Behavior Intersection: Hallway right");
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		logger.info("Intersection detected");
		time = 0;
		if ((actualDistance = distnx.getDistance()) <= minimalDistanceFront
				&& actualDistance >= 0) {
			motorControl.stop();
			findHallway();
		} else {
			motorControl.forward(speed);
			while (odometer.getDistanceCounter() < 30) {
				odometer.calculateDistance(time, speed);
				time = System.currentTimeMillis();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					logger.fatal("InterruptedException while sleep()");
				}
				time = System.currentTimeMillis() - time;
				if ((actualDistance = distnx.getDistance()) <= minimalDistanceFront
						&& actualDistance >= 0) {
					break;
				}
			}
			odometer.resetDistanceCounter();
			motorControl.stop();
			findHallway();
		}

		if (lastIntersection != null) {
			nav.cutWallConnections(lastIntersection);
		}
		lastIntersection = nav.getCurrentTile();

		// int directionToMove = nav.tremauxAlgorithm(absImu.getAbsImuHeading(),
		// false);

		int directionToMove = nav.tremauxAlgorithm(
				motorControl.getRotationHeading(), false);

		// motorControl.decideTurn(absImu.getAbsImuHeading(), directionToMove);

		motorControl.decideTurn(motorControl.getRotationHeading(),
				directionToMove);
	}

	private void findHallway() {
		double distanceSide = -1;
		if ((actualDistance = distnx.getDistance()) <= minimalDistanceFront
				&& actualDistance >= 0) {
			// nav.removeNeighbor(absImu.getAbsImuHeading());
			nav.removeNeighbor(motorControl.getRotationHeading());
		}
		if ((distanceSide = eopdLeft.getDistance()) <= maximalDistanceSide
				& distanceSide > 0) {
			// nav.removeNeighbor(nav.rightleftDirection(
			// absImu.getAbsImuHeading(), false));
			nav.removeNeighbor(nav.rightleftDirection(
					motorControl.getRotationHeading(), false));
		}
		if ((distanceSide = eopdRight.getDistance()) <= maximalDistanceSide
				& distanceSide > 0) {
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
