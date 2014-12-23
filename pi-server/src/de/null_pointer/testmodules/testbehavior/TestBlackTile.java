package de.null_pointer.testmodules.testbehavior;

import de.null_pointer.behavior.BlackTile;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;

public class TestBlackTile extends BlackTile {

	private boolean active = false;

	public TestBlackTile(MotorControlPi motorControl, LSAProcessingPi lsa,
			Abs_ImuProcessingPi absImu, Navigation nav) {
		super(motorControl, lsa, absImu, nav);
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
