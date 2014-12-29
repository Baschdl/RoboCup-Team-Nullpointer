package de.null_pointer.testmodules.testbehavior;

import de.null_pointer.behavior.MovingForward;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Odometer;

public class TestMovingForward extends MovingForward {

	private boolean active = false;

	public TestMovingForward(MotorControlPi motorControl, Odometer odometer, int speed) {
		super(motorControl, odometer, speed);
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public void action() {
		active = true;
		super.action();
	}

	@Override
	public void suppress() {
		active = false;
		super.suppress();
	}

}
