package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class BlackTile implements Behavior {
	private static Logger logger = Logger.getLogger(BlackTile.class);
	
	MotorControlPi motorControl;
	LSAProcessingPi lsa;
	Navigation nav;

	public BlackTile(MotorControlPi motorControl, LSAProcessingPi lsa, Navigation nav) {
		this.motorControl = motorControl;
		this.lsa = lsa;
		this.nav = nav;
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		logger.info("Schwarze Kachel erkannt");
		// TODO Auto-generated method stub

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
