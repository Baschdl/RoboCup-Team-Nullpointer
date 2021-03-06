package de.null_pointer.behavior;

import java.util.Properties;

import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Logger;

import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Node;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.pi_server.InitializeProgram;
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
	private InitializeProgram initProgram;

	private Node lastIntersection = null;

	private int minimalDistanceFront = -1;
	private int maximalDistanceSideEOPDRight = -1;
	private int maximalDistanceSideEOPDLeft = -1;
	private int speed = -1;
	private long time = 0;
	private int currentDistanceFront = -1;

	public Intersection(MotorControlPi motorControl, DistNxProcessingPi distnx,
			EOPDProcessingPi eopdLeft, EOPDProcessingPi eopdRight,
			Abs_ImuProcessingPi absImu, Odometer odometer, Navigation nav,
			Properties propPiServer, InitializeProgram initProgram) {
		this.motorControl = motorControl;
		this.distnx = distnx;
		this.eopdLeft = eopdLeft;
		this.eopdRight = eopdRight;
		this.absImu = absImu;
		this.odometer = odometer;
		this.nav = nav;
		this.initProgram = initProgram;

		minimalDistanceFront = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.minimalDistanceFront"));
		maximalDistanceSideEOPDRight = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.maximalDistanceSideEOPDRight"));
		maximalDistanceSideEOPDLeft = Integer.parseInt(propPiServer
				.getProperty("Behavior.Intersection.maximalDistanceSideEOPDLeft"));
		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.MovingForward.speed"));
	}

	@Override
	public boolean takeControl() {
		int actualDistance;
		logger.info("takeControl: right EOPD: " + eopdRight.getDistance() + "; left EOPD: " + eopdLeft.getDistance() + ";");
		if ((actualDistance = distnx.getDistance()) <= minimalDistanceFront
				&& actualDistance >= 0) {
			logger.info("takeControl: Wall ahead; Calling Action: YES;");
			return true;
		} else if (actualDistance < 0) {
			logger.error("takeControl: No DistNx-Values (negative value), Calling action: NO;");
		} else if (eopdLeft.getDistance() >= maximalDistanceSideEOPDLeft || eopdLeft.getDistance() == 0.0) {
			if ((nav.getCurrentTile() == lastIntersection) && ((odometer.getDistanceCounter() % 30) <= 20)/*
														 * odometer.
														 * getDistanceCounter()
														 * < 15
														 */) {
				logger.info("takeControl: Hallway left detected, but distanceCounter is < 15; Calling action: NO;");
				return false;
			}
			logger.info("takeControl: Hallway left detected; Calling action: YES;");
			return true;
		} else if (eopdRight.getDistance() >= maximalDistanceSideEOPDRight || eopdRight.getDistance() == 0.0) {
			if ((nav.getCurrentTile() == lastIntersection) && ((odometer.getDistanceCounter() % 30) <= 20)/*
														 * odometer.
														 * getDistanceCounter()
														 * < 15
														 */) {
				logger.info("takeControl: Hallway right detected, but distanceCounter is < 15; Calling action: NO;");
				return false;
			}
			logger.info("takeControl: Hallway right detected; Calling action: YES;");
			return true;
		}
		logger.info("takeControl: No hallway detected; Calling action: NO;");
		return false;
	}

	@Override
	public void action() {
		logger.info("action: Running");
		time = 0;
		if ((currentDistanceFront = distnx.getDistance()) > minimalDistanceFront
				&& currentDistanceFront >= 0) {
			logger.info("action: Wall is not ahead, moving to the centre of the tile;");
			motorControl.forward(speed);
			while ((odometer.getDistanceCounter() % 30) > 3) {
				odometer.calculateDistance(time, speed);
				time = System.currentTimeMillis();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					logger.fatal("action: InterruptedException while sleep();");
				}
				time = System.currentTimeMillis() - time;
				if ((currentDistanceFront = distnx.getDistance()) <= minimalDistanceFront
						&& currentDistanceFront >= 0) {
					logger.info("action: Wall is now ahead; Stopping movement;");
					break;
				}
			}
			logger.info("action: Switching tile because roboter went to the centre of the tile;");
			nav.switchTile(motorControl.getRotationHeading());
			odometer.setOldDistance(odometer.getDistanceCounter());
		} else if ((currentDistanceFront = distnx.getDistance()) <= minimalDistanceFront
				&& currentDistanceFront >= 0) {
			motorControl.stop();
			logger.info("action: Wall is ahead, already in the centre of the tile;");
			if ((odometer.getDistanceCounter() - odometer.getOldDistance()) > 27) {
				logger.info("action: Switching tile because nextTile can not be called while intersection-action is active");
				odometer.addValueToDistanceCounter(30 - (odometer
						.getDistanceCounter() % 30));
				nav.switchTile(motorControl.getRotationHeading());
				odometer.setOldDistance(odometer.getDistanceCounter());
			}
		}
		motorControl.stop();
		findHallway();

		// if (lastIntersection != null) { //auf Pi entfernt
		// nav.cutNodeConnections(lastIntersection);
		// }
		lastIntersection = nav.getCurrentTile();

		// int directionToMove = nav.tremauxAlgorithm(absImu.getAbsImuHeading(),
		// false);

		int directionToMove = nav.tremauxAlgorithm(
				motorControl.getRotationHeading(), false);

		// motorControl.decideTurn(absImu.getAbsImuHeading(), directionToMove);
		if(directionToMove >= 0){
		motorControl.decideTurn(motorControl.getRotationHeading(),
				directionToMove);
		}else if(directionToMove == -1){
			logger.error("action: error in directionToMove-value;");
		}else if(directionToMove == -2){
			initProgram.finishCompetition();
		}
		logger.info("action: finished;");
	}

	private void findHallway() {
		double distanceSide = -1;
		if ((currentDistanceFront = distnx.getDistance()) <= minimalDistanceFront
				&& currentDistanceFront >= 0) {
			logger.info("findHallway: Wall is ahead; Saving wall in front");
			// nav.removeNeighbor(absImu.getAbsImuHeading());
			nav.removeNeighbor(motorControl.getRotationHeading());
		}
		if ((distanceSide = eopdLeft.getDistance()) <= maximalDistanceSideEOPDLeft
				& distanceSide > 0) {
			logger.info("findHallway: Wall is left; Saving wall on the left");
			// nav.removeNeighbor(nav.rightleftDirection(
			// absImu.getAbsImuHeading(), false));
			nav.removeNeighbor(nav.rightleftDirection(
					motorControl.getRotationHeading(), false));
		}
		if ((distanceSide = eopdRight.getDistance()) <= maximalDistanceSideEOPDRight
				& distanceSide > 0) {
			logger.info("findHallway: Wall is right; Saving wall on the right");
			// nav.removeNeighbor(nav.rightleftDirection(
			// absImu.getAbsImuHeading(), true));
			nav.removeNeighbor(nav.rightleftDirection(
					motorControl.getRotationHeading(), true));
		}
	}

	@Override
	public void suppress() {
	}
	
	public void setNavigation(Navigation nav){
		this.nav = nav;
	}

}
