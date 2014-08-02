package de.null_pointer.sensorprocessing_brick;

import de.null_pointer.communication_brick.BrickControlBrick;
import lejos.nxt.ADSensorPort;
import lejos.nxt.addon.EOPD;

public class EOPDProcessingBrick {
	
	private EOPD eopd;
	private BrickControlBrick brickControl;
	private int sensorID;
	
	private int old_value = -1;
	private int value = 0;
	
	public EOPDProcessingBrick(BrickControlBrick brickControl, int sensorID, ADSensorPort port, boolean longRange){
		eopd = new EOPD(port, longRange);
		this.brickControl = brickControl;
		this.sensorID = sensorID;
	}
	
	public void setLongRange(boolean longRange){
		if(longRange){
			eopd.setModeLong();
		}else{
			eopd.setModeShort();
		}
	}
	
	public void processData(){
		if(old_value != (value = eopd.readRawValue())){
			old_value = value;
			brickControl.sendData(sensorID, 1, value);
		}
	}
}
