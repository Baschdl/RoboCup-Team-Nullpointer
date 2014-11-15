package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class Intersection implements Behavior {
	private static Logger logger = Logger.getLogger(Intersection.class);

	MotorControlPi motorControl;
	DistNxProcessingPi distnx;
	EOPDProcessingPi eopdLeft;
	EOPDProcessingPi eopdRight;
	Navigation nav;

	int minimalDistanceFront = 10;
	int maximalDistanceSide = 20;

	public Intersection(MotorControlPi motorControl, DistNxProcessingPi distnx,
			EOPDProcessingPi eopdLeft, EOPDProcessingPi eopdRight,
			Navigation nav) {
		this.motorControl = motorControl;
		this.distnx = distnx;
		this.eopdLeft = eopdLeft;
		this.eopdRight = eopdRight;
		this.nav = nav;
	}

	@Override
	public boolean takeControl() {
		if (distnx.getDistance() <= minimalDistanceFront) {
			logger.info("Behavior Intersection: Wall ahead");
			return true;
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
		logger.info("Behavior Intersection: Intersection detected");

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

		// TODO: von dieser bis zur letzten KReuzung seitliche Verbindungen
		// kappen

		// TODO: aktuelle Ausrichtung von Gyro erfragen
		int directionToMove = nav.tremauxAlgorithm(-1);

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
		// TODO: ersetze "-1" durch passende Nummer fuer "vorne"/ Gyro
		nav.removeNeighbor(-1);

		findHallway();

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
