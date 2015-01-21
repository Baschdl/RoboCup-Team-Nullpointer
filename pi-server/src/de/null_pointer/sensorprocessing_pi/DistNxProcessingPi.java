package de.null_pointer.sensorprocessing_pi;

public class DistNxProcessingPi {

	public DistNxProcessingPi() {
	}

	private int distance = -1;

	private final Object lockDistance = new Object();

	public void setDistance(int distance) {
		synchronized (lockDistance) {
			this.distance = distance;
		}
	}

	public int getDistance() {
		synchronized (lockDistance) {
			return distance;
		}
	}
	
	public void setTestDistance(int distance){
		this.distance = distance;
	}
}
