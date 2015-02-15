package de.null_pointer.behavior;

import java.util.Properties;

import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Logger;

import de.null_pointer.motorcontrol_pi.MotorControlPi;

public class WallTooClose implements Behavior {
	private static Logger logger = Logger.getLogger(WallTooClose.class);
	private Properties  propPiServer = null;
	private MotorControlPi motorControl = null;
	
	public WallTooClose(MotorControlPi motorControl, Properties propPiServer){
		this.propPiServer = propPiServer;
		this.motorControl = motorControl;
		
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}

	
}
