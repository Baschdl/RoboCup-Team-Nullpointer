package de.null_pointer.behavior;

import java.util.Properties;

import lejos.robotics.subsumption.Behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.motorcontrol_pi.MotorControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.ThermalSensorProcessingPi;

public class Victim implements Behavior {
	private static Logger logger = Logger.getLogger(Victim.class);

	private BrickControlPi brickControlLED = null;
	private MotorControlPi motorControl = null;
	private Navigation nav = null;
	private ThermalSensorProcessingPi thermal = null;
	private Properties propPiServer = null;

	private int minimumTemperature = -1;
	private int maximumTemperature = -1;

	private int victimsFound = 0;

	public Victim(BrickControlPi brickControlLED, MotorControlPi motorControl,
			Navigation nav, ThermalSensorProcessingPi thermal,
			Properties propPiServer) {
		this.brickControlLED = brickControlLED;
		this.motorControl = motorControl;
		this.nav = nav;
		this.thermal = thermal;
		this.propPiServer = propPiServer;

		this.minimumTemperature = Integer.parseInt(propPiServer
				.getProperty("Behavior.Victim.minimumTemperature"));
		this.maximumTemperature = Integer.parseInt(propPiServer
				.getProperty("Behavior.Victim.maximumTemperature"));
	}

	@Override
	public boolean takeControl() {
		logger.debug("takeControl: Running;");
		int temperature = -1;
		if ((temperature = thermal.getTemperature()) > minimumTemperature
				&& temperature < maximumTemperature) {
			if (nav.getVictimFound(nav.rightleftDirection(
					motorControl.getRotationHeading(), true)) == false) {
				logger.info("takeControl: Victim detected, but already marked as found; Calling action: NO;");
				return false;
			}
			logger.info("takeControl: Victim detected; Calling action: YES;");
			return true;
		} else {
			logger.info("takeControl: no victim detected; Calling action: NO;");
			return false;
		}
	}

	@Override
	public void action() {
		logger.info("action: Running;");

		// TODO: ggf. bis zur mitte der Kachel fahren (mittels Odometer)
		motorControl.stop();

		nav.setVictimFound(nav.rightleftDirection(
				motorControl.getRotationHeading(), true));
		brickControlLED.blinkColorSensorLED();

		// TODO: Rettungspaket abwerfen
		logger.debug("action: Incrementing counter of found victims;");
		victimsFound++;
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

	public int getVictimsFound() {
		return victimsFound;
	}
	
	public void setNavigation(Navigation nav){
		this.nav = nav;
	}
}
