package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class NextTile implements Behavior {
	private static Logger logger = Logger.getLogger(NextTile.class);

	private Abs_ImuProcessingPi absImu = null;
	private Navigation nav = null;
	private Odometer odometer = null;

	public NextTile(Abs_ImuProcessingPi absImu, Navigation nav,
			Odometer odometer) {
		this.absImu = absImu;
		this.nav = nav;
		this.odometer = odometer;
	}

	@Override
	public boolean takeControl() {
		// TODO ggf. Umschaltwert anpassen
		if (odometer.getDistanceCounter() >= 30) {
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		logger.info("Naechste Kachel erreicht");
		nav.switchTile(absImu.getAbsImuHeading());
		odometer.resetDistanceCounter();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
