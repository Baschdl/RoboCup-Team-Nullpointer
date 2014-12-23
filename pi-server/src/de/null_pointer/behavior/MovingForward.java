package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import lejos.robotics.subsumption.Behavior;

public class MovingForward implements Behavior {
	private static Logger logger = Logger.getLogger(MovingForward.class);

	private MotorControlPi motorControl = null;

	private int speed = -1;

	public MovingForward(MotorControlPi motorControl, int speed) {
		this.motorControl = motorControl;

		this.speed = speed;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		logger.debug("Bewege mich vorwaerts");
		// TODO: Geschwindigkeit anpassen
		motorControl.forward(speed);

	}

	@Override
	public void suppress() {

	}

}
