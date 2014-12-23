package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class Intersection implements Behavior {
	private static Logger logger = Logger.getLogger(Intersection.class);

	MotorControlPi motorControl;
	DistNxProcessingPi distnx;
	EOPDProcessingPi eopdLeft;
	EOPDProcessingPi eopdRight;
	Abs_ImuProcessingPi absImu;
	Navigation nav;

	int minimalDistanceFront = 10;
	int maximalDistanceSide = 20;

	public Intersection(MotorControlPi motorControl, DistNxProcessingPi distnx,
			EOPDProcessingPi eopdLeft, EOPDProcessingPi eopdRight,
			Abs_ImuProcessingPi absImu, Navigation nav) {
		this.motorControl = motorControl;
		this.distnx = distnx;
		this.eopdLeft = eopdLeft;
		this.eopdRight = eopdRight;
		this.absImu = absImu;
		this.nav = nav;
	}

	@Override
	public boolean takeControl() {
		int actualDistance;
		if ((actualDistance = distnx.getDistance()) <= minimalDistanceFront) {
			logger.info("Wall ahead");
			return true;
		} else if (actualDistance < 0) {
			logger.error("No DistNx-Values (negative value)");
		}
		// TODO: passende EOPD-Methoden werden benoetigt
		// else if (eopdLeft.getDistance() >= maximalDistanceSide) {
		// logger.info("Behavior Intersection: Hallway left");
		// return true;
		// } else if (eopdLeft.getDistance() >= maximalDistanceSide) {
		// logger.info("Behavior Intersection: Hallway right");
		// return true;
		// }
		return false;
	}

	@Override
	public void action() {
		logger.info("Intersection detected");

		if (distnx.getDistance() <= minimalDistanceFront) {
			motorControl.stop();
			wallAhead();
		}

		// TODO: benoetigt Implementation des Streckenzaehlers
		// Wenn Streckenzaehler >= 30 ist
		else if (false) {
			motorControl.stop();
			findHallway();
		}

		// TODO: von dieser bis zur letzten Kreuzung seitliche Verbindungen
		// kappen

		int directionToMove = nav.tremauxAlgorithm(absImu.getHeading(), false);

		// TODO: in die gegebene Richtung fahren
	}

	private void findHallway() {
		// if (eopdLeft.getDistance() <= maximalDistanceSide) {
		// // TODO: linker Nachbar entfernen
		// nav.removeNeighbor(-1);
		// }
		// if (eopdRight.getDistance() <= maximalDistanceSide) {
		// // TODO: rechter Nachbar entfernen
		// nav.removeNeighbor(-1);
		// }

	}

	private void wallAhead() {
		nav.removeNeighbor(absImu.getHeading());
		findHallway();

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
