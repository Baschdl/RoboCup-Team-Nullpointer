package de.null_pointer.testmodules.testbehavior;

import java.util.Properties;

import de.null_pointer.behavior.MovingForward;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Odometer;

public class TestMovingForward extends MovingForward {

	private boolean active = false;

	public TestMovingForward(MotorControlPi motorControl, Odometer odometer,
			Properties propPiServer) {
		super(motorControl, odometer, propPiServer);
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
