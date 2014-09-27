package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import lejos.robotics.subsumption.Behavior;

public class NextTile implements Behavior {
	private static Logger logger = Logger.getLogger(NextTile.class);

	@Override
	public boolean takeControl() {
		// TODO: Benoetigt "Streckenzaehler"
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
