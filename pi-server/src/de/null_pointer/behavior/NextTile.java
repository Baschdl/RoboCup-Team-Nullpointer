package de.null_pointer.behavior;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Odometer;
//import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class NextTile implements Behavior {
	private static Logger logger = Logger.getLogger(NextTile.class);

	// private Abs_ImuProcessingPi absImu = null;
	private MotorControlPi motorControl = null;
	private Navigation nav = null;
	private Odometer odometer = null;

	private int nextTileReachDistance = -1;

	public NextTile(
			/* Abs_ImuProcessingPi absImu, */MotorControlPi motorControl,
			Navigation nav, Odometer odometer, Properties propPiServer) {
		// this.absImu = absImu;
		this.motorControl = motorControl;
		this.nav = nav;
		this.odometer = odometer;
		nextTileReachDistance = Integer.parseInt(propPiServer
				.getProperty("Behavior.NextTile.nextTileReachDistance"));
	}

	@Override
	public boolean takeControl() {
		// TODO ggf. Umschaltwert anpassen
		if (odometer.getDistanceCounter() >= nextTileReachDistance) {
			logger.info("takeControl: Calling action: YES;");
			return true;
		}
		logger.debug("takeControl: Calling action: NO;");
		return false;
	}

	@Override
	public void action() {
		logger.info("action: Running; Next tile reached;");
		// nav.switchTile(absImu.getAbsImuHeading());
		nav.switchTile(motorControl.getRotationHeading());
		odometer.resetDistanceCounter();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
