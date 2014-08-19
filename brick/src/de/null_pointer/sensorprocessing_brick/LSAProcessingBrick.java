package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.sensor.LightSensorArray;
import lejos.nxt.I2CPort;

public class LSAProcessingBrick {
	
	private LightSensorArray lsa = null;
	private BrickControlBrick brickControl;
	
	// length = 8
	private int[] mid_values = new int[]{-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] old_values = new int[]{0,0,0,0,0,0,0,0};
	private int counter = 1;

	public LSAProcessingBrick(BrickControlBrick brickControl, I2CPort port){
		lsa = new LightSensorArray(port);
		this.brickControl = brickControl;
	}
	
	public int lsa_calibrateBlack(){
		return lsa.calibrateBlack();
	}
	
	public int lsa_calibrateWhite(){
		return lsa.calibrateWhite();
	}
	
	//Daten werden nach dem 5. Aufruf Gesendet
	public void processData(){
		for(int i = 0; i < 8; i++){
			mid_values[i] = (counter * mid_values[i] + lsa.getLightValues()[i]) / counter + 1;
			counter++;
		}
		if((counter == 5) && compareIntArr()){
		for(int i = 0; i < 8; i++){
			old_values[i] = mid_values[i];
			//TODO: eventuell eine Warteschleife?
			brickControl.sendData(2, i + 1, mid_values[i]);
		}
		counter = 1;
		}
	}
	
	private boolean compareIntArr(){
		for(int i = 0; i < 8; i++){
			if(old_values[i] != mid_values[i]){
				return false;
			}
		}
		return true;
	}
}
