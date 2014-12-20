package de.null_pointer.testmodules.testbehavior;

import de.null_pointer.behavior.Victim;
import de.null_pointer.motorcontrol_pi.MotorControlPi;

public class TestVictim extends Victim {

	private boolean active = false;

	public TestVictim(MotorControlPi motorControl) {
		super(motorControl);
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
