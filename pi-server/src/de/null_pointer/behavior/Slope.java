package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class Slope implements Behavior {
	private static Logger logger = Logger.getLogger(Slope.class);

	MotorControlPi motorControl;
	Abs_ImuProcessingPi absImu;
	Navigation nav;

	public Slope(MotorControlPi motorControl, Abs_ImuProcessingPi absImu,
			Navigation nav) {
		this.motorControl = motorControl;
		this.absImu = absImu;
		this.nav = nav;
	}

	@Override
	public boolean takeControl() {
		// TODO: Benoetigt Neigung vom AbsImu
		return false;
	}

	@Override
	public void action() {
		logger.info("Steigung erkannt");
		// TODO Auto-generated method stub

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
