package de.null_pointer.testmodules.testbehavior;

import de.null_pointer.behavior.Intersection;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.DistNxProcessingPi;
import de.null_pointer.sensorprocessing_pi.EOPDProcessingPi;

public class TestIntersection extends Intersection {

	private boolean active = false;

	public TestIntersection(MotorControlPi motorControl,
			DistNxProcessingPi distnx, EOPDProcessingPi eopdLeft,
			EOPDProcessingPi eopdRight, Abs_ImuProcessingPi absImu,
			Navigation nav) {
		super(motorControl, distnx, eopdLeft, eopdRight, absImu, nav);
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
