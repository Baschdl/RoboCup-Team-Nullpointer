package de.null_pointer.behavior;

import java.util.Properties;

import de.null_pointer.navigation.map.Navigation;
import de.null_pointer.sensorprocessing_pi.Abs_ImuProcessingPi;
import de.null_pointer.sensorprocessing_pi.LSAProcessingPi;
import lejos.robotics.subsumption.Behavior;

public class SilverTile implements Behavior {

	private LSAProcessingPi lsa = null;
	private Abs_ImuProcessingPi absImu = null;
	private Navigation nav = null;

	private int lsaActionValue = -1;

	public SilverTile(LSAProcessingPi lsa, Navigation nav,
			Properties propPiServer) {
		this.lsa = lsa;
		this.nav = nav;
		lsaActionValue = Integer.parseInt(propPiServer
				.getProperty("Behavior.SilverTile.lsaActionValue"));
	}

	@Override
	public boolean takeControl() {
		int[] values = lsa.getLSA();
		int value = 0;
		for (int val : values) {
			value += val;
		}
		// Alle 8 Sensoren im Durchschnitt ueber dem Schwellenwert
		// lsaActionValue
		return value > lsaActionValue * 8;
	}

	@Override
	public void action() {
		nav.saveMap();
	}

	@Override
	public void suppress() {
	}

}
