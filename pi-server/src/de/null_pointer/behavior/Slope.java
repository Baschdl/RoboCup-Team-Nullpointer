package de.null_pointer.behavior;

import java.util.Properties;

import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Logger;

import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;

public class Slope implements Behavior {
	private static Logger logger = Logger.getLogger(Slope.class);

	private Properties propPiServer = null;

	private MotorControlPi motorControl = null;
	private Abs_ImuProcessingPi absImu = null;
	private Navigation nav = null;

	private int angleToTakeControl = -1;
	private int speed = -1;
	private int mapSize = -1;
	private boolean suppress = false;

	public Slope(MotorControlPi motorControl, Abs_ImuProcessingPi absImu,
			Navigation nav, Properties propPiServer) {
		this.motorControl = motorControl;
		this.absImu = absImu;
		this.nav = nav;
		this.propPiServer = propPiServer;

		angleToTakeControl = Integer.parseInt(propPiServer
				.getProperty("Behavior.Slope.angleToTakeControl"));
		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.Slope.speed"));
		mapSize = Integer.parseInt(propPiServer
				.getProperty("Behavior.Slope.mapSize"));
	}

	@Override
	public boolean takeControl() {
		logger.debug("takeControl: Running;");
		if (absImu.getTiltDataVertical() > angleToTakeControl) {
			logger.info("takeControl: Calling action: YES;");
		} else {
			logger.debug("takeControl: Calling action: NO;");
		}
		return (absImu.getTiltDataVertical() > angleToTakeControl);
	}

	@Override
	public void action() {
		logger.info("action: Running;");
		suppress = false;
		// nav.slope(absImu.getAbsImuHeading(), mapSize, mapSize);
		nav.slope(motorControl.getRotationHeading(), mapSize, mapSize);
		motorControl.forward(speed);
		logger.debug("action: Setting speed to slopeSpeed;");
		while (suppress && absImu.getTiltDataVertical() > angleToTakeControl) {
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				logger.fatal("InterruptedException while sleep()");
			}
		}
	}

	@Override
	public void suppress() {
		logger.info("suppress: Running;");
		suppress = true;

	}

	public void setNavigation(Navigation nav) {
		this.nav = nav;
	}
}
