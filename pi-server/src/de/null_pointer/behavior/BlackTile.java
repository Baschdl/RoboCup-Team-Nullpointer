package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class BlackTile implements Behavior {
	private static Logger logger = Logger.getLogger(BlackTile.class);

	MotorControlPi motorControl;
	LSAProcessingPi lsa;
	Abs_ImuProcessingPi absImu;
	Navigation nav;

	public BlackTile(MotorControlPi motorControl, LSAProcessingPi lsa,
			Abs_ImuProcessingPi absImu, Navigation nav) {
		this.motorControl = motorControl;
		this.lsa = lsa;
		this.absImu = absImu;
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
		motorControl.stop();
		nav.setBlackTile();
		// TODO: Zurueck fahren
		int directionToMove = nav.tremauxAlgorithm(absImu.getHeading(), true);

		// TODO: in "directionToMove"-Richtung fahren

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
