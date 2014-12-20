package de.null_pointer.testmodules.testbehavior;

import de.null_pointer.behavior.Slope;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;

public class TestSlope extends Slope {

	private boolean active = false;

	public TestSlope(MotorControlPi motorControl, Abs_ImuProcessingPi absImu,
			Navigation nav) {
		super(motorControl, absImu, nav);
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
