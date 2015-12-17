package de.null_pointer.sensorprocessing_pi;

public class AccumulatorProcessingPi {

	private int milliVolt = -1;

	private final Object lockMilliVolt = new Object();

	public AccumulatorProcessingPi() {

	}

	public void setMilliVolt(int milliVolt) {
		synchronized (lockMilliVolt) {
			this.milliVolt = milliVolt;
		}
	}

	public int getMilliVolt() {
		synchronized (lockMilliVolt) {
			return milliVolt;
		}
	}
	
	public void setTestMilliVolt(int milliVolt){
		
	}
}
