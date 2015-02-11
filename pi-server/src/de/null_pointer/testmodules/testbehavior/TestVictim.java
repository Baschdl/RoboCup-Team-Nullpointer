package de.null_pointer.testmodules.testbehavior;

import java.util.Properties;

import de.null_pointer.behavior.Victim;
import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.sensorprocessing_pi.ThermalSensorProcessingPi;

public class TestVictim extends Victim {

	private boolean active = false;

	public TestVictim(BrickControlPi brickControlLED,
			MotorControlPi motorControl, ThermalSensorProcessingPi thermal,
			Properties propPiServer) {
		super(brickControlLED, motorControl, thermal, propPiServer);
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
