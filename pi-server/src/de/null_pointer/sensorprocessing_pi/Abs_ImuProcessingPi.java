package de.null_pointer.sensorprocessing_pi;

public class Abs_ImuProcessingPi {

	public Abs_ImuProcessingPi(){}
	
	private int angle = 0;
	private int heading = 0; // 0 = N, 1 = O, 2 = S, 3 = W
	
	public void setAngle(int angle){
		this.angle = angle;
		
		if(angle < 45 && angle > 315){
			heading = 0;
		}else if(angle < 135 && angle > 45){
			heading = 1;
		}else if(angle < 225 && angle > 135){
			heading = 2;
		}else{
			heading = 3;
		}
	}
	
	public int getAngle(){
		return angle;
	}
	
	public int getHeading(){
		return heading;
	}
}
