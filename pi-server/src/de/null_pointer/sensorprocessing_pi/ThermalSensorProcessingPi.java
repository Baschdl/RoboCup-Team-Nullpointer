package de.null_pointer.sensorprocessing_pi;

public class ThermalSensorProcessingPi {

	private int temperature = -1;

	private final Object lockDistance = new Object();

	public void setTemperature(int temperature) {
		synchronized (lockDistance) {
			this.temperature = temperature;
		}
	}

	public int getTemperature() {
		synchronized (lockDistance) {
			return temperature;
		}
	}

	public void setTestTemperature(int temperature) {
		this.temperature = temperature;
	}

}
