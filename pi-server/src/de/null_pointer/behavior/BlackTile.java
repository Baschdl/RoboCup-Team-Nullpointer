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

	private int speed = -1;
	private int lsaActionValue = -1;

	private boolean moving = false;
	private long time = -1;

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
		lsaActionValue = Integer.parseInt(propPiServer
				.getProperty("Behavior.BlackTile.lsaActionValue"));
	}

	@Override
	public boolean takeControl() {
		logger.debug("takeControl: running");
		int[] values = lsa.getLSA();
		int value = 0;
		for (int val : values) {
			value += val;
		}
		// All 8 sensor values in average smaller than the threshold (threshold
		// = lsaActionValue)
		if (value < lsaActionValue * 8) {
			logger.debug("takeControl: Average LSA-values: " + value);
			logger.info("takeControl: Calling action: YES;");
		} else {
			logger.debug("takeControl: Average LSA-values: " + value);
			logger.debug("takeControl: Calling action: NO;");
		}
		return value < lsaActionValue * 8;
	}

	@Override
	public void action() {
		logger.info("action: Running");
		time = 0;
		moving = true;
		motorControl.stop();
		// nav.setBlackTile();
		if ((odometer.getDistanceCounter() % 30) > 1) {
			logger.debug("action: DistanceCounter % 30 is > 1");
			motorControl.backward(speed);
			while (moving && (odometer.getDistanceCounter() % 30) > 1) {
				odometer.calculateDistance(time, -speed);
				time = System.currentTimeMillis();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					logger.fatal("action: InterruptedException while sleep();");
				}
				time = System.currentTimeMillis() - time;
			}
			motorControl.stop();
		}
		// int directionToMove = nav.tremauxAlgorithm(absImu.getAbsImuHeading(),
		// true);
		nav.disconnectTile(nav.getNeighbor(motorControl.getRotationHeading()));

		int directionToMove = nav.tremauxAlgorithm(
				motorControl.getRotationHeading(), true);

		// motorControl.decideTurn(absImu.getAbsImuHeading(), directionToMove);

		motorControl.decideTurn(motorControl.getRotationHeading(),
				directionToMove);
	}

	@Override
	public void suppress() {
		logger.debug("suppress: running");
		moving = false;
	}

}
