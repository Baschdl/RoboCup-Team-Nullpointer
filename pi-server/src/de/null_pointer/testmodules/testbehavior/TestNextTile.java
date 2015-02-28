package de.null_pointer.testmodules.testbehavior;

import de.null_pointer.behavior.NextTile;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;

public class TestNextTile extends NextTile {

	private boolean active = false;

	public TestNextTile(Abs_ImuProcessingPi absImu, Navigation nav,
			Odometer odometer) {
		super(absImu, nav, odometer);
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
