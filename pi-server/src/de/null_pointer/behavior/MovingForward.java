package de.null_pointer.behavior;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Odometer;
import lejos.robotics.subsumption.Behavior;

public class MovingForward implements Behavior {
	private static Logger logger = Logger.getLogger(MovingForward.class);
	private Properties  propPiServer = null;

	private MotorControlPi motorControl = null;
	private Odometer odometer = null;

	private int speed = -1;

	private boolean moving = true;
	private long time;

	public MovingForward(MotorControlPi motorControl, Odometer odometer, Properties propPiServer) {
		this.motorControl = motorControl;
		this.odometer = odometer;
		this.propPiServer = propPiServer;
		
		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.MovingForward.speed"));
	}

	@Override
	public boolean takeControl() {
		return true;
	}

	@Override
	public void action() {
		time = 0;
		moving = true;
		logger.debug("Bewege mich vorwaerts");
		// TODO: Geschwindigkeit anpassen
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
		moving = false;
	}

}
