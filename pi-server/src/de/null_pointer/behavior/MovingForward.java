package de.null_pointer.behavior;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Odometer;
import lejos.robotics.subsumption.Behavior;

public class MovingForward implements Behavior {
	private static Logger logger = Logger.getLogger(MovingForward.class);
	private Properties propPiServer = null;

	private MotorControlPi motorControl = null;
	private Odometer odometer = null;

	private int speed = -1;

	private boolean moving = false;
	private long time;

	public MovingForward(MotorControlPi motorControl, Odometer odometer,
			Properties propPiServer) {
		this.motorControl = motorControl;
		this.odometer = odometer;
		this.propPiServer = propPiServer;

		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.MovingForward.speed"));
	}

	@Override
	public boolean takeControl() {
		logger.debug("takeControl: Calling action: YES;");
		return true;
	}

	@Override
	public void action() {
		logger.info("action: Running;");
		time = 0;
		moving = true;
		motorControl.forward(speed);
		while (moving) {
			odometer.calculateDistance(time, speed);
			time = System.currentTimeMillis();
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				logger.fatal("InterruptedException while sleep()");
			}
			time = System.currentTimeMillis() - time;
		}
	}

	@Override
	public void suppress() {
		logger.debug("suppress: running");
		moving = false;
	}

	public boolean testGetMoving() {
		return moving;
	}

	public void testActionWithoutLoop() {
		logger.info("action: Running;");
		time = 0;
		moving = true;
		motorControl.forward(speed);
		odometer.calculateDistance(time, speed);
		time = System.currentTimeMillis();
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			logger.fatal("InterruptedException while sleep()");
		}
		time = System.currentTimeMillis() - time;
	}

	public void testSetMoving(boolean mov) {
		moving = mov;
	}
}
