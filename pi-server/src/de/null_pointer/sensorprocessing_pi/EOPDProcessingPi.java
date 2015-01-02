package de.null_pointer.sensorprocessing_pi;

public class EOPDProcessingPi {

	double distance = -1;

	private final Object lockDistance = new Object();

	public double getDistance() {
		synchronized (lockDistance) {
			return distance;
		}
	}

	public void setEOPDdistance(int value) {
		synchronized (lockDistance) {
			// Wert zur Streckeneinteilung ggf. anpassen
			distance = 150 / Math.sqrt(value);
		}
	}
}