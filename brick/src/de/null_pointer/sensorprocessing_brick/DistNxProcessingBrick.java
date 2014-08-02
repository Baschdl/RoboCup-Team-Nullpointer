package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;

public class DistNxProcessingBrick {
	
	OpticalDistanceSensor distNx = null;
	private BrickControlBrick brickControl;
	
	private int mid_value = 0;
	private int old_value = 0;
	private int counter = 1;
	
	public DistNxProcessingBrick(BrickControlBrick brickControl, SensorPort port){
		distNx = new OpticalDistanceSensor(port);
		this.brickControl = brickControl;
	}
	
	public void processDistance(){
		mid_value = (counter * mid_value + distNx.getDistance()) / counter + 1;
		counter++;
		if(counter == 5 && mid_value != old_value){
			old_value = mid_value;
			counter = 1;
			brickControl.sendData(1, 1, mid_value);
		}
	}
}
