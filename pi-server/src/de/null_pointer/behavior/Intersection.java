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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		logger.info("Kreuzung erkannt");
		// TODO Auto-generated method stub

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
