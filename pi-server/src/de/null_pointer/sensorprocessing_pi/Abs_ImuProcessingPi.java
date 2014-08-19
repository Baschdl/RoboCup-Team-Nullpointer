package de.null_pointer.sensorprocessing_pi;

public class Abs_ImuProcessingPi {

	public Abs_ImuProcessingPi(){}
	
	private int[] tiltData = new int[]{-1,-1,-1};
	private int[] acceleration = new int[]{-1,-1,-1};
	private int[] magneticField = new int[]{-1,-1,-1};
	private int[] gyro = new int[]{-1,-1,-1};
	private int compassHeading = -1;
	
	public void setTiltData(int index, int value){
		tiltData[index] = value;
	}
	
	public void setAcceleration(int index, int value){
		acceleration[index] = value;
	}
	
	public void setMagneticField(int index, int value){
		magneticField[index] = value;
	}
	
	public void setGyro(int index, int value){
		gyro[index] = value;
	}
	
	public void setCompassHeading(int value){
		compassHeading = value;
	}
}
