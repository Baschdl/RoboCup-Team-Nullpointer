package de.null_pointer.behavior;

import org.apache.log4j.Logger;

import de.null_pointer.communication_pi.BrickControlPi;
import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.navigation.map.Odometer;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class NextTile implements Behavior {
	private static Logger logger = Logger.getLogger(NextTile.class);

	Abs_ImuProcessingPi absImu = null;
	Navigation nav = null;
	Odometer odometer = null;

	public NextTile(Abs_ImuProcessingPi absImu, Navigation nav) {
		this.absImu = absImu;
		this.nav = nav;
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
		nav.switchTile(absImu.getHeading());
		odometer.resetDistanceCounter();
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
