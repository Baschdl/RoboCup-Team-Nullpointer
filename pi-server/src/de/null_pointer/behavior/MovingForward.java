package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import lejos.robotics.subsumption.Behavior;

public class MovingForward implements Behavior {
	private static Logger logger = Logger.getLogger(MovingForward.class);
	
	MotorControlPi motorControl;
	
	public MovingForward(MotorControlPi motorControl) {
		this.motorControl = motorControl;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		logger.info("Bewege mich vorwaerts");
		//TODO: Geschwindigkeit anpassen
		motorControl.forward(200);
		
	}

	@Override
	public void suppress() {
		
	}

}
