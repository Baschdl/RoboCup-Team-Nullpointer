package de.null_pointer.sensorprocessing_pi;

public class EOPDProcessingPi {
	double distance = -1;
	
	public double getDistance(){
		return distance;
	}
	
	public void setEOPDdistance(int value){
		//Wert zur Streckeneinteilung ggf. anpassen
		distance = 30000 / Math.sqrt(value);
	}
}