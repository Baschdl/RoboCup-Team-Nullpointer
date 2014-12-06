package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;

public class DistNxProcessingBrick {
	
	private OpticalDistanceSensor distNx = null;
	private BrickControlBrick brickControl;
	
	private int[] values = {0,0,0,0,0};
	private int midValue = -1;
	
	/**
	 * @param brickControl
	 * 			Association to BrickControl
	 * @param port
	 * 			the port the Abs_Imu is plugged into
	 */
	public DistNxProcessingBrick(BrickControlBrick brickControl, SensorPort port){
		distNx = new OpticalDistanceSensor(port);
		this.brickControl = brickControl;
	}
	
	/**
	 * processes the sensor-readings, creates a middle value of the last 5 Readings and sends it if it has changed
	 */
	public void processData(){
		for(int i = 0; i < 4; i++){
			values[i] = values[i+1];
		}
		values[5] = distNx.getDistance();
		int buffer  = 0;
		for(int i = 0; i < 5; i++){
			buffer += values[i];
		}
		buffer /= 5;
		if(buffer != midValue){
			midValue = buffer;
			brickControl.sendData(1, 1, midValue);
		}
	}
}
