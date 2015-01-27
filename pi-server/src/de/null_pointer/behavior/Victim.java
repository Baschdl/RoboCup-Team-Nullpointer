package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import lejos.robotics.subsumption.Behavior;

public class Victim implements Behavior {
	private static Logger logger = Logger.getLogger(Victim.class);
	
	private MotorControlPi motorControl;

	public Victim(MotorControlPi motorControl) {
		this.motorControl = motorControl;
	}

	@Override
	public boolean takeControl() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			logger.fatal("InterruptedException while sleep()"); 
		}
		// TODO: Benoetigt Waermesensor
		return false;
	}

	@Override
	public void action() {
		logger.info("Victim detected");
		// TODO Auto-generated method stub

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
