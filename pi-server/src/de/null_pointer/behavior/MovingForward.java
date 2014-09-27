package de.null_pointer.behavior;

import de.null_pointer.motorcontrol_pi.MotorControlPi;
import lejos.robotics.subsumption.Behavior;

public class MovingForward implements Behavior {
	
	MotorControlPi motorControl;
	
	MovingForward(MotorControlPi motorControl) {
		this.motorControl = motorControl;
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		//TODO: Geschwindigkeit anpassen
		motorControl.forward(200);
		
	}

	@Override
	public void suppress() {
		
	}

}
