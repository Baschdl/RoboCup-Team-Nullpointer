package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class Slope implements Behavior {
	private static Logger logger = Logger.getLogger(Slope.class);

	private MotorControlPi motorControl = null;
	private Abs_ImuProcessingPi absImu = null;
	private Navigation nav = null;

	private int angleToTakeControl = -1;
	private int speed = -1;

	public Slope(MotorControlPi motorControl, Abs_ImuProcessingPi absImu,
			Navigation nav, int angleToTakeControl, int speed) {
		this.motorControl = motorControl;
		this.absImu = absImu;
		this.nav = nav;

		this.angleToTakeControl = angleToTakeControl;
	}

	@Override
	public boolean takeControl() {
		return (absImu.getTiltDataVertical() > angleToTakeControl);
	}

	@Override
	public void action() {
		logger.info("Steigung erkannt");
		// TODO
		nav.slope(absImu.getHeading(), 15, 15);
		motorControl.forward(speed);
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
