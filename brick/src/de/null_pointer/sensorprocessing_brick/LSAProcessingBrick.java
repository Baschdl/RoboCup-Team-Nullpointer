package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import de.null_pointer.sensor.LightSensorArray;
import lejos.nxt.I2CPort;

public class LSAProcessingBrick {
	
	private LightSensorArray lsa = null;
	private BrickControlBrick brickControl;

	private int[][] values = {{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0},{0,0,0,0,0}};
	private int[] midValues = new int[]{-1,-1,-1,-1,-1,-1,-1,-1};
	
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
	
	public void processData(){
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 8; j++){
				values[j][j] = values[j+1][j];
			}
		}
		for(int i = 0; i < 8; i++){
			values[5] = lsa.getLightValues();
		}
		
		int[] buffer  = {0,0,0,0,0,0,0,0};
		
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 5; j++){
				buffer[j] += values[j][i];
			}
		}
		for(int i = 0; i < 8; i++){
			buffer[i] /= 5;
		}
		for(int i = 0; i < 8; i++){
			if(buffer[i] != midValues[i]){
				midValues[i] = buffer[i];
				brickControl.sendData(2, i + 1, midValues[i]);
			}
		}
	}
}
