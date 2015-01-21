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
			if (value < 520) {
				distance = 0.00625 * value + 0.1875;
			} else if (value < 890) {
				distance = 8.865467626 + (1.007194245E-14) * Math
						.sqrt((8.792541276E+29) - (9.928571429E+26) * value);
			} else if (value < 970) {
				distance = 0.07918552036 * value - 62.72322775;
			} else {
				distance = 29.27093359 - (5.497593840E-14) * Math
						.sqrt((1.840381553E+30) - (1.818977591E+27) * value);
			}
		}
	}
	
	public void setTestEOPDdistance(int value){
		
	}
}