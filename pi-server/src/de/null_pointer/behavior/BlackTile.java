package de.null_pointer.behavior;

import java.util.Properties;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class BlackTile implements Behavior {
	private static Logger logger = Logger.getLogger(BlackTile.class);

	private Properties propPiServer = null;

	private MotorControlPi motorControl = null;
	private LSAProcessingPi lsa = null;
	private Abs_ImuProcessingPi absImu = null;
	private Navigation nav = null;
	private Odometer odometer = null;
	private boolean moving = false;
	private long time = -1;
	private int speed = -1;

	public BlackTile(MotorControlPi motorControl, LSAProcessingPi lsa,
			Abs_ImuProcessingPi absImu, Navigation nav, Odometer odometer,
			Properties propPiServer) {
		this.motorControl = motorControl;
		this.lsa = lsa;
		this.absImu = absImu;
		this.nav = nav;
		this.odometer = odometer;
		this.propPiServer = propPiServer;

		speed = Integer.parseInt(propPiServer
				.getProperty("Behavior.BlackTile.speed"));
	}

	@Override
	public boolean takeControl() {
		int[] values = lsa.getLSA();
		int value = 0;
		for (int val : values) {
			value += val;
		}
		// Alle 8 Sensoren im Durchschnitt ueber 70
		return value > 70 * 8;
	}

	@Override
	public void action() {
		time = 0;
		moving = true;
		logger.info("Schwarze Kachel erkannt");
		motorControl.stop();
		nav.setBlackTile();

		motorControl.backward(speed);
		while (moving && odometer.getDistanceCounter() > 0) {
			odometer.calculateDistance(time, -speed);
			time = System.currentTimeMillis();
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				logger.fatal("InterruptedException while sleep()");
			}
			time = System.currentTimeMillis() - time;
		}
		motorControl.stop();
		int directionToMove = nav.tremauxAlgorithm(absImu.getHeading(), true);

		// TODO: in "directionToMove"-Richtung drehen

	}

	@Override
	public void suppress() {
		moving = false;

	}

}
