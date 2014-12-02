package de.null_pointer.sensorprocessing_pi;



public class EOPDProcessingPi {
	double distance = -1;
	
	public double getEOPDdistance(){
		return distance;
	}
	
	public void setEOPDdistance(int value){
		distance = 30000 / Math.sqrt(value);
	}
}